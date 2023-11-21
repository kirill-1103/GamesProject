<template>
  <div id="app" class="main-div">
    <Nav :player="player"></Nav>

    <router-view></router-view>
    <button @click="testfunc">test</button>
  </div>
</template>

<script>
import Nav from 'components/Nav.vue'
import axios from 'axios'
import updateAuthUserInStorage from '../service/auth.js'
import {connectToChats} from '../service/ws'
import {tokenTime, updateToken} from "../service/jwtUtils";
import {playerApi} from "../service/openapi/config/player_openapi_config";

export default {
  components: {
    Nav,
  },
  data: function () {
    return {
      player: null,
    }
  },
  mounted() {
    this.updateAuthUser()
    //TODO: get count unread messages and set it
  },
  created() {
    if (localStorage['jwtToken'] && localStorage['jwtToken'] != '') {
      axios.defaults.headers.common["Authorization"] = 'Bearer ' + localStorage['jwtToken']
      this.updateTokenInterval();
      this.updateActiveInterval();
    }
  },
  methods: {
    updateAuthUser() {
      if (!this.$store.state.player) {
        updateAuthUserInStorage(this.$store).then(()=>{
          if (this.$store.state.player) {
            this.player = this.$store.state.player
            this.handleChatPage();
            connectToChats(this.player.id, this.addNewMessageInState)
          } else {
            this.player = null
          }
        })

      } else {
        this.player = this.$store.state.player
        connectToChats(this.player.id, this.addNewMessageInState)
      }
    },
    addNewMessageInState(message) {
      this.$store.commit('addNewMessage', message)
    },
    testfunc() {
      playerApi.getById(2,(error,data,response)=>{
        if(error){
          console.log('error')
          console.log(error)
        }else{
          console.log('data')
          console.log(data)
        }
      })
    },
    handleChatPage() {
      const path = this.$router.currentRoute._value.fullPath;
      if (path.startsWith("/chat/") && path !== '/chat/' + this.$store.state.player.id) {
        this.$router.replace({path: "/chat/" + this.$store.state.player.id});
      }
    },
    updateToken() {
      const tokenT = tokenTime(localStorage["jwtToken"]);
      if (tokenT == null || tokenT < 1000 * 60 * 10) {
        updateToken()
        console.log(localStorage["jwtToken"])
      }
    },
    updateTokenInterval() {
      this.updateToken()
      setInterval(() => {
        this.updateToken();
      }, 1000 * 60);
    },
    updateActiveInterval() {
      this.updateActive();
      setInterval(() => {
        this.updateActive();
      }, 1000 * 10)
    },
    updateActive() {
      playerApi.updateActive((error,data,response)=>{})
    }
  },
}
</script>

<style></style>
