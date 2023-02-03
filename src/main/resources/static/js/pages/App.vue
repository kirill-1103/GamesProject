<template>
  <div id="app" class="main-div">
    <Nav :player="player"></Nav>

    <router-view>
    </router-view>
  </div>
</template>

<script>
import Nav from "components/Nav.vue"
import axios from "axios";
import updateAuthUserInStorage from "../service/auth.js";

export default {
  components: {
    Nav
  },
  data: function () {
    return {
      player: null
    }
  },
  mounted() {
    this.updateAuthUser();
  },
  created() {
    this.updateAuthUser();
  },
  methods:{
    updateAuthUser(){
      if(!this.$store.state.player){
        updateAuthUserInStorage(this.$store).then(() => {
              if (this.$store.state.player) {
                this.player = this.$store.state.player;
              } else {
                this.player = null;
              }
              // console.log(this.player)
            }
        )
      }else{
        this.player = this.$store.state.player;
      }
    }
  }
}

</script>

<style>
</style>