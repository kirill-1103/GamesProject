<template>
  <div style="text-align: center;font-size: 24px" v-if="notFound">404 - Такой страницы не существует :(</div>

  <div v-else>
    <div style="width:30%; height:550px;" class="card ">
      <div class="card-body">
        <div class="d-flex flex-column align-items-center text-center">
          <div v-if="player.login">
            <img v-if="!player.photo" src="../../img/default.png" alt="img" class="rounded-circle" width="150">
            <img v-if="player.photo" src={{imgSrc}} id="player_photo" alt="img" class="rounded-circle" width="150"/>
            <div class="mt-3">
              <h4>{{ player.login }}</h4>
              <p class="text-secondary mb-1">Логин: {{ player.login }}</p>
              <p class="text-secondary mb-1">Почта: {{ player.email }}</p>
              <p class="text-secondary mb-1">Рейтинг: {{ player.rating }}</p>
              <p class="text-secondary mb-1">Место в топе: {{ playerTop }}</p>
              <p class="text-secondary mb-1">Дата регистрации: {{ signUpTime }}</p>
              <button style="width: 100%" v-on:click="sendMessage">Написать</button>
            </div>
          </div>
          <div style="margin-top:50%;" v-else class="spinner-border text-primary" role="status">
            <span class="visually-hidden">Loading..</span>
          </div>
        </div>
      </div>

    </div>

    <div style="width:67%;" class="card">
      <div class="card-body">
        <div class="d-flex flex-column align-items-center text-center">
          <h4>История игр</h4>
          <div class="table-scroll table-div">
            <table class="table " style="overflow-y:scroll; scroll-behavior:smooth">
              <thead style="height: 20px">
              <tr>
                <th class="col1" scope="col">№</th>
                <th class="col2" scope="col">Игра</th>
                <th class="col3" scope="col">Противник</th>
                <th class="col4" scope="col">Итог</th>
                <th class="col5" scope="col">Время игры</th>
              </tr>
              </thead>
            </table>
            <div ref="scroll_table" class="table-scroll-body">
              <table class="main-table" ref="table" v-if="games">
                <tbody>
                <tr v-for="game of games" v-on:click="showGame(game.id)" data-bs-toggle="modal"
                    data-bs-target="#showGame">
                  <th class="col1" scope="row">{{ game.id }}</th>
                  <td class="col2">{{ game.name }}</td>
                  <td class="col3">{{ game.entityName }}</td>
                  <td class="col4">{{ game.result }}</td>
                  <td class="col5">{{ game.time }}</td>
                </tr>
                </tbody>
              </table>
              <div style="margin-top:20%;" v-else class="spinner-border text-primary" role="status">
                <span class="visually-hidden">Loading..</span>
              </div>

              <div v-if="waitingTable && games"
                   class="spinner-border text-primary" role="status">
                <span class="visually-hidden">Loading..</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <ShowGameModal :gameSettings="gameSettingsForModal"/>
  </div>
</template>

<script>

import axios from "axios";
import ShowGameModal from "../components/ShowGameModal.vue";
import {fromArrayToDate, fromArrayToDateWithTime} from "../service/datetime";
import {TTT_GAME_CODE} from "../service/TttGameHelper";
import updateAuthUserInStorage from "../service/auth";
import {CHAT_PAGE_NAME} from "../router/component_names";

export default {
  name: 'PlayerProfilePage',
  components: {
    ShowGameModal
  },
  data: () => {
    return {
      player: {login: null},
      notFound: false,
      signUpTime: null,
      imgSrc: null,
      games: null,
      tttGameCode: TTT_GAME_CODE,
      from: 0,
      to: 15,
      waitingTable: false,
      stopTable: false,
      playerTop: 0,
      gameSettingsForModal: {
        id: null,
        code: null
      },

      config: {
        headers: {
          'Content-Type': 'multipart/form-data;application/json',
          "Access-Control-Allow-Origin": "*",
        }
      },
    }
  },
  created() {
    updateAuthUserInStorage(this.$store).then(() => {
      if (this.$store.state.player.id == this.$route.params.id) {
        this.$router.replace("/me")
      } else {
        this.getGamesTable();
        this.addScrollListener();
        this.getUser(this.$route.params.id);
      }
    })
    this.getPlayerTop();
  },
  mounted() {

  },
  methods: {
    getUser(id) {
      console.log(id)
      axios.get("/api/player/" + id).then((result) => {
        console.log('hi')
        this.player = result.data
        this.signUpTime = fromArrayToDate(this.player.signUpTime)
        if (this.player.photo && this.player.photo !== '') {
          axios.post("/api/player/image", {img_name: this.player.photo}, this.config).then((result) => {
            this.imgSrc = "data:image/;base64, " + result.data;
            let img = document.getElementById("player_photo");
            img['src'] = this.imgSrc;
          }).catch(err => {
            console.log("ERR:");
            console.log(err)
          })
        }
      }).catch(err => {
        console.log('err')
        this.notFound = true;
      })
    },
    getGamesTable() {
      let interval = setInterval(() => {
        if (this.player.login !== null) {
          this.waitingTable = true;
          axios.post("/api/games/byplayer", {
            id: this.player.id,
            from: this.from,
            to: this.to
          }, this.config).then(result => {
            if (result.data.length === 0) {
              this.stopTable = true;
              this.waitingTable = false;
              return;
            }
            for (let game of result.data) {
              game.time = fromArrayToDateWithTime(game.time);
            }
            if (this.games === null) {
              this.games = result.data
            } else {
              this.games = this.games.concat(result.data)
            }
            this.from = this.to;
            this.to += 10;
            this.waitingTable = false;
          }).catch(error => {
            console.log("ERROR:" + error);
            console.log(error)
          });
          if (!this.games) {
            this.games = []
          }
          clearInterval(interval);
        }
      }, 100)
    },

    addScrollListener() {
      this.$refs.scroll_table.addEventListener("scroll", (event) => {
        let table = this.$refs.scroll_table;
        if (Math.abs((table.scrollHeight - table.scrollTop) - table.offsetHeight)<10 && !this.waitingTable && !this.stopTable) {
          this.getGamesTable();
        }
      })
    },
    showGame(id) {
      let gameCodeForModal = this.games.filter((game) => game.id === id)[0].code;
      this.gameSettingsForModal = {
        id: id,
        code: gameCodeForModal,
        withoutChat: true
      }
    },
    getPlayerTop() {
      let interval = setInterval(() => {
        if (this.$route.params.id !== null) {
          if(this.$route.params.id == undefined){
            clearInterval(interval)
            return;
          }
          axios.get("/api/player/top/" + this.$route.params.id).then(res => {
            this.playerTop = res.data;
          })
          clearInterval(interval);
        }
      }, 100)
    },
    sendMessage(){
      this.$router.push({name:CHAT_PAGE_NAME,params:{id:this.player.id}})
    }
  }
}

</script>

<style>

</style>

