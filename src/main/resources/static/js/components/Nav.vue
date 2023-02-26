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
              <a class="nav-link" href="/chat">
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
import {LOGIN_PAGE_NAME, TTT_GAME_PAGE_NAME} from "../router/component_names.js";
import updateAuthUserInStorage from "../service/auth.js";
import {GAME_CODE} from "../service/TttGameHelper";

export default {
  name:"Nav",
  props:['player'],
  mounted() {
    setInterval(()=>{
      if(this.$store.state.playerGameId && this.$store.state.playerGameId!==-1){
        this.$refs.currentGame.style.color = 'white'
      }else{
        this.$refs.currentGame.style.color = 'gray'
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
      axios.post("/logout").then(()=>{
        updateAuthUserInStorage(this.$store);
        this.$router.push({name:LOGIN_PAGE_NAME});
      });
    },
    goToCurrentGame(){
      if(this.$store.state.playerGameCode === GAME_CODE
          && this.$store.state.playerGameId !== -1 && this.$store.state.playerGameId){
        this.$router.push({name:TTT_GAME_PAGE_NAME});
      }
    }
  }
}
</script>