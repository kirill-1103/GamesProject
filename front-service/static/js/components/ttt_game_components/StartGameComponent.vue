<template>
  <div style="text-align: center">
    <div style="text-align: center">
      <img class="game-img" src="../../../img/ttt-logo.jpg" alt="ttt-game-img">
    </div>


    <div class="input-div">
      <div class="dropdown">
        <button id="time_button" class="settings-input dropdown-toggle" data-bs-toggle="dropdown">
          Время игрока: {{ time_string }}
        </button>

        <ul class="dropdown-menu">
          <li>
            <button class="dropdown-item" @click="setTime(1, '1 минута')">1 минута</button>
          </li>
          <li>
            <button class="dropdown-item" @click="setTime(5,'5 минут')">5 минут</button>
          </li>
          <li>
            <button class="dropdown-item" @click="setTime(30,'30 минут')">30 минут</button>
          </li>
          <li>
            <button class="dropdown-item" @click="setTime(-1,'Без времени')">Без времени</button>
          </li>
        </ul>
      </div>

      <div class="dropdown">
        <button id="size_button" class="settings-input dropdown-toggle" data-bs-toggle="dropdown">
          Размер поля: {{ field_size_text }}
        </button>

        <ul class="dropdown-menu">
          <li>
            <button class="dropdown-item" @click="setSize(3, '3x3')">3x3</button>
          </li>
          <li>
            <button class="dropdown-item" @click="setSize(5,'5x5')">5x5</button>
          </li>
          <li>
            <button class="dropdown-item" @click="setSize(7,'7x7')">7x7</button>
          </li>
          <li>
            <button class="dropdown-item" @click="setSize(13,'13x13')">13x13</button>
          </li>
        </ul>
      </div>

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
import {connectToSearchResult} from "../../service/ws";
import {TTT_GAME_CODE} from "../../service/GameHelper";
import {TTT_NEW_PATH, TTT_SEARCH_PATH, TTT_STOP_SEARCH_PATH} from "../../service/api/ttt";

export default {
  name: "StartGameComponent",
  components: {SearchGameModal},
  props: ["startGame"],
  data: function () {
    return {
      time_string: "5 минут",
      field_size_text: "3x3",
      settings: {
        time: 5,
        field_size: 3,
        complexity: 1
      },
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
    setTime(time, time_str) {
      this.settings.time = time;
      this.time_string = time_str;
      document.getElementById("time_button").innerText = 'Время игрока: ' + this.time_string;
    },
    setSize(size, size_str) {
      this.settings.field_size = size;
      this.field_size_text = size_str;
      document.getElementById("size_button").innerText = 'Размер поля: ' + this.field_size_text;
    },
    computer() {
      if (this.alreadyStart) {
        return;
      }
      this.alreadyStart = true;
      let data = {
        player1_id: this.$store.state.player.id,
        field_size: this.settings.field_size,
        base_player_time: this.settings.time,
        complexity: this.settings.complexity,
      }
      console.log(this.$store.state.player)
      axios.post(TTT_NEW_PATH, data, this.config).then((response) => {
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
        size_field: this.settings.field_size,
        base_player_time: this.settings.time,
      }
      axios.post(TTT_SEARCH_PATH, data, this.config2)
          .then(()=>{      this.$store.commit("setInSearch", true);})
      connectToSearchResult(data.player_id, this.setSettingsAndStartGame)
    },
    setSettingsAndStartGame(game) {
      this.gameStarting = true;
      this.$store.commit("setPlayerGameCode", TTT_GAME_CODE);
      this.$store.commit("setPlayerGameId", game);
      this.$store.commit("setInSearch", false);
      this.alreadyStart = false;
      location.reload();
      // this.startGame(game);
    },
    stopSearch() {
      let data = {
        player_id: this.$store.state.player.id
      }
      axios.post(TTT_STOP_SEARCH_PATH, data, this.config2)
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
