<template>
<!--  <form @submit.prevent="submitForm">-->
  <Form
  :form="form"
  :error-message='errorMessage'
  ></Form>
</template>

<script>
import axios from 'axios'
import {REGISTER_PAGE_NAME,LOGIN_PAGE_NAME} from "../router/component_names";
import Form from "../components/LoginForm.vue";

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
      axios.post('/login', this.form).then(({data}) => {
        console.log(data);
      }).catch((error) => {
        this.errorMessage = error.message
        console.log(this.errorMessage);
      })
    }
  }
}
</script>

<style>

</style>