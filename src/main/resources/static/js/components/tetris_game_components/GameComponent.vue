<template>
  <div v-if="!game.field2" style="text-align: center; display: flex; flex-wrap: wrap; justify-content: center">
    <div>

      <p style="margin-top:-20px; font-size: 22px; font-family: 'Droid Sans'; color:#3f51b5 ">
        {{ toTimeFormat(Math.abs(game.player1Time)) }}</p>
      <Canvas
          :active="solo"
          :field="game.field1"
          :isActive="true"
          :isShowing="false"
          :makeMove="makeMove"
          style="margin-top:-20px;"
      />


    </div>
    <div style="float: left; margin-left: 10px; margin-top: 50px;">
      <MiniCanvas
          :field="game.nextFigure1"
          :isActive="true"
          :isShowing="true"/>
      <ProfileInTetrisGame :field="game.field1" :game="game" :player_points="game.player1Points" :surrender="true"
                           :player="player"></ProfileInTetrisGame>

    </div>
  </div>


  <div class="container" v-else>
    <div style="display: flex; flex-wrap: wrap; justify-content: center">
      <p style="font-size: 22px; font-family: 'Droid Sans'; color:#3f51b5 ">
        {{ toTimeFormat(Math.abs(game.player1Time)) }}</p>
    </div>
    <div class="row">
      <div class="col" style="display: flex; flex-wrap: wrap; justify-content: center">
        <div>
          <Canvas
              :field="game.player1.id == player.id ? game.field1 : game.field2"
              :isActive="true"
              :isShowing="false"
              :makeMove="makeMove"
              :active="!solo"
              style="margin-top:-20px;"
          />


        </div>
        <div style="float: left; margin-left: 10px; margin-top: -20px;">
          <MiniCanvas
              :field="game.player1.id == player.id ? game.nextFigure1 : game.nextFigure2"
              :isActive="true"
              :isShowing="true"/>
          <ProfileInTetrisGame :field="game.player1.id == player.id ? game.field1 : game.field2"
                               :game="game"
                               :player_points="game.player1.id == player.id ? game.player1Points : game.player2Points"
                               :surrender="true"
                               :player="game.player1.id == player.id ? game.player1 : game.player2"></ProfileInTetrisGame>

        </div>
      </div>


      <div class="col" style="display: flex; flex-wrap: wrap; justify-content: center">
        <div>
          <Canvas
              :field="game.player2.id == player.id ? game.field1 : game.field2"
              :isActive="false"
              :isShowing="true"
              :small="true"
              style="margin-top:-20px;"
          />
        </div>
        <div style="float: left; margin-left: 5px; margin-top: -30px;">
          <ProfileInTetrisGame :field="game.field2"
                               :game="game"
                               :player_points="game.player2.id == player.id ? game.player1Points : game.player2Points"
                               :surrender="false"
                               :player="game.player1.id == player.id ? game.player2 : game.player1"></ProfileInTetrisGame>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios'
import Canvas from './Canvas.vue'
import MiniCanvas from "./MiniCanvas.vue";
import ProfileInTetrisGame from "./ProfileInTetrisGame.vue";

export default {
  name: 'GameComponent',
  components: {ProfileInTetrisGame, MiniCanvas, Canvas},
  props: ['player', 'entity', 'game'],
  data: function () {
    return {
      config: {
        headers: {
          'Content-Type': 'multipart/form-data;application/json',
          'Access-Control-Allow-Origin': '*',
        },
      },
      solo: false
    }
  },
  created(){
    if(!this.game.player2){
      this.solo = true;
    }
  },
  methods: {
    makeMove(moveCode) {
      // console.log(moveCode)
      if (this.player.id == this.game.player1.id && this.game.game1Stop ||
          this.game.player2 && this.player.id == this.game.player2.id && this.game.game2Stop) {
        return;
      }
      let data = {
        player_id: this.player.id,
        game_id: this.game.gameId,
        move_code: moveCode,
      }
      axios.post('/api/tetris_game/move', data, this.config).then(response => {
      })
    },
    toTimeFormat(millis) {
      let seconds = Math.floor((millis / 1000) % 60);
      let minutes = Math.floor(millis / 1000 / 60);
      let secondsStr = seconds > 9 ? seconds : "0" + seconds;
      let minutesStr = minutes > 9 ? minutes : "0" + minutes;
      return minutesStr + ":" + secondsStr;
    }
  },
}
</script>

<style></style>
