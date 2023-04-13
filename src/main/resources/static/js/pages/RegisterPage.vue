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
      console.log('a')
      if(this.$store.state.player){
        this.$router.push("/me")
        clearInterval(interval)
      }
    },100)
    axios.get("/api/settings/img_size").then(result=>{
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
      axios.post("/registration",
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
      // axios.post("/registration",this.form,this.config1).then((result)=>{
      //   if(this.form.img_file){
      //     this.form.img_file
      //     console.log(this.form.img_file)
      //     this.saveImg({image:this.form.img_file,player_id:result.data.id}).then(()=>{
      //       if(this.badFileRequest){
      //         this.badFileRequest=false;
      //         alert("Не удалось загрузить фотографию. Попробуйте позже - в разделе профиль.")
      //       }
      //       this.$router.push({name:LOGIN_PAGE_NAME})
      //     });
      //   }else{
      //     this.$router.push({name:LOGIN_PAGE_NAME})
      //   }
      //   // console.log(result.data)
      // }).catch(error=>{
      //   console.log(error)
      //   this.errorMessage = 'Failed to register from server. '+error.response.data.message
      // })
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