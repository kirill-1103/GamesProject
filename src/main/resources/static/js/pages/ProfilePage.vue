<template>
  <div style="width:30%; height:550px;" class="card ">
    <div class="card-body">
      <div class="d-flex flex-column align-items-center text-center">
        <div v-if="player.login">
          <img v-if="!player.photo" src="../../img/default.png" alt="img" class="rounded-circle" width="150">
          <img v-if="player.photo" src={{imgSrc}} id="player_photo" alt="img" class="rounded-circle" width="150"/>
          <div class="mt-3">
            <h4>{{ player.login }}</h4>
            <p class="text-secondary mb-1">Логин: {{ player.login }}</p>
            <p class="text-secondary mb-1">Почта: {{ player.email }}</p>
            <p class="text-secondary mb-1">Рейтинг: {{ player.rating }}</p>
            <p class="text-secondary mb-1">Место в топе: 1</p>
            <p class="text-secondary mb-1">Дата регистрации: {{ signUpTime }}</p>
            <button data-bs-toggle="modal" data-bs-target="#editProfileModal" style="width:100%">Редактировать</button>
          </div>
        </div>
        <div style="margin-top:50%;" v-else class="spinner-border text-primary" role="status">
          <span class="visually-hidden">Loading..</span>
        </div>
      </div>
    </div>

  </div>

  <div style="width:67%;" class="card">
    <div class="card-body">
      <div class="d-flex flex-column align-items-center text-center">
        <!--        TODO: таблица с историей игр: номер, название игры, противник, итог, время игры
                    При нажатии в крестиках ноликах на номер - поле + длительность игры (может быть последовательность ходов)
                    При нажатии на противника - профиль противника-->
        <h4>История игр</h4>
        <div class="table-scroll table-div">
          <table class="table " style="overflow-y:scroll; scroll-behavior:smooth">
            <thead style="height: 20px">
            <tr>
              <th class = "col1" scope="col">№</th>
              <th class = "col2" scope="col">Игра</th>
              <th class = "col3" scope="col">Противник</th>
              <th class = "col4" scope="col">Итог</th>
              <th class = "col5" scope="col">Время игры</th>
            </tr>
            </thead>
          </table>
          <div class="table-scroll-body">
            <table>
              <tbody>
              <tr >
                <th class = "col1"  scope="row">3</th>
                <td class = "col2">Крестики-Нолики</td>
                <td class = "col3">KIRILL_KIRILL</td>
                <td class = "col4">Поражение</td>
                <td class = "col5">11.03.2001 03:00:55</td>
              </tr>

              </tbody>
            </table>
          </div>
        </div>

      </div>
    </div>

    <EditProfileModal :player="player"/>

  </div>


</template>

<script>
import axios from "axios";
import updateAuthUserInStorage from "../service/auth.js";
import fromArrayToDate from "../service/datetime.js";
import EditProfileModal from "../components/EditProfileModal.vue";

export default {
  name: "ProfilePage",
  components: {
    EditProfileModal
  },
  data: function () {
    return {
      player: {login:null},
      signUpTime: '',
      config: {
        headers: {
          'Content-Type': 'multipart/form-data;application/json',
          "Access-Control-Allow-Origin": "*",
        }
      },
      imgSrc: ''
    }
  },
  created() {
    updateAuthUserInStorage(this.$store).then(() => {//get player
      this.player = this.$store.state.player;
      this.signUpTime = fromArrayToDate(this.player.signUpTime);
      console.log(this.player);
      if (this.player.photo && this.player.photo !== '') {
        axios.post("/api/player/image", {img_name: this.player.photo}, this.config).then((result) => {
          // console.log(result);
          // console.log(typeof (result.data))
          this.imgSrc = "data:image/;base64, " + result.data;
          let img = document.getElementById("player_photo");
          img['src'] = this.imgSrc;
    
          // console.log(this.imgSrc)
        }).catch(err => {
          console.log("ERR:");
          console.log(err)
        })
      }
    })
  }
}
</script>

<style>


.card {
  box-shadow: 0 1px 3px 0 rgba(0, 0, 0, .1), 0 1px 2px 0 rgba(0, 0, 0, .06);
}

.card {
  position: relative;
  display: flex;
  flex-direction: column;
  width: 45%;
  /*margin:10px auto;*/
  margin-top: 10px;
  word-wrap: break-word;
  background-clip: border-box;
  border: 0 solid rgba(0, 0, 0, .125);
  border-radius: .25rem;
  float: left;
}

.col1{
  width:4%;
}

.col2{
  width:20%;
}

.col3{
  width:30%;
}
.col4{
  width:18%;
}

.card-body {
  background-color: #fafafa;
  flex: 1 1 auto;
  min-height: 1px;
  padding: 1rem;
}
 th,td{
  font-size:12px !important;;
}

.gutters-sm {
  margin-right: -8px;
  margin-left: -8px;
}

.gutters-sm > .col, .gutters-sm > [class*=col-] {
  padding-right: 8px;
  padding-left: 8px;
}

.mb-3, .my-3 {
  margin-bottom: 1rem !important;
}

.bg-gray-300 {
  background-color: #e2e8f0;
}

.h-100 {
  height: 100% !important;
}

.shadow-none {
  box-shadow: none !important;
}

</style>