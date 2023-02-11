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
          <button style="width:100%" v-if="surrender">Сдаться </button>
        </div>

        <div style="margin-top:50%;" v-else class="spinner-border text-primary" role="status">
          <span class="visually-hidden">Loading..</span>
        </div>
      </div>
    </div>
</template>

<script>
  export default {
    name:"ProfileInTttGame",
    props:['player', 'surrender', "player_time"],
    data:function(){
      return {
      }
    },
    methods:{
      reformatTime(time){
        let minutes = Math.floor(time/60/10);
        let seconds = Math.floor((time - minutes*60*10)/10)
        let min_str = minutes > 9 ?  String(minutes) : "0"+String(minutes);
        let sec_str = seconds > 9 ?  String(seconds) : "0"+String(seconds);
        return min_str + ":" + sec_str;
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