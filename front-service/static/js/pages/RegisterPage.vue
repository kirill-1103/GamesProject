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
import {REGISTRATION_PATH} from "../service/api/auth";
import {IMG_SIZE_PATH} from "../service/api/settings";

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
        img_file:null
      },
      errorMessage:'',
      badFileRequest:false,
      max_file_size:0,
      config1:{
        headers: {
          'Content-Type': 'application/json;charset=UTF-8',
          "Access-Control-Allow-Origin": "*",
        }
      },
      config2:{
        headers:{
         'Content-Type':'multipart/form-data;application/json',
          "Access-Control-Allow-Origin": "*",
        }
      }
    }
  },

  created() {
    let interval = setInterval(()=>{
      if(this.$store.state.player){
        this.$router.push("/me")
        clearInterval(interval)
      }
    },100)
    axios.get(IMG_SIZE_PATH).then(result=>{
      console.log('max file size:', result.data);
      this.max_file_size = result.data;
    })
  },

  methods:{
    submitForm(){
      if(this.form.img_file){
        if(this.form.img_file.size>this.max_file_size){
          alert("Слишком большой файл!");
          return;
        }
      }
      let img = this.form.img_file;
      this.form.img_file = null;
      axios.post(REGISTRATION_PATH,
          {login:this.form.login,password:this.form.password,email:this.form.email,player_img:img}
          ,this.config2)
          .then(result=>{
            if(result.data.error){
              this.errorMessage = 'Failed to register from server. '+result.data.message
              return;
            }
            this.$router.push({name:LOGIN_PAGE_NAME});
          }).catch(error=>{
          console.log(error)
          this.errorMessage = 'Failed to register from server. '+error.response.data.message
      })
    },
    saveImg(img_file){
      return axios.post("/api/file/player_image",img_file,this.config2).then((result)=>{

      }).catch((error)=>{
        this.badFileRequest=true;
        console.log(error);
      })
    }
  }
}
</script>

<style>

</style>