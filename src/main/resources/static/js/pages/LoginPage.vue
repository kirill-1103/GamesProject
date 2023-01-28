<template>
<!--  <form @submit.prevent="submitForm">-->
  <form action="/login" method="POST">
    <label for="login">Username:</label>
    <input @change = changeLogin() type="text" id="login" name="login" v-model="form.login">
    <br>
    <label for="password">Password:</label>
    <input type="password" id="password" name="password" v-model="form.password">

    <button type="submit">Submit</button>
    <label v-text="errorMessage"></label>
    
    <button @click="goToSignUp">Sign up</button>
  </form>
</template>

<script>
import axios from 'axios'
import {REGISTER_PAGE_NAME,LOGIN_PAGE_NAME} from "../router/component_names";

export default {
  name: "LoginPage",
  data: function () {
    return {
      form: {
        login: '',
        password: ''
      },
      errorMessage:''
    }
  },
  created() {
    if(localStorage.getItem("login") != null && localStorage.getItem("login")!=="undefined"){
      this.form.login = localStorage.getItem("login")
    }
    if(this.$route.fullPath.includes("error")){
      this.errorMessage = "Неверное имя пользователя или пароль."
      this.$router.replace({name: LOGIN_PAGE_NAME});
    }
  },

  methods: {
    submitForm() {
      axios.post('/login', this.form).then(({data}) => {
        console.log(data);
      }).catch((error) => {
        this.errorMessage = error.message
        console.log(this.errorMessage);
      })
    },

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