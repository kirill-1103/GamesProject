<template>
  <div v-if="!game.field2" style="text-align: center">
    <Canvas :field="game.field1" :isActive="true" :isShowing="false"
      :makeMove="makeMove"/>
  </div>
  <div v-else>

  </div>
</template>

<script>
import Canvas from "./Canvas.vue";
export default {
  name: "GameComponent",
  components: {Canvas},
  props: ["player", "entity", "game"],
  data: function(){
    return{

    }
  },
  methods:{
    makeMove(moveCode){
      console.log(moveCode)
    }
  }
}
</script>

<style>

</style>