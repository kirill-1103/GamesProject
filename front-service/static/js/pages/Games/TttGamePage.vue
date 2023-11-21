<template>
  <div style="text-align: center">
    <h2>Крестики-Нолики</h2>
  </div>
  <StartGameComponent :startGame="startGame"
                      v-if="!$store.state.playerGameId || $store.state.playerGameId === -1"/>
  <div class="container" v-else>
    <div class="row">
      <div class="col">
        <ProfileInTttGame :field="field" :game="game" :player_time="otherPlayerTime" :surrender="false" :player="player_2"></ProfileInTttGame>
      </div>

      <div class="col"  style="text-align: center" v-if="!chat_b && field.length !== 0">
        <TttCanvas  :game=game :field="field" :player="player" :makeMove="makeMove" :isShowing="false">
        </TttCanvas>
        <span v-text="whoMoveText" style="font-size: 20px; color:brown; "></span>
      </div>


      <div class="div-chat col" v-else-if="chat_b && field.length !== 0">
        <GameChatComponent :messagesLoaded="messagesLoaded"
                           :messages="messages"
                           :game="game"
                           :player="player"
                           :addMessage="addMessageInChat"
        ></GameChatComponent>
      </div>

      <div class="col" v-else>
        <div   style="margin-left:38%;margin-top:38%;width:7rem; height:7rem;" class="spinner-border text-primary" role="status">
          <span  class="visually-hidden" >Loading..</span>
        </div>
      </div>

      <div class="col">
        <ProfileInTttGame :field="field" :game="game" :end="end" :player_time="playerTime" :surrender="true" :player="player"></ProfileInTttGame>
      </div>
    </div>

    <div class="buttons">
      <button @click="openChat">Чат</button>
      <button @click="openGame">Игра</button>
    </div>
  </div>
</template>

<script>
import StartGameComponent from "../../components/ttt_game_components/StartGameComponent.vue";
import ProfileInTttGame from "../../components/ttt_game_components/ProfileInTttGame.vue";
import updateAuthUserInStorage from "../../service/auth.js";
import GameChatComponent from "../../components/GameChatComponent.vue";
import TttCanvas from "../../components/ttt_game_components/TttCanvas.vue";
import {connectToGameMessages, connectToTttGame} from "../../service/ws.js";
import axios from "axios";
import {
  VICTORY_REASON_DRAW,
  VICTORY_REASON_PLAYER1_LOSE,
  VICTORY_REASON_PLAYER1_TIME_WIN,
  VICTORY_REASON_PLAYER1_WIN,
  VICTORY_REASON_PLAYER2_LOSE,
  VICTORY_REASON_PLAYER2_TIME_WIN,
  VICTORY_REASON_PLAYER2_WIN
} from "../../service/TttGameHelper";
import {fromArrayToHoursMinutesSeconds, fromStringToHoursMinutesSeconds} from "../../service/datetime";
import {img_path} from "../../service/api/player";
import {gameMessagesByCodeAndIdPath} from "../../service/api/game_message";
import {TTT_MAKE_MOVE_PATH, tttOneByPlayerIdPath} from "../../service/api/ttt";
import {playerApi} from "../../service/openapi/config/player_openapi_config";

