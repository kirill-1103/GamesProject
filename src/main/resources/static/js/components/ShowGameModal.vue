<template>
  <div  class="modal fade" id="showGame" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
    <div style="width:800px;height: 100%" class="modal-dialog">
      <div style="width:800px;height: 103%" class="modal-content">
        <div style="width:800px;" class="modal-header">
          <h1 class="modal-title fs-5" id="exampleModalLabel">Game Replay</h1>
          <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
        </div>
        <div style="width:800px;" class="modal-body">
          <ShowTttGameComponent  v-if="gameSettings && isTtt" :game="game" :waitingGame = "waitingGame"></ShowTttGameComponent>

          <div style="width:100%" v-else>
            <div  style="width:80px;height:80px;margin:25% 45%" class="spinner-border text-primary" role="status">
              <span style="margin:auto" class="visually-hidden">Loading..</span>
            </div>
          </div>
        </div>
        <div class="modal-footer">
          <button type="button" class="button-close" data-bs-dismiss="modal">Закрыть</button>
        </div>
      </div>
    </div>
  </div>
</template>


<script>
import axios from "axios";
import {TTT_GAME_CODE} from "../service/TttGameHelper";
import ShowTttGameComponent from "./ttt_game_components/ShowTttGameComponent.vue";

export default {
  name: "ShowGame",
  props: ["gameSettings"],
  components: {ShowTttGameComponent},
  data: function () {
    return {
      config: {
        headers: {
          'Content-Type': 'multipart/form-data;application/json',
          "Access-Control-Allow-Origin": "*"
        }
      },
      game: null,
      isTtt: false,
      waitingGame:false
    }
  },
  mounted() {
  },
  watch:{
    gameSettings(newV,oldV){
      if (newV) {
        if (newV.code === TTT_GAME_CODE) {//show ttt game
          this.waitingGame = true;
          axios.post("/api/ttt_move/all", {id: newV.id}, this.config).then(result => {
            this.game = result.data;
            this.isTtt = true;
            this.waitingGame = false
          })
        }
      }
    }
  },
  updated() {
  }
}
</script>

<style>

</style>