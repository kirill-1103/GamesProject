<template>
  <RegisterForm
      :submitForm="submitForm"
      :errorMessage="errorMessage"
      :form="form"
  />

</template>

<script>
import {LOGIN_PAGE_NAME} from "../router/component_names";
import axios from "axios";
import RegisterForm from "../components/RegisterForm.vue";

export default {
  name: "RegisterPage",
  components:{
    RegisterForm
  },
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
        this.errorMessage = 'Failed to register from server. '+error.response.data.message
        console.log(error)
      })
    }
  }
}
</script>

<style>

</style>