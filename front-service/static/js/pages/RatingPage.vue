<template>
  <div style="width:90%;" class="card card2">
    <div class="card-body card-body2">
      <div class="d-flex flex-column align-items-center text-center">
        <h2>Рейтинг</h2>
        <div class="table-scroll table-div">
          <table class="table " style="overflow-y:scroll; scroll-behavior:smooth">
            <thead style="height: 20px">
            <tr>
              <th class="col1" scope="col">№</th>
              <th class="col2" scope="col">Фото</th>
              <th class="col3" scope="col">Логин</th>
              <th class="col4" scope="col">Рейтинг</th>
            </tr>
            </thead>
          </table>
          <div ref="scroll_table" class="table-scroll-body">
            <table class="main-table" ref="table" v-if="players">
              <tbody>
              <tr v-for="[index,player] of players.entries()" v-on:click="goToPlayer(player.id)">
                <th class="col1" scope="row">{{ index + 1 }}</th>
                <td class="col2">
                  <div style="text-align: center" v-if="readyPhotos>index">
                    <img v-if="!photos[index]" src="../../img/default.png" alt="img" width="100">
                    <img v-if="photos[index]" v-bind:src="photos[index]" id="player_photo" alt="img" width="100"/>
                  </div>
                  <div v-else>
                    <div style="margin-top:50%;" class="spinner-border text-primary" role="status">
                      <span class="visually-hidden">Loading..</span>
                    </div>
                  </div>
                </td>
                <td class="col3">{{ player.login }}</td>
                <td class="col4">{{ player.rating }}</td>
              </tr>
              </tbody>
            </table>
            <div style="margin-top:20%;" v-else class="spinner-border text-primary" role="status">
              <span class="visually-hidden">Loading..</span>
            </div>

            <div v-if="waitingTable && players"
                 class="spinner-border text-primary" role="status">
              <span class="visually-hidden">Loading..</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import {IMAGES_PATH} from "../service/api/player";
import {playerApi} from "../service/openapi/config/player_openapi_config";

export default {
  name: "RatingPage",
  data: () => {
    return {
      players: null,
      photos: null,
      from: 0,
      to: 10,
      waitingTable: false,
      stopTable: false,
      config: {
        headers: {
          'Content-Type': 'multipart/form-data;application/json',
          "Access-Control-Allow-Origin": "*",
        }
      },
      readyPhotos: 0,
      batchSize: 10
    }
  },
  mounted() {
    this.getPlayers();
    this.addScrollListener();
  },
  methods: {
    getPlayers() {
      this.waitingTable = true;
      playerApi.getAllOrderedByRatingStepByStep(this.from, this.to, (error, data, response) => {
        if (error) {
          console.error(error)
        } else {
          if (data.length === 0) {
            this.stopTable = true;
            this.waitingTable = false;
            return;
          }
          if (this.players === null) {
            this.players = data
          } else {
            this.players = this.players.concat(data)
          }
          this.from = this.to;
          this.to += this.batchSize;
          this.waitingTable = false
          this.setPhotos();
        }
      })
    },
    goToPlayer(id) {
      this.$router.push("/player/" + id);
    },
    addScrollListener() {
      this.$refs.scroll_table.addEventListener("scroll", (event) => {
        let table = this.$refs.scroll_table;
        if (Math.abs((table.scrollHeight - table.scrollTop) - table.clientHeight) < 1 && !this.waitingTable && !this.stopTable) {
          console.log('here')
          this.getPlayers();
        }
      })
    },
    setPhotos() {
      let names = [];
      for (let player of this.players) {
        names.push(player.photo);
      }
      axios.post(IMAGES_PATH, names.slice(this.from - this.batchSize, this.to - this.batchSize)).then((res) => {
        for (let i = 0; i < res.data.length; i++) {
          if (res.data[i] !== null) {
            res.data[i] = "data:image/;base64, " + res.data[i].base64;
          }
        }

        if (this.photos == null) {
          this.photos = res.data;
        } else {
          this.photos = this.photos.concat(res.data)
        }
        this.readyPhotos += res.data.length
        // console.log(this.readyPhotos)
      })
    }
  }
}
</script>

<style>

</style>

