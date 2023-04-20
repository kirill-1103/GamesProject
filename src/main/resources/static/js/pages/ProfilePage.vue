<template>
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
            <button data-bs-toggle="modal" data-bs-target="#editProfileModal" style="width:100%">Редактировать</button>
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
        <!--        TODO: таблица с историей игр: номер, название игры, противник, итог, время игры
                    При нажатии в крестиках ноликах на номер - поле + длительность игры (может быть последовательность ходов)
                    При нажатии на противника - профиль противника-->
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

    <EditProfileModal :player="player"/>

    <ShowGameModal :gameSettings="gameSettingsForModal"/>

  </div>


</template>

<script>
import axios from "axios";
import updateAuthUserInStorage from "../service/auth.js";
import {fromArrayToDate, fromArrayToDateWithTime} from "../service/datetime";
import EditProfileModal from "../components/EditProfileModal.vue";
import ShowGameModal from "../components/ShowGameModal.vue";
import {TTT_GAME_CODE} from "../service/TttGameHelper"

export default {
  name: "ProfilePage",
  components: {
    ShowGameModal,
    EditProfileModal
  },
  data: function () {
    return {
      player: {login: null},
      playerTop: 0,
      signUpTime: '',
      config: {
        headers: {
          'Content-Type': 'multipart/form-data;application/json',
          "Access-Control-Allow-Origin": "*",
        }
      },
      imgSrc: '',
      games: null,
      tttGameCode: TTT_GAME_CODE,
      from: 0,
      to: 15,
      waitingTable: false,
      stopTable: false,
      gameSettingsForModal: {
        id: null,
        code: null
      }
    }
  },
  created() {
    updateAuthUserInStorage(this.$store).then(() => {//get player
      this.player = this.$store.state.player;
      this.signUpTime = fromArrayToDate(this.player.signUpTime);
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
    })
  },
  mounted() {
    this.getGamesTable();
    this.addScrollListener();
    this.getPlayerTop();
  },
  methods: {
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
              console.log('here1')
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
          clearInterval(interval);
        }
      }, 100)
    },

    addScrollListener() {
      this.$refs.scroll_table.addEventListener("scroll", (event) => {
        let table = this.$refs.scroll_table;
        if (Math.abs((table.scrollHeight - table.scrollTop) - table.clientHeight)<1 && !this.waitingTable && !this.stopTable) {
          this.getGamesTable();
        }
      })
    },

    showGame(id) {
      let gameCodeForModal = this.games.filter((game) => game.id === id)[0].code;
      let gameIdForModal = id;
      this.gameSettingsForModal = {
        id: gameIdForModal,
        code: gameCodeForModal
      }
    },

    getPlayerTop() {
      let interval = setInterval(() => {
        if (this.player.login !== null) {
          axios.get("/api/player/top/"+this.player.id).then(res=>{
            this.playerTop = res.data;
          })
          clearInterval(interval);
        }
      }, 100)
    }
  }
}
</script>

<style>


.card2 {
  box-shadow: 0 1px 3px 0 rgba(0, 0, 0, .1), 0 1px 2px 0 rgba(0, 0, 0, .06);
}

.card {
  position: relative;
  display: flex;
  flex-direction: column;
  width: 45%;
  margin-top: 10px;
  word-wrap: break-word;
  background-clip: border-box;
  border: 0 solid rgba(0, 0, 0, .125);
  border-radius: .25rem;
  float: left;
}

.col1 {
  width: 4%;
}

.col2 {
  width: 20%;
}

.col3 {
  width: 30%;
}

.col4 {
  width: 18%;
}

.card-body {
  background-color: #fafafa;
  flex: 1 1 auto;
  min-height: 1px;
  padding: 1rem;
}

th, td {
  font-size: 12px !important;;
}

.gutters-sm {
  margin-right: -8px;
  margin-left: -8px;
}

.gutters-sm > .col, .gutters-sm > [class*=col-] {
  padding-right: 8px;
  padding-left: 8px;
}

.main-table tr:hover {
  background: aliceblue !important;
  cursor: pointer;
}

</style>