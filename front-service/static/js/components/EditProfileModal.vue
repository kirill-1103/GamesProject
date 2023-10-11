<template>
  <div class="modal fade" id="editProfileModal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
    <div class="modal-dialog">
      <div class="modal-content">
        <div class="modal-header">
          <h1 class="modal-title fs-5" id="exampleModalLabel">Редактирование профиля</h1>
          <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
        </div>
        <div class="modal-body">
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
        </div>
        <div class="modal-footer">
          <button type="button" class="button-close" data-bs-dismiss="modal">Закрыть</button>
          <button type="button" class="button-save" @click="save">Сохранить</button>
          <label class="error" v-text="errorMessage"></label>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import {checkLogin, checkEmail, checkPasswordEmptyOk} from "../service/correct_form.js";
import axios from "axios";
import {UPDATE_PATH} from "../service/api/player";
import {IMG_SIZE_PATH} from "../service/api/settings";


export default {
  name: "EditProfileModal",
  props: ['player'],
  methods: {
    save() {
      if (!this.form.login || !this.form.email) {
        alert('Логин и почта должны быть заполнены!')
      }
      if (!checkLogin(this.form.login)) {
        this.errorMessage = "Логин должен быть не пустой, содержать менее 70 символов - строчных латинских букв и цифр. Должен начинатся с буквы."
      } else if (!checkPasswordEmptyOk(this.form.password)) {
        this.errorMessage = "Пароль должен  содержать менее 70 символов - строчкных латинских букв и цифр."
      } else if ((!this.isEmpty(this.form.password) || !this.isEmpty(this.password2)) && this.form.password !== this.password2) {
        this.errorMessage = "Пароли должны совпадать!"
        console.log(this.errorMessage);
      } else if (!checkEmail(this.form.email)) {
        this.errorMessage = "Почта должна быть корректной, содержать менее 90 символов.";
      } else {
        // this.submitForm();
        this.errorMessage = ''
        this.submit();
      }
    },
    fileChange(e) {
      this.form.photo = e.target.files[0];
      this.filename = this.form.photo.name
    },
    isEmpty(s) {
      return !s || s.length === 0;
    },
    submit() {
      if (this.form.photo) {
        if (this.form.photo.size > this.max_file_size) {
          alert("Слишком большой файл!");
          return;
        }
      }
      let img = this.form.photo;
      this.form.photo = null;
      axios.post(UPDATE_PATH,
          {login:this.form.login,password:this.form.password,email:this.form.email,player_img:img,id:this.form.id}
          , this.config).then(response => {
            if(response.data && response.data.error){
              this.errorMessage = 'Failed to updated from server. '+response.data.message
              return;
            }else{
              localStorage["jwtToken"] = response.data;
              location.reload()
            }
          }
      ).catch(error=>{
        console.log(error)
        this.errorMessage = 'Failed to register from server. '+error.response.data.message
      })
    }
  },
  created() {
    axios.get(IMG_SIZE_PATH).then(result => {
      console.log('max file size:', result.data);
      this.max_file_size = result.data;
    })
  },
  data: function () {
    return {
      password2: '',
      filename: '',
      errorMessage: '',
      max_file_size: 0,
      form: {
        login: '',
        password: '',
        email: '',
        photo: '',
        id:-1
      },
      config: {
        headers: {
          'Content-Type': 'multipart/form-data;application/json',
          "Access-Control-Allow-Origin": "*",
        }
      }
    }
  },
  watch: {
    player(p, old) {
      this.form.email = p.email;
      this.form.photo = p.photo;
      this.form.login = p.login;
      this.form.password = p.password;
      this.form.id = p.id;
      this.password2 = '';
    }
  }

}
</script>

<style>
.button-close {
  width: 30%;
  background-color: rgb(128, 128, 128);
}

.button-close:hover {
  background-color: black;
  color: white;
}

.button-save {
  width: 30%;
}

input {
  width: 100%;
}
</style>