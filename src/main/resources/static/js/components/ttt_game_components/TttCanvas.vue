<template>
  <div>
    <canvas  class="ttt-canvas" ref="canvas">
      Ваш браузер не поддерживает canvas :(
    </canvas>
  </div>
</template>

<script>
import {O, X} from "../../service/TttGameHelper";

export default {
  name: "TttCanvas",
  props: ["game", "field", "player", "makeMove"],
  data: function () {
    return {
      canvas: null,
      ctx: null,
      fieldSize: 0,
      squareSize: 0,
      canvasSize: 500,
      white: "rgb(252, 88, 104)",
      whiteChecking: [252, 88, 104],
      black: "rgb(247, 52, 71)",
      blackChecking: [247, 52, 71],
      whiteHover: "rgb(150, 50, 58)",
      whiteHoverChecking: [150, 50, 58],
      blackHover: "rgb(150, 50, 58)",
      blackHoverChecking: [150, 50, 58],
      figureColor: "rgb(200, 200, 255)",
      fieldExists: false
    }
  },
  mounted() {
    this.canvas = this.$refs.canvas
    this.ctx = this.canvas.getContext('2d');
    this.canvas.height = this.canvasSize;
    this.canvas.width = this.canvasSize;

    this.ctx.fillStyle = 'white';
    this.ctx.fillRect(0, 0, this.canvas.width, this.canvas.height)
  },
  created() {
    this.fieldSize = this.field.length
    this.drawFieldWhenCan()
    this.addListeners();
  },
  watch: {
    field(oldField, newField) {
      if (newField && newField.length !== 0) {
        if (!this.fieldExists || !this.fieldsEquals(oldField, newField)) {
          this.fieldSize = newField.length;
          this.drawFieldWhenCan();
          this.fieldExists = true;
        }
      }
    }
  },
  methods: {
    drawFieldWhenCan() {
      const waitContextTimeout = setTimeout(() => {
        if (this.ctx && this.fieldSize !== 0) {
          this.drawField();
          clearTimeout(waitContextTimeout);
        }
      }, 100);
    },
    drawField() {
      this.drawSquares();
      this.drawLines();
      this.drawXO();
    },
    drawSquares() {
      this.squareSize = Math.floor(this.canvasSize / this.fieldSize)
      for (let i = 0; i < this.fieldSize; i++) {
        for (let j = 0; j < this.fieldSize; j++) {
          let color;
          if ((i + j) % 2 === 0) {
            color = this.white
          } else {
            color = this.black
          }
          this.drawSquare(j, i, color)
        }
      }
    },
    drawSquare(field_x, field_y, color) {
      this.ctx.fillStyle = color
      let width = this.squareSize;
      let height = this.squareSize;
      let canvas_x = field_x * this.squareSize;
      let canvas_y = field_y * this.squareSize;

      if (field_y === this.fieldSize - 1) {
        height = this.canvasSize - canvas_y;
      }
      if (field_x === this.fieldSize - 1) {
        width = this.canvasSize - canvas_x;
      }
      this.ctx.fillRect(canvas_x, canvas_y, width, height);
    },
    drawLines() {
      this.ctx.fillStyle = 'black'
      for (let i = 1; i < this.fieldSize; i++) {
        this.ctx.fillRect(i * this.squareSize, 0, 1, this.canvasSize - 1)
        this.ctx.fillRect(0, i * this.squareSize, this.canvasSize - 1, 1)
      }
      this.ctx.beginPath()
      this.ctx.lineWidth = 5
      this.ctx.strokeStyle = 'black'
      this.ctx.moveTo(0, 0)
      this.ctx.lineTo(0, this.canvasSize)
      this.ctx.lineTo(this.canvasSize, this.canvasSize)
      this.ctx.lineTo(this.canvasSize, 0)
      this.ctx.lineTo(0, 0)
      this.ctx.stroke()
    },
    drawXO() {
      for (let y = 0; y < this.fieldSize; y++) {
        for (let x = 0; x < this.fieldSize; x++) {
          if(this.field[y][x] === X){
            this.drawX(x,y);
          }else if(this.field[y][x] === O){
            this.drawO(x,y);
          }
        }
      }
    },
    drawX(fieldX, fieldY) {
      let shift = 10;
      this.ctx.beginPath();
      this.ctx.lineCap = "round";
      this.ctx.lineWidth = 7;
      this.ctx.strokeStyle = this.figureColor
      this.ctx.moveTo(fieldX * this.squareSize + shift, fieldY * this.squareSize + shift)
      this.ctx.lineTo((fieldX + 1) * (this.squareSize) - shift, (fieldY + 1) * (this.squareSize) - shift);
      this.ctx.moveTo((fieldX + 1) * (this.squareSize) - shift, fieldY * this.squareSize + shift)
      this.ctx.lineTo(fieldX * this.squareSize + shift, (fieldY + 1) * (this.squareSize) - shift)
      this.ctx.stroke();
    },
    drawO(fieldX, fieldY) {
      let shift = 10;
      this.ctx.beginPath();
      this.ctx.lineWidth = 7;
      this.ctx.strokeStyle = this.figureColor
      let x = Math.floor((fieldX + 1 / 2) * this.squareSize),
          y = Math.floor((fieldY + 1 / 2) * this.squareSize);
      this.ctx.arc(x, y, Math.floor(this.squareSize / 2) - shift, 0, 2 * Math.PI);
      this.ctx.stroke();
    },
    addListeners() {
      let canvasTimeout = setTimeout(() => {
        if (this.canvas) {
          this.addClickListener();
          this.addMoveListener();

          clearTimeout(canvasTimeout);
        }
      }, 100)
    },
    addClickListener() {
      this.canvas.addEventListener("mouseup", e => {
        let canvas_x = e.pageX - e.target.offsetLeft,
            canvas_y = e.pageY - e.target.offsetTop;
        let field_coords = this.getFieldCoordsByCanvasCoords(canvas_x, canvas_y);
        if (this.player.id === this.game.player1Id && this.game.queue === 1
            || this.player.id === this.game.player2Id && this.game.queue === 2) {
          if(this.field[field_coords.field_y][field_coords.field_x] !== 0){
            alert("Данное поле занято!");
            return;
          }
          this.makeMove(field_coords.field_x, field_coords.field_y);
        }else if(this.game.endTime){
          alert('Игра закончена')
        } else {
          alert("Сейчас не ваш ход")
        }
      })
    },
    addMoveListener() {
      this.canvas.addEventListener("mousemove", e => {
        let canvas_x = e.pageX - e.target.offsetLeft,
            canvas_y = e.pageY - e.target.offsetTop;

        let field_coords = this.getFieldCoordsByCanvasCoords(canvas_x, canvas_y);
        this.changeSquareColor(field_coords.field_x, field_coords.field_y)
      })
    },
    getFieldCoordsByCanvasCoords(canvas_x, canvas_y) {
      let field_x = Math.floor(canvas_x / this.squareSize),
          field_y = Math.floor(canvas_y / this.squareSize);
      return {'field_x': field_x, field_y: field_y};
    },
    changeSquareColor(x, y) {
      const shiftForCheck = 6;
      const canvas_x = x * this.squareSize + shiftForCheck;
      const canvas_y = y * this.squareSize + shiftForCheck;
      let pixel;
      try {
         pixel = this.ctx.getImageData(canvas_x, canvas_y, 1, 1).data;
      }catch (e){
        return;
      }

      if (this.colorEquals(pixel, this.whiteHoverChecking) || this.colorEquals(pixel, this.blackHoverChecking)) {
        return;
      }
      let newColor = ""
      if (this.colorEquals(pixel, this.whiteChecking)) {
        newColor = this.whiteHover;
      } else {
        newColor = this.blackHover;
      }
      this.drawSquares();
      this.drawSquare(x, y, newColor);
      this.drawLines();
      this.drawXO();
    },
    colorEquals(color1, color2) {
      return color1[0] === color2[0] &&
          color1[1] === color2[1] &&
          color1[2] === color2[2];
    },
    fieldsEquals(field1, field2) {
      if (field1 && field2 && field1.length !== field2.length) {
        return false;
      }
      for (let y = 0; y < field1.length; y++) {
        for (let x = 0; x < field1[y].length; x++) {
          if (field1[y][x] !== field2[y][x]) {
            return false;
          }
        }
      }
      return true;
    }
  }
}
</script>

<style>

.ttt-canvas {
  display: block;
  margin: auto;
}

</style>