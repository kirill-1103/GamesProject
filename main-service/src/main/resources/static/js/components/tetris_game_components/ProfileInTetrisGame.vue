<template>
  <div class="card profile-card">
    <div class="card-body profile-card">
      <div v-if="player.login">
        <div class="div_for_p">
          <p v-if="!player_points">Счет: 0</p>
          <p v-else>Счет: {{ player_points }}</p>
        </div>

        <div v-if="!player.photo">
          <img src="../../../img/default.png" alt="img" class="rounded-circle" width="150px">
        </div>
        <div v-if="imgData">
          <img :src="imgData" alt="img" class="rounded-circle" width="100">
        </div>
        <div v-if="player.photo && !imgData">
          <div style="margin-top:50%;" class="spinner-border text-primary" role="status">
            <span class="visually-hidden">Loading..</span>
          </div>
        </div>
        <div class="div_for_p">
          <p style="hyphens:auto; max-width: 150px;">Имя: {{ player.login }} </p>
        </div>
        <div class="div_for_p">
          <p> Рейтинг: {{ player.rating }} </p>
        </div>
        <button style="width:100%" v-if="surrender " @click="surrender_f">Сдаться</button>
      </div>

      <div style="margin-top:50%;" v-else class="spinner-border text-primary" role="status">
        <span class="visually-hidden">Loading..</span>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import {reformatTime} from "../../service/datetime";

export default {
  name: "ProfileInTetrisGame",
  props: ['player', 'surrender', "player_points", "field", "game"],
  data: function () {
    return {
      config: {
        headers: {
          'Content-Type': 'multipart/form-data;application/json',
          "Access-Control-Allow-Origin": "*",
        }
      },
      alreadySurrendered: false,
      reformatTime: reformatTime,
      imgData: false
    }
  },
  created() {
    this.getPhoto();
  },
  methods: {
    surrender_f() {
      if (!this.alreadySurrendered && !this.game.endTime) {
        this.alreadySurrendered = true;
        axios.post("/api/tetris_game/surrender", {game_id: this.game.gameId, player_id: this.player.id}, this.config)
      } else {
        alert("Игра уже завершена")
      }
    },
    getPhoto() {
      if(this.player.photo){
        axios.post("/api/player/image", {img_name: this.player.photo}, this.config)
            .then((result)=>{
              this.imgData = "data:image/;base64, "+result.data;
              console.log(this.imgData)
            })
      }
    }
  }
}
</script>

<style>
.profile-card {
  width: 100%;
  text-align: center;
}

.div_for_p {
  color: darkblue;
  font-size: 20px;
  margin-top: 40px;
}

.div_for_p:first-child {
  margin-top: 0px;
}


</style>