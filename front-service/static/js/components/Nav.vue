<template>
  <div class="head">
    <nav class="navbar navbar-expand-lg head" >
      <div class="container-fluid">
          <span class="navbar-brand" href="#">
            <img src="../../img/logo1.png" alt="logo" width="34" height="28" class="d-inline-block align-text-top">
            Games
          </span>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
          <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarSupportedContent">
          <ul class="navbar-nav me-auto mb-2 mb-lg-0">
            <li class="nav-item">
              <a class="nav-link" href="/me">Профиль</a>
            </li>
            <li class="nav-item">
              <a class="nav-link" href="/game_list">Список игр</a>
            </li>

            <li class="nav-item">
              <a class="nav-link" href="/rating">Рейтинг</a>
            </li>

            <li class="nav-item">
              <a class="nav-link" href="/player_list">
                Игроки
              </a>
            </li>

            <li class="nav-item">
              <a class="nav-link" v-bind:href="chatUrl">
                Чат
              </a>
            </li>

            <li class="nav-item">
              <a ref="currentGame" class="nav-link " @click="goToCurrentGame" href="#">Текущая игра</a>
            </li>
          </ul>

<!--          <a class="nav-link" href="/logout">-->
<!--            Выход-->
<!--          </a>-->
          <div v-if="player">
            <button @click="exit" >
              Выход
            </button>
          </div>
        </div>
      </div>
    </nav>
  </div>
</template>

<script>
import axios from "axios";
import {
  LOGIN_PAGE_NAME,
  LOGIN_PAGE_PATH,
  TETRIS_GAME_PAGE_NAME,
  TTT_GAME_PAGE_NAME
} from "../router/component_names.js";
import updateAuthUserInStorage from "../service/auth.js";
import {TETRIS_GAME_CODE, TTT_GAME_CODE} from "../service/GameHelper";
import router from "../router/router";

export default {
  name:"Nav",
  props:['player'],
  data:function(){
    return {
      chatUrl :'/chat/'
    }
  },
  mounted() {
    setInterval(()=>{
      if(this.$store.state.playerGameId && this.$store.state.playerGameId!==-1){
        this.$refs.currentGame.style.color = 'white'
      }else{
        this.$refs.currentGame.style.color = 'gray'
      }
      if(this.player){
        this.chatUrl = '/chat/'+this.player.id
      }
    },100)
  },
  methods:{
    exit(){
      if(!this.$store.state.player){
        alert("Вы еще не авторизованы!");
        return;
      }
      console.log('exit')
      // axios.post("/logout").then(()=>{
      //   updateAuthUserInStorage(this.$store);
      //   this.$router.push({name:LOGIN_PAGE_NAME});
      // });
      localStorage.removeItem("jwtToken")
      router.push(LOGIN_PAGE_PATH);
      location.reload()
    },
    goToCurrentGame(){
      if(this.$store.state.playerGameId !== -1 && this.$store.state.playerGameId){
        if(this.$store.state.playerGameCode === TTT_GAME_CODE){
          this.$router.push({name:TTT_GAME_PAGE_NAME});
        }else if(this.$store.state.playerGameCode === TETRIS_GAME_CODE){
          this.$router.push({name:TETRIS_GAME_PAGE_NAME});
        }
      }
    }
  }
}
</script>