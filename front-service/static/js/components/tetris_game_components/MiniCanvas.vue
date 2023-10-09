<template>
  <div style="background-color:#d3ddf5; padding: 4px;">
    <p style="font-family: cursive; font-size: 15px;">Следующая фигура:</p>
    <canvas class="tetris-canvas" ref="canvas">
      Ваш браузер не поддерживает canvas :(
    </canvas>
  </div>
</template>

<script>
import {
  TETRIS_ACTIVE_CELL, TETRIS_BOTTOM_OFFSET,
  TETRIS_BUSY_CELL,
  TETRIS_EMPTY_CELL,
  TETRIS_HEIGHT_OFFSET,
  TETRIS_LEFT_OFFSET,
  TETRIS_MOVE_DOWN,
  TETRIS_MOVE_LEFT,
  TETRIS_MOVE_RIGHT,
  TETRIS_MOVE_ROTATE,
  TETRIS_RIGHT_OFFSET, TETRIS_TOP_OFFSET,
  TETRIS_WIDTH_OFFSET
} from "../../service/GameHelper";

export default {
  name: "MiniCanvas",
  props: ["field", "isActive", "isShowing"],
  data: function () {
    return {
      canvas: null,
      ctx: null,
      canvasWidth: 70,
      canvasHeight: 70,
      colors: {
        mainColor: 'pink',
        busyColor: 'yellow',
        activeColor: 'green',
        stroke: 'black'
      },
    }
  },
  mounted() {
    this.canvas = this.$refs.canvas;
    this.ctx = this.canvas.getContext('2d');
    this.canvas.height = this.canvasHeight;
    this.canvas.width = this.canvasWidth;
  },
  created() {
    this.drawFieldWhenCan();
  },
  methods: {
    drawFieldWhenCan() {
      const waitContextInterval = setInterval(() => {
        if (this.ctx && this.field) {
          this.drawField()
          clearInterval(waitContextInterval)
        }
      }, 50)
    },
    drawField() {
      // console.log("DRAW_FIELD:");
      // console.log(this.field)
      this.ctx.fillStyle = this.colors.mainColor;
      this.ctx.fillRect(0, 0, this.canvas.width, this.canvas.height);
      for (let y = 0; y < this.field.length; y++) {
        for (let x = 0; x < this.field[0].length ; x++) {
          this.drawRect(x , y , this.field[y][x]);
        }
      }
    },
    drawRect(x, y, cellCode) {
      const width = this.canvasWidth / (this.field[0].length );
      const height = this.canvasHeight / (this.field.length );
      switch (cellCode) {
        case TETRIS_EMPTY_CELL:
          this.ctx.fillStyle = this.colors.mainColor;
          break;
        case TETRIS_ACTIVE_CELL:
          this.ctx.fillStyle = this.colors.activeColor;
          break;
        case TETRIS_BUSY_CELL:
          this.ctx.fillStyle = this.colors.busyColor;
          break;
      }
      this.ctx.fillRect(x * width, y * height, width, height)
      this.ctx.strokeStyle = this.colors.stroke;
      this.ctx.strokeRect(x * width, y * height, width, height)
    }


  },
  watch:{
    field(newV,oldV){
      // console.log("newv")
      this.drawFieldWhenCan()
    }
  }
}
</script>

<style>
.tetris-canvas {
}
</style>