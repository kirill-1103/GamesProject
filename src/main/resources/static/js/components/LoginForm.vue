<template>
  <form class="form" action="/login" method="POST">
    <label for="login">Логин:</label>
    <input @change = changeLogin() type="text" id="login" name="login" v-model="form.login">
    <br>
    <label for="password">Пароль:</label>
    <input type="password" id="password" name="password" v-model="form.password">

    <button  type="submit">Войти</button>

    <button @click="goToSignUp">Регистрация</button>

    <label class="error" v-text="errorMessage"></label>

  </form>
</template>

<script>
import {REGISTER_PAGE_NAME} from "../router/component_names";

export default {
  name: 'LoginForm',

  props:['form','errorMessage',"testCounter"],

  methods:{
    changeLogin(){
      localStorage.setItem("login",this.form.login)
    },
    goToSignUp(){
      this.$router.push({name: REGISTER_PAGE_NAME});
    }
  }
}

</script>

<style>

</style>