<template>
  <div style="width:90%;" class="card">
    <div class="card-body">
      <div class="d-flex flex-column align-items-center text-center">

        <div class="search-div" style="width:100%">
          <input type="search" v-model="stringSearch" class="search" id="search" name="search"
                 placeholder="Search (name or email)">
          <button v-on:click="search">
            <a>
              <i class=" fa-solid fa-magnifying-glass fa-beat " style="color: #ffffff;"></i>
            </a>
          </button>
        </div>


        <div class="table-scroll table-div">
          <table class="table " style="overflow-y:scroll; scroll-behavior:smooth">
            <thead style="height: 20px">
            <tr>
              <th class="col1" scope="col">№</th>
              <th class="col2" scope="col">Фото</th>
              <th class="col3" scope="col">Логин</th>
              <th class="col4" scope="col">E-mail</th>
              <th class="col5" scope="col">Рейтинг</th>
            </tr>
            </thead>
          </table>
          <div ref="scroll_table" class="table-scroll-body">
            <table class="main-table" ref="table" v-if="!searchProcessing">
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
                <td class="col4">{{ player.email }}</td>
                <td class="col5">{{ player.rating }}</td>
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
  name: "PlayersListPage",
  data: function () {
    return {
      stringSearch: "",
      searchProcessing: false,
      players: [],
      readyPhotos: 0,
      photos: [],
      waitingTable: false,
      stopTable:false,
      batchSize: 10,
      from: 0,
      to: 10,
      lastStringSearch: "",
      config: {
        headers: {
          'Content-Type': 'multipart/form-data;application/json',
          "Access-Control-Allow-Origin": "*",
        }
      },
    }
  },
  mounted() {
    this.addScrollListener();
  },
  methods: {
    search() {
      this.searchProcessing = true
      this.players = []
      this.photos = []
      this.from = 0;
      this.to = 10;
      this.stopTable = false;
      this.startSearch();
    },
    startSearch() {
      if (this.stopTable){
        return
      }
      playerApi.searchPlayers(this.stringSearch,this.from, this.to,(err,data,resp)=>{
        if (this.players.length === 0) {
          this.players = data
        } else {
          this.players = this.players.concat(data);
        }
        if(data.length < this.batchSize){
          this.stopTable = true;
        }
        this.from = this.to;
        this.to += this.batchSize;
        this.waitingTable = false;
        this.searchProcessing = false

        this.setPhotos()
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
          this.startSearch();
        }
      })
    },
    setPhotos(){
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
        if (this.photos.length === 0) {
          this.photos = res.data;
        } else {
          this.photos = this.photos.concat(res.data)
        }
        this.readyPhotos += res.data.length
      })
    }
  }
}
</script>

<style>

.search-div {
  width: 100%
}

.search-div .search {
  width: 80%;
  margin-bottom: 10px;
}

.search-div button {
  margin-left: 4px;
  width: 10%;
}

.search-div button i {
  color: black
}

</style>

