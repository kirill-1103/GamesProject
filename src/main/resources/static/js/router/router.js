import {createRouter, createWebHistory} from 'vue-router'
import RegisterPage from '../pages/RegisterPage.vue'
import LoginPage from '../pages/LoginPage.vue'
import ProfilePage from "../pages/ProfilePage.vue";
import GameListPage from "../pages/GameListPage.vue";
import RatingPage from "../pages/RatingPage.vue";
import PlayersListPage from "../pages/PlayersListPage.vue";
import ChatPage from "../pages/ChatPage.vue";
import TttGamePage from "../pages/Games/TttGamePage.vue";


import {
    REGISTER_PAGE_NAME, REGISTER_PAGE_PATH,
    LOGIN_PAGE_NAME, LOGIN_PAGE_PATH,
    PROFILE_PAGE_NAME, PROFILE_PAGE_PATH,
    GAME_LIST_PAGE_NAME, GAME_LIST_PAGE_PATH,
    RATING_PAGE_NAME, RATING_PAGE_PATH,
    PLAYERS_LIST_PAGE_NAME, PLAYERS_LIST_PAGE_PATH,
    CHAT_PAGE_NAME, CHAT_PAGE_PATH,
    TTT_GAME_PAGE_NAME,TTT_GAME_PAGE_PATH
} from "./component_names";

const routes = [
    {path: REGISTER_PAGE_PATH, name: REGISTER_PAGE_NAME, component: RegisterPage},
    {path: LOGIN_PAGE_PATH, name: LOGIN_PAGE_NAME, component: LoginPage},
    {path: PROFILE_PAGE_PATH, name: PROFILE_PAGE_NAME, component: ProfilePage},
    {path: GAME_LIST_PAGE_PATH, name: GAME_LIST_PAGE_NAME, component: GameListPage},
    {path: PLAYERS_LIST_PAGE_PATH, name: PLAYERS_LIST_PAGE_NAME, component: PlayersListPage},
    {path: RATING_PAGE_PATH, name: RATING_PAGE_NAME, component: RatingPage},
    {path: CHAT_PAGE_PATH, name: CHAT_PAGE_NAME, component: ChatPage},
    {path: TTT_GAME_PAGE_PATH, name: TTT_GAME_PAGE_NAME, component: TttGamePage},
]

const router = createRouter({
    history: createWebHistory(),
    routes
})

function routesContains(path){
    for(r of routes){
        if(r.path === path){
            return true;
        }
    }
    return false;
}



export default router