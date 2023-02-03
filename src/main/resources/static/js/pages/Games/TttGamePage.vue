<template>
  <div style="text-align: center">
    <h2 >Крестики-Нолики</h2>
  </div>


  <canvas class="ttt-canvas" id="canvas">
    Ваш браузер не поддерживает canvas.
  </canvas>
</template>

<script>
import StartGameComponent from "../../components/StartGameComponent.vue";

export default{
  name:"TttGamePage",
  components:{StartGameComponent},
  data: function(){
    return{
      ctx:null,
      canvas:null,
      width:500,
      height:500
    }
  },
  mounted(){
    this.canvas = document.getElementById('canvas');
    this.ctx = canvas.getContext('2d');
    this.canvas.height = 500;
    this.canvas.width = 500;

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
  }
}

function getRandomInt(max) {
 let coef=1;
  if(Math.random()>0.5){
    coef=-1;
  }
  return coef* Math.floor(Math.random() * max);
}
</script>

<style>
.ttt-canvas{
  display: block;
  margin: auto;
}
</style>