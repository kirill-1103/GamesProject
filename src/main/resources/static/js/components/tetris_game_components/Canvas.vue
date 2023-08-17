<template>
  <div>
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
  name: "Canvas",
  props: ["field", "isActive", "isShowing", "makeMove"],
  data: function () {
    return {
      canvas: null,
      ctx: null,
      canvasWidth: 300,
      canvasHeight: 600,
      colors: {
        mainColor: 'pink',
        busyColor: 'yellow',
        activeColor: 'green',
        stroke: 'black'
      },
      lastMoveTime: new Date(),
      canRotate: true
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
    this.addListeners();
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
      for (let y = TETRIS_TOP_OFFSET; y < this.field.length-TETRIS_BOTTOM_OFFSET; y++) {
        for (let x = TETRIS_LEFT_OFFSET; x < this.field[0].length - TETRIS_RIGHT_OFFSET; x++) {
          this.drawRect(x - TETRIS_LEFT_OFFSET, y - TETRIS_TOP_OFFSET, this.field[y][x]);
        }
      }
    },
    drawRect(x, y, cellCode) {
      const width = this.canvasWidth / (this.field[0].length - TETRIS_WIDTH_OFFSET);
      const height = this.canvasHeight / (this.field.length - TETRIS_HEIGHT_OFFSET);
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
    },
    addListeners() {
      let canvasInterval = setInterval(() => {
        if (this.canvas) {
          if (!this.isShowing) {
            this.addKeyboardListener();
          }
          clearInterval(canvasInterval)
        }
      }, 50);
    },
    addKeyboardListener() {
      document.addEventListener("keydown", (e) => {
        let time = new Date();
        if (time - this.lastMoveTime > 100) {
          if (e.code === 'ArrowLeft') {
            this.makeMove(TETRIS_MOVE_LEFT)
          } else if (e.code === 'ArrowRight') {
            this.makeMove(TETRIS_MOVE_RIGHT)
          } else if (e.code === 'ArrowDown') {
            this.makeMove(TETRIS_MOVE_DOWN)
          } else if (e.code === 'ArrowUp') {
            if (this.canRotate) {
              this.makeMove(TETRIS_MOVE_ROTATE)
              this.canRotate = false;
            }
          }
          this.lastMoveTime = time;
        }
      })
      document.addEventListener("keyup", (e) => {
        if (e.code === 'ArrowUp') {
          this.canRotate = true;
        }
      })
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