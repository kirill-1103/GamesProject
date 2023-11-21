<template>
<!--  <form @submit.prevent="submitForm">-->
  <Form
  :form="form"
  :error-message='errorMessage'
  :submit-form="submitForm"
  ></Form>
</template>

<script>
import axios from 'axios'
import {REGISTER_PAGE_NAME, LOGIN_PAGE_NAME, PROFILE_PAGE_PATH} from "../router/component_names";
import Form from "../components/LoginForm.vue";
import router from "../router/router";
import {AUTHORIZATION_PATH} from "../service/api/auth";
import {playerApi} from "../service/openapi/config/player_openapi_config";

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
  components:{
    Form
  },
  created() {
    let interval = setInterval(()=>{
      if(this.$store.state.player){
        this.$router.push("/me")
        clearInterval(interval)
      }
    },100)
    if(localStorage.getItem("login") != null && localStorage.getItem("login")!=="undefined"){
      this.form.login = localStorage.getItem("login")
    }
    if(this.$route.fullPath.includes("error")){
      this.errorMessage = "Неверный логин или пароль!"
      this.$router.replace({name: LOGIN_PAGE_NAME});
    }
  },

  methods: {
    submitForm() {
      axios.post(AUTHORIZATION_PATH, this.form).then(({data}) => {
        if(data.error){
          this.errorMessage = data.message;
          console.log(this.errorMessage);
          return;
        }
        localStorage.setItem("jwtToken",data);
        router.push(PROFILE_PAGE_PATH);
        location.reload();
        // console.log(this.$store.state.jwtToken)
      })
    }
  }
}
</script>

<style>

</style>