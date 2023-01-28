<template>
  <form @submit.prevent="submitForm">
    <label for="login">Login:</label>
    <input type="text" id="login" name="login" v-model="form.login">
    <br>

    <label for="email">Email:</label>
    <input type="email" id="email" name="email" v-model="form.email">
    <br>

    <label for="password">Password:</label>
    <input type="password" id="password" name="password" v-model="form.password">
    <br>

    <label for="photo">Photo:</label>
    <input type="text" id="photo" name="photo" v-model="form.photo">
    <br>

    <button type="submit">Submit</button>
    <label v-text="errorMessage"></label>

  </form>
</template>

<script>
import {LOGIN_PAGE_NAME} from "../router/component_names";
import axios from "axios";

export default {
  name: "RegisterPage",

  data:function (){
    return{
      form:{
        login:'',
        password:'',
        email:'',
        photo:null
      },
      errorMessage:'',
      config:{
        headers: {
          'Content-Type': 'application/json;charset=UTF-8',
          "Access-Control-Allow-Origin": "*",
        }
      }
    }
  },

  methods:{
    submitForm(){
    /*TODO: check data before request*/

      axios.post("/registration",this.form,this.config).then((result)=>{
        this.$router.push({name:LOGIN_PAGE_NAME})
        // console.log(result.data)
      }).catch(error=>{
        this.errorMessage = 'Failed to register. '+error.response.data.message
        console.log(error)
      })
    }
  }
}
</script>

<style>

</style>