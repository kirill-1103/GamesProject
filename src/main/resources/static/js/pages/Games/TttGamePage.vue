<template>
  <div style="text-align: center">
    <h2 >Крестики-Нолики</h2>
  </div>
  <StartGameComponent v-if="player.lastGameCode"/>
  <div class="container" v-else>
    <div class="row">
      <div class="col">
        <ProfileInTttGame :surrender="true" :player="player"></ProfileInTttGame>
      </div>

      <canvas v-if="!chat_b" class="ttt-canvas col" ref="canvas">
        Ваш браузер не поддерживает canvas.
      </canvas>

      <div class="div-chat col" v-else>
        <GameChatComponent></GameChatComponent>
      </div>

      <div class="col">
        <ProfileInTttGame :surrender="false" :player="player_2"></ProfileInTttGame>
      </div>
    </div>
  </div>
  <div class="buttons">
    <button @click="openChat">Чат</button>
    <button @click="openGame">Игра</button>
  </div>
</template>

<script>
import StartGameComponent from "../../components/StartGameComponent.vue";
import ProfileInTttGame from "../../components/ProfileInTttGame.vue";
import updateAuthUserInStorage from "../../service/auth.js";
import GameChatComponent from "../../components/GameChatComponent.vue";

export default{
  name:"TttGamePage",
  components:{StartGameComponent,ProfileInTttGame,GameChatComponent},
  data: function(){
    return{
      ctx:null,
      canvas:null,
      width:500,
      height:500,
      player:{},
      player_2:{},
      chat_b:true
    }
  },
  created() {
    if(this.$store.state.player){
      this.player = this.$store.state.player
      this.player.img_data = this.$store.state.playerPhoto;
    }else{
      updateAuthUserInStorage(this.$store).then(()=>{
        this.player = this.$store.state.player;
        console.log(this.player)
      }).then(()=>{
        this.player.img_data = this.$store.state.playerPhoto;
      })
    }
    //TODO: Сделать запрос на профиль противника
    this.player_2.login="Компьютер"
    this.player_2.rating =  "Без рейтинга"
  },
  mounted(){
    if(!this.chat_b){
      this.setCanvasSettings();
    }
    // let x = 0;
    // let y = 50;
    // this.ctx.fillStyle = 'red';
    // let b = 0;
    // let move_x=1;
    // let move_y=0;
    // this.ctx.fillRect(x,50,100,100);
    // setInterval(()=>{
    //   this.ctx.fillStyle = 'white';
    //   this.ctx.fillRect(0,0,this.canvas.width,this.canvas.height);
    //
    //     this.ctx.fillStyle = 'red';
    //     b=0;
    //
    //   move_x = getRandomInt(30);
    //   move_y = getRandomInt(30);
    //   if(move_x>=0){
    //     if(x+100+move_x>=this.canvas.width){
    //       move_x=this.canvas.width-x-100
    //     }
    //   }else{
    //     if(x+move_x<0){
    //       move_x= -x;
    //     }
    //   }
    //   if(move_y>=0){
    //     if(y+100+move_y>=this.canvas.height){
    //       move_y = this.canvas.height-y-100;
    //     }
    //   }else{
    //     if(y+move_y<0){
    //       move_y=-y;
    //     }
    //   }
    //
    //   x+=move_x
    //   y+=move_y
    //   this.ctx.fillRect(x,y,100,100);
    //
    // },50)
  },
  methods:{
    openChat(){
      this.chat_b=true;
    },
    openGame(){
      this.chat_b=false;
      if(!this.canvas){
        this.setCanvasSettings();
      }
    },
    setCanvasSettings(){
      this.canvas = this.$refs.canvas
      this.ctx = this.canvas.getContext('2d');
      this.canvas.height = 500;
      this.canvas.width = 500;

      this.ctx.fillStyle = 'white';
      this.ctx.fillRect(0,0,this.canvas.width,this.canvas.height)
    }
  }
}

// function getRandomInt(max) {
//  let coef=1;
//   if(Math.random()>0.5){
//     coef=-1;
//   }
//   return coef* Math.floor(Math.random() * max);
// }
</script>

<style>
.ttt-canvas{
  display: block;
  margin: auto;
}

.div_chat{
  width: 500px;
  height: 500px;
}

.buttons{
  width:500px;
  margin:auto;
}
.buttons button{
  width:48%;
  margin-top:10px
}

.buttons button:first-child{
  margin-right:4%;
}
</style>