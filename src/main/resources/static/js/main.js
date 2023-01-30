import {createApp} from 'vue'
import App from "./pages/App.vue"
import router from "router/router"
import "bootstrap/dist/css/bootstrap.min.css"
import "bootstrap"



import '../css/main.scss'
import '../css/form.scss'
import '../css/w3.css'

const app = createApp(App)
    .use(router)


app.mount("#app")