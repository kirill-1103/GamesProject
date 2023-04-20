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
import {connectToChats} from "../service/ws";

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
    //TODO: get count unread messages and set it
  },
  created() {
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
            }
        ).then(()=>{connectToChats(this.player.id,this.addNewMessageInState)})
      }else{
        this.player = this.$store.state.player;
        connectToChats(this.player.id,this.addNewMessageInState)
      }
    },
    addNewMessageInState(message){
      this.$store.commit("addNewMessage",message);
    }
  }
}

</script>

<style>
</style>