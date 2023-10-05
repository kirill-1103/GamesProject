import {createApp} from 'vue'
import App from "./pages/App.vue"
import router from "router/router"
import {createStore} from 'vuex'

import "bootstrap/dist/css/bootstrap.min.css"
import "bootstrap"


import '../css/main.scss'
import '../css/form.scss'
import '../css/w3.css'
import '../css/table.scss'
import '../css/for_chat.scss'
import axios from "axios";
import {tokenIsExpired} from "./service/jwtUtils";
import {CHAT_PAGE_PATH, LOGIN_PAGE_PATH, paths, PROFILE_PAGE_PATH, REGISTER_PAGE_PATH} from "./router/component_names";

const store = createStore({
    state() {
        return {
            player: null,
            playerPhoto: null,
            playerGameCode: null,
            playerGameId: null,
            inSearch: false,
            newMessages: []
        }
    },
    mutations: {
        setPlayer(state, player) {
            state.player = player;
        },
        setPlayerPhoto(state, photo) {
            state.playerPhoto = photo;
        },
        setPlayerGameCode(state, code){
            state.playerGameCode = code;
        },
        setPlayerGameId(state,id){
            state.playerGameId = id;
        },
        setInSearch(state, inSearch){
            state.inSearch = inSearch;
        },
        addNewMessage(state, message){
            state.newMessages.push(message)
        },
        clearMessages(state){
            state.newMessages = [];
        }
    }
})

router.beforeEach((to,from,next)=>{
    // if(!paths.includes(to.fullPath)){
    //     next(PROFILE_PAGE_PATH);
    //     return;
    // }
    if(localStorage['jwtToken'] && localStorage['jwtToken'] != ''){
        if(to.fullPath == REGISTER_PAGE_PATH || to.fullPath  == LOGIN_PAGE_PATH){
            next(PROFILE_PAGE_PATH)
            return;
        }
        let token = localStorage['jwtToken'];
        if(tokenIsExpired(token)){
            localStorage['jwtToken']='';
            next(LOGIN_PAGE_PATH)
            return;
        }
        axios.defaults.headers.common['Authorization'] = 'Bearer '+localStorage['jwtToken']
    }else{
        if(to.fullPath  != REGISTER_PAGE_PATH && to.fullPath !=LOGIN_PAGE_PATH){
            console.log(to.fullPath)
            next(LOGIN_PAGE_PATH);
            return;
        }
    }
    next()
})


const app = createApp(App)
    .use(router)
    .use(store)



app.mount('#app')