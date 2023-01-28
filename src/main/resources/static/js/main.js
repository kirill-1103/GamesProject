import {createApp} from 'vue'
import App from "./pages/App.vue"
import router from "router/router"

const app = createApp(App).use(router)

app.mount("#app")