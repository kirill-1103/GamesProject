<template>
  <div style="text-align: center">
    <h2 >Крестики-Нолики</h2>
  </div>


  <canvas class="ttt-canvas" id="canvas">
    Ваш браузер не поддерживает canvas.
  </canvas>
</template>

<script>
export default{
  name:"TttGamePage",
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

    let x = 0;
    this.ctx.fillStyle = 'red';
    let b = 0;
    let move=1;
    this.ctx.fillRect(x,50,100,100);
    setInterval(()=>{
      this.ctx.fillStyle = 'white';
      this.ctx.fillRect(0,0,this.canvas.width,this.canvas.height);

      if(b===0){
        this.ctx.fillStyle = 'yellow'
        b=1;
      }else{
        this.ctx.fillStyle = 'pink';
        b=0;
      }
      if(move===1){
        if(x+100===this.canvas.width){
          move=-1;
        }
      }else{
        if(x===0){
          move=1;
        }
      }
      x+=move;
      this.ctx.fillRect(x,50,100,100);

    },100)
  }
}
</script>

<style>
.ttt-canvas{
  display: block;
  margin: auto;
}
</style>