export default {
  name: "TttGamePage",
  components: {StartGameComponent, ProfileInTttGame, GameChatComponent, TttCanvas},
  data: function () {
    return {
      player: {},
      player_2: {},
      chat_b: false,
      settings: {
        field_size: 0,
        time: 0
      },
      game: {},
      field: [],
      config: {
        headers: {
          'Content-Type': 'multipart/form-data;application/json',
          "Access-Control-Allow-Origin": "*",
        }
      },
      playerTime: 0,
      otherPlayerTime: 0,
      end: false,
      finished:false,
      messages:[],
      messagesLoaded:false,
      whoMoveText :''
    }
  },
  created() {
    if (this.$store.state.player) {
      this.player = this.$store.state.player
      this.player.img_data = this.$store.state.playerPhoto;
      this.startGame();
    } else {
      updateAuthUserInStorage(this.$store).then(() => {
        this.player = this.$store.state.player;
        if(this.player.photo){
          let interval =
              setInterval(()=> {
                if(this.$store.state.playerPhoto){
                  this.player.img_data = this.$store.state.playerPhoto;
                  clearInterval(interval)
                }
              },100)
        }
        this.startGame();
      })
    }

    this.player_2.login = "Компьютер"
    this.player_2.rating = "Без рейтинга"

  },

  methods: {
    openChat() {
      this.chat_b = true;
    },
    openGame() {
      this.chat_b = false;
    },
    startGame(game = null) {
      this.getGameAndConnect();
      this.setEnemyIfExists(game);
      this.startChat();
      this.getMessages();
    },
    getGameAndConnect() {
      if (this.$store.state.playerGameId) {
        this.updateGameFromDb();
      } else {
        updateAuthUserInStorage(this.$store, this.updateGameFromDb);
      }
    },
    updateGameFromDb() {
      axios.get(tttOneByPlayerIdPath(this.$store.state.playerGameId)).then((response) => {
        this.game = response.data;
        console.log(this.game)
        connectToTttGame(this.game.id, this.updateState, this.$store);
      }).catch((error) => {
        console.log(error)
      })
    },
    updateState(game) {
      this.game = game;
      this.field = this.game.field.field;
      this.setWhoMove();
      this.checkEndGame()
    },
    makeMove(x, y) {
      if (!this.game.endTime) {
        axios.post(TTT_MAKE_MOVE_PATH,
            {
              game_id: this.game.id,
              player_id: this.player.id,
              x: x,
              y: y
            }, this.config);
      } else {
        alert("Игра закончена");
      }
    },
    setEnemy(game){
      if(game && game.player2Id){
        let id;
        if(this.$store.state.player.id === game.player2Id){
          id = game.player1Id
        }else{
          id = game.player2Id
        }
        playerApi.getById(id, (error, data, response) => {
          if (error) {
            console.log(error)
          } else {
            this.player_2 = data
            if (this.player_2.photo) {
              axios.get(img_path(this.player_2.photo), this.config).then((result) => {
                this.player_2.img_data = "data:image/;base64, " + result.data.base64;
              }).catch(err => {
                console.log("ERR:");
                console.log(err)
              })
            }
          }
        })
      }
    },
    setEnemyIfExists(game){
      let interval = setInterval(()=>{
        if(!this.player_2.id){
          if(game && game.player2Id || this.game && this.game.player2Id){
            if(game){
              this.setEnemy(game);
            }else{
              this.setEnemy(this.game)
            }
            clearInterval(interval);
          }
        }else{
          if(this.game.player1Id && !this.game.player2Id){
            clearInterval(interval);
          }
        }
      },100);
    },
    checkEndGame() {
      if (this.game.endTime && !this.finished) {
        this.finished=true;
        this.whoMoveText = '';
        if (this.game.victoryReasonCode === VICTORY_REASON_DRAW) {
          alert("Ничья!");
          return;
        }
        if (this.game.player1Id === this.player.id) {
          if (this.game.victoryReasonCode === VICTORY_REASON_PLAYER1_WIN) {
            alert("Вы выиграли!")
          } else if (this.game.victoryReasonCode === VICTORY_REASON_PLAYER1_TIME_WIN) {
            alert("Вы выиграли! У соперника закончилось время!")
          } else if (this.game.victoryReasonCode === VICTORY_REASON_PLAYER2_LOSE) {
            alert("Вы выиграли! Соперник сдался!")
          } else if (this.game.victoryReasonCode === VICTORY_REASON_PLAYER1_LOSE) {
            alert("Вы сдались!")
          } else if (this.game.victoryReasonCode === VICTORY_REASON_PLAYER2_TIME_WIN) {
            alert("Вы проиграли! У вас закончилось время!")
          } else if (this.game.victoryReasonCode === VICTORY_REASON_PLAYER2_WIN) {
            alert("Вы проиграли!")
          }
        }
        if (this.game.player2Id === this.player.id) {
          if (this.game.victoryReasonCode === VICTORY_REASON_PLAYER1_WIN) {
            alert("Вы проиграли!")
          } else if (this.game.victoryReasonCode === VICTORY_REASON_PLAYER1_TIME_WIN) {
            alert("Вы проиграли! У вас закончилось время!")
          } else if (this.game.victoryReasonCode === VICTORY_REASON_PLAYER2_LOSE) {
            alert("Вы сдались!")
          } else if (this.game.victoryReasonCode === VICTORY_REASON_PLAYER1_LOSE) {
            alert("Вы выиграли! Соперник сдался!")
          } else if (this.game.victoryReasonCode === VICTORY_REASON_PLAYER2_TIME_WIN) {
            alert("Вы выиграли! У соперника закончилось время!")
          } else if (this.game.victoryReasonCode === VICTORY_REASON_PLAYER2_WIN) {
            alert("Вы выиграли!")
          }
        }
      }
    },
    setWhoMove(){
      if(this.whoMoveText !== null && this.whoMoveText!==undefined){
        if(!this.finished){
          if(this.player.id === this.game.player1Id && this.game.queue === 0
              || this.player.id === this.game.player2Id && this.game.queue === 1){
            this.whoMoveText = "Ваш ход"
          }else{
            this.whoMoveText = "Ход противника"
          }
        }else{
          this.whoMoveText = ""

        }
      }
    },
    getMessages(){
      let interval = setInterval(()=>{
        if(this.game && this.game.id && this.game.gameCode){
          axios.get(gameMessagesByCodeAndIdPath(this.game.id,this.game.gameCode))
              .then(messages=>{
                this.messages = messages.data;
                for (let mess of this.messages){
                  mess.time = fromArrayToHoursMinutesSeconds(mess.time)
                }
                this.messages = this.messages.reverse();
                connectToGameMessages(this.game.id, this.game.gameCode, this.addMessageInChat);
                this.messagesLoaded = true;

              })
          clearInterval(interval)
        }
      },100)
    },
    addMessageInChat(message){
      message.time = fromStringToHoursMinutesSeconds(message.time);
      this.messages.unshift(message)
    },
    startChat(){

    }
  },
  watch: {
    game(oldGame, newGame) {
      if (newGame.player1Id === this.player.id) {
        this.playerTime = newGame.player1Time;
        this.otherPlayerTime = newGame.player2Time;
      } else {
        this.playerTime = newGame.player2Time;
        this.otherPlayerTime = newGame.player1Time;
      }
    }
  }
}

// function getRandomInt(max) {
//  let coef=1;
//   if(Math.random()>0.5){
//     coef=-1;
//   }
//   return coef* Math.floor(Math.random() * max);
// }
</script>

<style>
.div_chat {
  width: 500px;
  height: 500px;
}

.buttons {
  width: 500px;
  margin: auto;
}

.buttons button {
  width: 48%;
  margin-top: 10px
}

.buttons button:first-child {
  margin-right: 4%;
}
</style>