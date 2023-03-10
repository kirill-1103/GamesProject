<template>
  <div  style="width:100%;text-align: center; font-size:16pt;" >
    <span v-if="game.moves[moveNumber-1]">    {{reformatTime(game.moves[moveNumber-1].gameTimeMillis)}}</span>
    <span v-else>00:00</span>
  </div>
  <div v-if="field && field.length !== 0 && !waitingGame">
    <TttCanvas  :field="field" :player=null :game="game.game" :isShowing=true :makeMove="()=>{}"></TttCanvas>
    <div style="width:500px;margin: 20px auto auto;">
      <div style="width:100%;text-align: center">
        <span v-text="whoWin"></span>
      </div>
      <br>
      <button style="width:49%;margin-right: 2%" @click="back">Назад</button>
      <button style="width:49%" @click="forward">Вперед</button>
    </div>
  </div>
  <div style="width:100%" v-else>
    <div  style="width:80px;height:80px;margin:25% 45%" class="spinner-border text-primary" role="status">
      <span style="margin:auto" class="visually-hidden">Loading..</span>
    </div>
  </div>
</template>

<script>
import {
  X,
  O,
  NONE,
  VICTORY_REASON_DRAW,
  VICTORY_REASON_PLAYER1_WIN,
  VICTORY_REASON_PLAYER1_TIME_WIN,
  VICTORY_REASON_PLAYER2_LOSE,
  VICTORY_REASON_PLAYER1_LOSE,
  VICTORY_REASON_PLAYER2_TIME_WIN, VICTORY_REASON_PLAYER2_WIN
} from "../../service/TttGameHelper";
import TttCanvas from "./TttCanvas.vue";
import {reformatTime} from "../../service/datetime";

export default {
  name: "ShowTttGameComponent",
  components: {TttCanvas},
  props: ["game","waitingGame"],
  data: function () {
    return {
      field: null,
      moveNumber:null,
      reformatTime:reformatTime,
      whoWin:''
    }
  },
  mounted() {
    if (this.game) {
      this.init();
    }
  },
  watch: {
    game(newV, oldV) {
      this.init();
    }
  },
  methods: {
    init() {
      this.moveNumber = this.game.moves.length;
      this.fillField();
      this.setWhoWin();
    },

    getFieldFromMoves(moves,sizeField) {
      let field = this.getEmptyFieldBySize(sizeField);
      let firstId = null
      if (moves.length !== 0) {
        firstId = moves[0].playerId;
      }
      for (let move of moves) {
        if(move.playerId === firstId){
          field[move.ycoord][move.xcoord] = X;
        }else{
          field[move.ycoord][move.xcoord] = O;
        }
      }
      return field;
    },
    getEmptyFieldBySize(size){
      let field = [];
      for(let i = 0;i<size;i++){
        field.push([]);
        for(let j = 0;j<size;j++){
          field[i].push(NONE);
        }
      }
      return field;
    },
    fillField(){
      this.field = this.getFieldFromMoves(this.game.moves.slice(0,this.moveNumber),this.game.game.sizeField)
    },
    back(){
      if(this.moveNumber !== 0){
        this.moveNumber -=1;
        this.fillField();
      }
    },
    forward(){
      if(this.moveNumber !== this.game.moves.length){
        this.moveNumber +=1;
        this.fillField();
      }
    },
    setWhoWin(){
      let playerId = this.$store.state.player.id
      let reasonCode = this.game.game.victoryReasonCode;
      if (reasonCode === VICTORY_REASON_DRAW) {
        this.setWhoWinText("Ничья!");
        return;
      }
      if (this.game.game.player1Id === playerId) {
        if (reasonCode === VICTORY_REASON_PLAYER1_WIN) {
          this.setWhoWinText("Вы выиграли!")
        } else if (reasonCode === VICTORY_REASON_PLAYER1_TIME_WIN) {
          this.setWhoWinText("Вы выиграли! У соперника закончилось время!")
        } else if (reasonCode === VICTORY_REASON_PLAYER2_LOSE) {
          this.setWhoWinText("Вы выиграли! Соперник сдался!")
        } else if (reasonCode === VICTORY_REASON_PLAYER1_LOSE) {
          this.setWhoWinText("Вы сдались!")
        } else if (reasonCode === VICTORY_REASON_PLAYER2_TIME_WIN) {
          this.setWhoWinText("Вы проиграли! У вас закончилось время!")
        } else if (reasonCode === VICTORY_REASON_PLAYER2_WIN) {
          this.setWhoWinText("Вы проиграли!")
        }
      }
      if (this.game.game.player2Id === playerId) {
        if (reasonCode === VICTORY_REASON_PLAYER1_WIN) {
          this.setWhoWinText("Вы проиграли!")
        } else if (reasonCode === VICTORY_REASON_PLAYER1_TIME_WIN) {
          this.setWhoWinText("Вы проиграли! У вас закончилось время!")
        } else if ( reasonCode === VICTORY_REASON_PLAYER2_LOSE) {
          this.setWhoWinText("Вы сдались!")
        } else if (reasonCode === VICTORY_REASON_PLAYER1_LOSE) {
          this.setWhoWinText("Вы выиграли! Соперник сдался!")
        } else if (reasonCode=== VICTORY_REASON_PLAYER2_TIME_WIN) {
          this.setWhoWinText("Вы выиграли! У соперника закончилось время!")
        } else if (reasonCode === VICTORY_REASON_PLAYER2_WIN) {
          this.setWhoWinText("Вы выиграли!")
        }
      }
    },
    setWhoWinText(text){
      this.whoWin = text;
    }
  }

}
</script>

<style>

</style>