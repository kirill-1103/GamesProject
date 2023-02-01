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
import updateAuthUserInLocalStorage from "../service/auth.js";

export default {
  components: {
    Nav
  },
  data:function (){
    return{
      player:null
    }
  },
  mounted() {
    updateAuthUserInLocalStorage().then(()=> {
          if (localStorage.getItem('player')) {
            this.player = localStorage.getItem('player');
          } else {
            this.player = null;
          }
          console.log(this.player)
        }
    )
  },
  created() {
    updateAuthUserInLocalStorage().then(()=> {
          if (localStorage.getItem('player')) {
            this.player = localStorage.getItem('player');
          } else {
            this.player = null;
          }
          console.log(this.player)
        }
    )
  }
}

</script>

<style>
</style>