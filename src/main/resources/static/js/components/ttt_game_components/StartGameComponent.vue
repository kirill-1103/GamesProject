<template>
  <div style="text-align: center">
    <div style="text-align: center">
      <img class="game-img" src="../../../img/ttt-logo.jpg" alt="ttt-game-img" >
    </div>


    <div class="input-div">
      <div class="dropdown">
        <button  id="time_button" class="settings-input dropdown-toggle" data-bs-toggle="dropdown" >
          Время игрока: {{time_string}}
        </button>

        <ul class="dropdown-menu">
          <li><button class="dropdown-item" @click="setTime(1, '1 минута')">1 минута</button></li>
          <li><button class="dropdown-item" @click="setTime(5,'5 минут')">5 минут</button></li>
          <li><button class="dropdown-item" @click="setTime(-1,'Без времени')">Без времени</button></li>
        </ul>
      </div>

      <div class="dropdown">
        <button  id="size_button" class="settings-input dropdown-toggle" data-bs-toggle="dropdown" >
         Размер поля: {{field_size_text}}
        </button>

        <ul class="dropdown-menu">
          <li><button class="dropdown-item" @click="setSize(3, '3x3')">3x3</button></li>
          <li><button class="dropdown-item" @click="setSize(5,'5x5')">5x5</button></li>
          <li><button class="dropdown-item" @click="setSize(7,'7x7')">7x7</button></li>
        </ul>
      </div>

      <button class="settings-input">Игра онлайн</button>

      <button class="settings-input">Игра с компьютером</button>
    </div>
  </div>
</template>

<script>
export default {
  name: "StartGameComponent",
  data: function(){
    return{
      time_string:"5 минут",
      field_size_text:"3x3",
      settings:{
        time:5,
        field_size:3,
      }
    }
  },
  methods:{
    setTime(time,time_str){
      this.settings.time=time;
      this.time_string = time_str;
      document.getElementById("time_button").innerText = 'Время игрока: ' + this.time_string;
    },
    setSize(size,size_str){
      this.settings.field_size = size;
      this.field_size_text = size_str;
      document.getElementById("size_button").innerText = 'Размер поля: '+this.field_size_text;
    }
  }
}
</script>

<style>
.game-img{
  width:200px;
  height:200px;
  border-radius: 10px;
  box-shadow: 4px 4px 8px black;
}

.input-div{
  min-width: 320px;
  width:50%;
  margin:20px auto;
}

.settings-input{
  width:80%;
  margin-top:20px;
}
</style>
