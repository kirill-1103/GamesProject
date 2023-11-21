<template>
    <div class="card profile-card">
      <div class="card-body profile-card" >
        <div v-if="player.login">
          <div class="div_for_p">
            <p v-if="!player_time">Время: 00:00</p>
            <p v-else>Время: {{reformatTime(player_time)}}</p>
          </div>

          <div v-if="!player.photo">
            <img src="../../../img/default.png" alt="img" class="rounded-circle" width="100" >
          </div>
          <div v-if="player.img_data">
              <img   :src="player.img_data" alt="img" class="rounded-circle" width="100" >
          </div>
          <div v-if="player.photo && !player.img_data">
            <div style="margin-top:50%;"  class="spinner-border text-primary" role="status">
              <span class="visually-hidden">Loading..</span>
            </div>
          </div>
          <div class="div_for_p">
            <p style="max-width: 100%;">Имя: {{player.login}} </p>
          </div>
          <div class="div_for_p">
            <p>Рейтинг: {{player.rating}}</p>
          </div>
          <button :disabled="field.length === 0" style="width:100%" v-if="surrender && !end" @click="surrender_f">Сдаться </button>
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
  import {TTT_SURRENDER_PATH} from "../../service/api/ttt";

  export default {
    name:"ProfileInTttGame",
    props:['player', 'surrender', "player_time", "end", "field","game"],

    data:function(){
      return {
        config: {
          headers: {
            'Content-Type': 'multipart/form-data;application/json',
            "Access-Control-Allow-Origin": "*",
          }
        },
        alreadySurrendered:false,
        reformatTime:reformatTime
      }
    },
    methods:{
      surrender_f(){
        if(!this.alreadySurrendered && !this.game.endTime ){
          this.alreadySurrendered = true;
          axios.post(TTT_SURRENDER_PATH,{game_id:this.game.id,player_id:this.player.id},this.config)
        }else{
          alert("Игра уже завершена")
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

.div_for_p{
  color:darkblue;
  font-size:20px;
  margin-top:40px;
}

.div_for_p:first-child{
  margin-top:0px;
}


</style>