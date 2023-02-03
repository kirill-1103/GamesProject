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

const store = createStore({
    state() {
        return {
            player: null,
            playerPhoto: null
        }
    },
    mutations: {
        setPlayer(state, player) {
            state.player = player;
        },
        setPlayerPhoto(state, photo) {
            state.playerPhoto = photo;
        }
    }
})

const app = createApp(App)
    .use(router)
    .use(store)


app.mount('#app')