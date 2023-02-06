<template>
  <div>
    <canvas class="ttt-canvas" ref="canvas">
      Ваш браузер не поддерживает canvas.
    </canvas>
  </div>
</template>

<script>
export default {
  name:"TttCanvas",
  data: function(){
    return{
      canvas:null,
      ctx:null
    }
  },
  mounted(){
    console.log('canvas')
    this.canvas = this.$refs.canvas
    this.ctx = this.canvas.getContext('2d');
    this.canvas.height = 500;
    this.canvas.width = 500;

    this.ctx.fillStyle = 'white';
    this.ctx.fillRect(0,0,this.canvas.width,this.canvas.height)
  },
}
</script>

<style>

.ttt-canvas{
  display: block;
  margin: auto;
}

</style>