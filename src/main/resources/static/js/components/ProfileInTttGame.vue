<template>
    <div class="card profile-card">
      <div class="card-body profile-card" >
        <div v-if="player.login">
          <div class="div_for_p">
            <p>Время: 1:03</p>
          </div>

          <div v-if="!player.photo">
            <img  src="../../img/default.png" alt="img" class="rounded-circle" width="100" >
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
    props:['player', 'surrender'],
    data:function(){
      return {
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