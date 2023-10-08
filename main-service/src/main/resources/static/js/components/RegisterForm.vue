<template>
  <form @submit.prevent="checkAndSubmitForm" class="form">
    <label for="login">Логин:</label>
    <input type="text" id="login" name="login" v-model="form.login">
    <br>

    <label for="email">Почта:</label>
    <input type="email" id="email" name="email" v-model="form.email">
    <br>

    <label for="password">Пароль:</label>
    <input type="password" id="password" name="password" v-model="form.password">
    <br>

    <label for="2password">Повторите пароль:</label>
    <input type="password" id="2password" name="2password" v-model="password2">
    <br>

    <label>
      Фото:
    </label>
    <label class="label_file" for="photo">
      Выберите фото <br/>
      <i class="fa fa-2x fa-camera"></i>
    <input type="file" id="photo" name="photo" v-on:change="fileChange"
      accept=".jpg, .jpeg, .png, .bmp">
      <br/>
      <span v-text="filename"></span>
    </label>
    <br>

    <button type="submit">Регистрация</button>
    <button @click="goToSignIn">Перейти на страницу входа</button>
    <label class="error" v-text="errorMessage2"></label>
  </form>
</template>

<script>
import {LOGIN_PAGE_NAME} from "../router/component_names";
import {checkLogin,checkEmail,checkPassword} from "../service/correct_form";

export default {
  name: 'RegisterForm',

  props: ['form', 'errorMessage', "submitForm"],

  data: function () {
    return {
      password2: "",
      errorMessage2:"",
      filename:""
    }
  },
  watch:{
    errorMessage(newMessage, oldMessage){
      this.errorMessage2 = newMessage;
    }
  },

  methods: {
    changeLogin() {
      localStorage.setItem("login", this.form.login)
    },
    goToSignIn() {
      this.$router.push({name: LOGIN_PAGE_NAME});
    },
    checkAndSubmitForm() {
      if(!checkLogin(this.form.login)){
        this.errorMessage2 = "Логин должен быть не пустой, содержать менее 70 символов - строчных латинских букв и цифр. Должен начинатся с буквы."
      }else if(!checkPassword(this.form.password)){
        this.errorMessage2 = "Пароль должен быть не пустой, содержать менее 70 символов - строчкных латинских букв и цифр."
      }
      else if(this.form.password !== this.password2){
        this.errorMessage2 = "Пароли должны совпадать!"
        console.log(this.errorMessage2);
      }else if(!checkEmail(this.form.email)){
        this.errorMessage2 = "Почта должна быть корректной, содержать менее 90 символов.";
      }
      else{
        this.submitForm();
      }
    },
    fileChange(e){
      this.form.img_file = e.target.files[0];
      this.filename = this.form.img_file.name
    }
  }
}

</script>

<style>

</style>