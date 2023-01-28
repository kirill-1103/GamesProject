import {createRouter, createWebHistory} from 'vue-router'
import RegisterPage from '../pages/RegisterPage.vue'
import LoginPage from '../pages/LoginPage.vue'

import TempPage from '../pages/TempPage.vue'

import {
    REGISTER_PAGE_NAME, REGISTER_PAGE_PATH,
    LOGIN_PAGE_NAME, LOGIN_PAGE_PATH
} from "./component_names";

const routes = [
    {path: REGISTER_PAGE_PATH, name: REGISTER_PAGE_NAME, component: RegisterPage},
    {path: LOGIN_PAGE_PATH, name: LOGIN_PAGE_NAME, component: LoginPage},
    {path: "/temp", name: "TempPage", component: TempPage},
]

const router = createRouter({
    history: createWebHistory(),
    routes
})

export default router