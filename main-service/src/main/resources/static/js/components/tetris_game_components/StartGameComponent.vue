<template>
  <div style="text-align: center">
    <div style="text-align: center">
      <img class="game-img" src="../../../img/tetris-logo.jpg" alt="ttt-game-img">
    </div>

    <div class="input-div">

      <button :disabled="$store.state.playerGameId !== -1 || $store.state.inSearch" class="settings-input" @click="online"
              data-bs-toggle="modal" data-bs-target="#tttSearchModal" >Игра онлайн
      </button>

      <button :disabled="$store.state.playerGameId !== -1" class="settings-input" @click="computer">Игра с компьютером
      </button>

    </div>
  </div>
  <SearchGameModal  :gameStarting="gameStarting" :player="$store.state.player" :stopSearch="stopSearch"/>
</template>

<script>
import axios from "axios";
import SearchGameModal from "../SearchGameModal.vue";
import {connectToSearchResult, connectToTetrisSearchResult} from "../../service/ws";
import {TETRIS_GAME_CODE} from "../../service/GameHelper";

export default {
  name: "StartGameComponent",
  components: {SearchGameModal},
  data: function () {
    return {
      alreadyStart: false,
      config: {
        headers: {
          'Content-Type': 'multipart/form-data;application/json',
          "Access-Control-Allow-Origin": "*",
        }
      },
      config2: {
        headers: {
          'Content-Type': 'application/json',
          'Accept': 'application/json'
        }
      },
      disabled: true,
      gameStarting:false,
    }
  },
  created() {
    let interval = setInterval(() => {
      if (this.$store.state.player && this.$store.state.player.id) {
        this.stopSearch()
        clearInterval(interval);
      }
    }, 10)
  },
  mounted() {
  },
  methods: {
    computer() {
      if (this.alreadyStart) {
        return;
      }
      this.alreadyStart = true;
      let data = {
        player_id: this.$store.state.player.id
      }
      axios.post("/api/tetris_game/computer", data, this.config).then((response) => {
        console.log(response.data);
        this.setSettingsAndStartGame(response.data.id)
      })
    },
    online() {
      if (this.alreadyStart || this.$store.state.inSearch) {
        return;
      }
      this.alreadyStart = true;
      let data = {
        player_id: this.$store.state.player.id,
      }
      axios.post("/api/tetris_game/search", {player_id: this.$store.state.player.id,}, this.config)
          .then(()=>{      this.$store.commit("setInSearch", true);})
      connectToTetrisSearchResult(data.player_id, this.setSettingsAndStartGame)
    },
    setSettingsAndStartGame(gameId) {
      this.gameStarting = true;
      this.$store.commit("setPlayerGameCode", TETRIS_GAME_CODE);
      console.log(gameId)
      this.$store.commit("setPlayerGameId", gameId);
      this.$store.commit("setInSearch", false);
      this.alreadyStart = false;
      location.reload();
    },
    stopSearch() {
      axios.post("/api/tetris_game/stop_search", {player_id: this.$store.state.player.id}, this.config)
          .catch(r=>{
        // console.log(r);
      })
      this.$store.commit("setInSearch", false);
      this.alreadyStart=false;
    },
    setSearchProcessTrue(){
      this.searchProcess = true;
    }
  }
}
</script>

<style>
.game-img {
  width: 200px;
  height: 200px;
  border-radius: 10px;
  box-shadow: 4px 4px 8px black;
}

.input-div {
  min-width: 320px;
  width: 50%;
  margin: 20px auto;
}

.settings-input {
  width: 80%;
  margin-top: 20px;
}
</style>
