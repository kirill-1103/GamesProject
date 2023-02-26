<template>
  <div class="card games_list_card">
    <div class="card-body">
      <div class="d-flex flex-column align-items-center text-center">
        <h2 style="color:mediumblue">Список игр</h2>
        <div data-bs-spy="scroll" data-bs-target="#navbar-example2" data-bs-root-margin="0px 0px -40%"
             data-bs-smooth-scroll="true" class="scrollspy-example bg-body-tertiary p-3 rounded-2 scroll-div"
             tabindex="0">
          <div class="card game-card" @click="goto_ttt">
            <div class="card-body">
              <div class="d-flex flex-column align-items-center text-center">
                <h3>Крестики-Нолики</h3>
                <img src="../../img/ttt-logo.jpg" alt="ttt-game-img">
              </div>
            </div>
          </div>
          <div class="card game-card">
            <div class="card-body">
              <div class="d-flex flex-column align-items-center text-center">
                <h3>Другая игра</h3>
                <img src="../../img/other_game.png">
              </div>
            </div>
          </div>
          <div class="card game-card">
            <div class="card-body">
              <div class="d-flex flex-column align-items-center text-center">
                <h3>Другая игра</h3>
                <img src="../../img/other_game.png">
              </div>
            </div>
          </div>
          <div class="card game-card">
            <div class="card-body">
              <div class="d-flex flex-column align-items-center text-center">
                <h3>Другая игра</h3>
                <img src="../../img/other_game.png">
              </div>
            </div>
          </div>


        </div>

      </div>
    </div>
  </div>
</template>

<script>
import {TTT_GAME_PAGE_NAME} from "../router/component_names.js";
import {TTT_GAME_CODE} from "../service/TttGameHelper";

export default {
  name: "GameListPage",
  data: function(){
    return{
      intervalHasBeenKilled:false
    }
  },
  methods: {
    goto_ttt() {
      if (this.canGo() && this.intervalHasBeenKilled)
        this.$router.push({name: TTT_GAME_PAGE_NAME})
    },
    canGo(){
      return this.$store.state.player && this.$store.state.player.id
          && this.$store.state.player.id !== -1;
    }
  },
  mounted() {

    let interval = setInterval(()=>{
      if(this.canGo()){
        if(this.$store.state.playerGameId){
          console.log(this.$store.state.playerGameId)
          if(this.$store.state.playerGameId){
            if(this.$store.state.playerGameId !== -1){
              if(this.$store.state.playerGameCode === TTT_GAME_CODE){
                this.goto_ttt();
                this.intervalHasBeenKilled = true;
                clearInterval(interval);
              }
            }else{
              this.intervalHasBeenKilled = true;
              clearInterval(interval)
            }
          }
        }
      }
    },10)
  }
}
</script>

<style>
.games_list_card {
  height: 90%;
  width: 94%;
  margin-left: 3%;
  margin-top: 5%;
}

.table-scroll-body {
  height: 500px;
}

.scroll-div {
  margin-top: 20px;
  position: relative;
  height: 500px;
  width: 80%;
  overflow: auto;
}

.game-card {
  width: 80%;
  margin-left: 10%;
}

.game-card img {
  width: 200px;
  height: 200px;
  border-radius: 10px;
  box-shadow: 4px 4px 8px black;
}

.card:focus {
  background-color: white;
}

.game-card:hover {
  background-color: white;
  color: cornflowerblue;
  cursor: pointer;
}

.game-card:hover img {
  box-shadow: 5px 5px 9px cornflowerblue;
}

</style>

