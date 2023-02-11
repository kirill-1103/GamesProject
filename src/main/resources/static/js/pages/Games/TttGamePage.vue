<template>
  <div style="text-align: center">
    <h2>Крестики-Нолики</h2>
  </div>
  <StartGameComponent :startGame="startGame" v-if="!$store.state.playerGameId || $store.state.playerGameCodeId===''"/>
  <div class="container" v-else>
    <div class="row">
      <div class="col">
        <ProfileInTttGame :player_time="game.player1Time" :surrender="true" :player="player"></ProfileInTttGame>
      </div>

      <TttCanvas v-if="!chat_b" class="col">

      </TttCanvas>

      <div class="div-chat col" v-else>
        <GameChatComponent></GameChatComponent>
      </div>

      <div class="col">
        <ProfileInTttGame :player_time="game.player2Time" :surrender="false" :player="player_2"></ProfileInTttGame>
      </div>
    </div>

    <div class="buttons">
      <button @click="openChat">Чат</button>
      <button @click="openGame">Игра</button>
    </div>
  </div>
  <button @click="click">click</button>
</template>

<script>
import StartGameComponent from "../../components/ttt_game_components/StartGameComponent.vue";
import ProfileInTttGame from "../../components/ttt_game_components/ProfileInTttGame.vue";
import updateAuthUserInStorage from "../../service/auth.js";
import GameChatComponent from "../../components/GameChatComponent.vue";
import TttCanvas from "../../components/ttt_game_components/TttCanvas.vue";
import {sendMessageToConnectWithTime, connect} from "../../service/ws.js";
import axios from "axios";

export default {
  name: "TttGamePage",
  components: {StartGameComponent, ProfileInTttGame, GameChatComponent, TttCanvas},
  data: function () {
    return {
      ctx: null,
      canvas: null,
      width: 500,
      height: 500,
      player: {},
      player_2: {},
      chat_b: false,
      settings: {
        field_size: 0,
        time: 0
      },
      game: {},
      config : {
        headers: {
          'Content-Type': 'multipart/form-data;application/json',
          "Access-Control-Allow-Origin": "*",
        }
      }
    }
  },
  created() {
    console.log('b')
    if (this.$store.state.player) {
      this.player = this.$store.state.player
      this.player.img_data = this.$store.state.playerPhoto;
      this.startGame();
    } else {
      updateAuthUserInStorage(this.$store).then(() => {
        this.player = this.$store.state.player;
        this.player.img_data = this.$store.state.playerPhoto;
        this.startGame();
      })
    }

    //TODO: Сделать запрос на профиль противника
    this.player_2.login = "Компьютер"
    this.player_2.rating = "Без рейтинга"
  },
  mounted() {

  },
  methods: {
    openChat() {
      this.chat_b = true;
    },
    openGame() {
      this.chat_b = false;
    },
    click() {
      // this.sendMessage({"x_coord":0,"y_coord":0,"player_id":this.player.id,"game_id":1})
      axios.post("/api/ttt_game/change_queue",{game_id:this.game.id},this.config)
    },
    startGame() {
      console.log('start')
      this.getGameAndConnect();
    },
    getGameAndConnect() {
      if(this.$store.state.playerGameId){
        this.updateGameFromDb();
    }else{
        updateAuthUserInStorage(this.$store, this.updateGameFromDb)
      }
    },
    updateGameFromDb(){
      axios.get("/api/ttt_game/" + this.$store.state.playerGameId).then((response) => {
        console.log(response.data);
        this.game = response.data;
        connect(this.game.id,this.updateState,this.$store);
      }).catch((error) => {
        console.log(error)
      })
    },
    updateState(game){
      this.game = game;
    }
  },
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