<template>
  <div class="modal fade" id="showGame" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
    <div style="width:800px;height: 100%" class="modal-dialog">
      <div style="width:800px;height: 103%" class="modal-content">
        <div style="width:800px;" class="modal-header">
          <h1 class="modal-title fs-5" id="exampleModalLabel">Game Replay</h1>
          <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
        </div>
        <div style="width:800px;" class="modal-body">
          <ShowTttGameComponent v-if="gameSettings && isTtt" :game="game"
                                :waitingGame="waitingGame" :entity="entity" :withoutChat="gameSettings.withoutChat"></ShowTttGameComponent>

          <div style="width:100%" v-else>
            <div style="width:80px;height:80px;margin:25% 45%" class="spinner-border text-primary" role="status">
              <span style="margin:auto" class="visually-hidden">Loading..</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>


<script>
import axios from "axios";
import {TTT_GAME_CODE} from "../service/TttGameHelper";
import ShowTttGameComponent from "./ttt_game_components/ShowTttGameComponent.vue";
import {oneByIdPath} from "../service/api/player";
import {TTT_MOVE_ALL} from "../service/api/ttt";

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
      waitingGame: false,
      entity: null
    }
  },
  mounted() {
  },
  watch: {
    gameSettings(newV, oldV) {
      if (newV) {
        if (newV.code === TTT_GAME_CODE) {//show ttt game
          this.waitingGame = true;
          axios.post(TTT_MOVE_ALL, {id: newV.id}, this.config).then(result => {
            this.game = result.data;
            this.isTtt = true;
            this.waitingGame = false

            let entityId;
            if (this.$route.params.id){
              if(this.game.game.player2Id){
                if (this.game.game.player1Id == this.$route.params.id){
                  entityId = this.game.game.player2Id
                }else{
                  entityId = this.game.game.player1Id;
                }
              }else{
                entityId = -1;
              }
            }else{
              if (this.game.game.player1Id === this.$store.state.player.id) {
                if (this.game.game.player2Id) {
                  entityId = this.game.game.player2Id
                } else {
                  entityId = -1;
                }
              } else {
                entityId = this.game.game.player1Id;
              }
            }

            console.log('id:',entityId)
            console.log(this.game.game)

            if (entityId !== -1) {
              axios.get(oneByIdPath(entityId)).then((result) => {
                this.entity = result.data;
              })
            }else{
              this.entity = null;
            }
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