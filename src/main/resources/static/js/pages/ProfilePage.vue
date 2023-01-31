<template>
  <div style="width:100%;" class="card ">
    <div class="card-body">
      <div class="d-flex flex-column align-items-center text-center">
        <img src="https://bootdey.com/img/Content/avatar/avatar7.png" alt="Admin" class="rounded-circle" width="150">
        <div class="mt-3">
          <h4>{{player.login}}</h4>
          <p class="text-secondary mb-1">Логин: {{player.login}}</p>
          <p class="text-secondary mb-1">Почта: {{player.email}}</p>
          <p class="text-secondary mb-1">Рейтинг: {{player.rating}}</p>
          <p class="text-secondary mb-1">Место в топе: 1</p>
          <p class="text-secondary mb-1">Дата регистрации: {{signUpTime}}</p>
          <button style="width:100%">Редактировать</button>
        </div>
      </div>
    </div>

  </div>

  <div  style="width:100%;"  class="card">
    <div class="card-body">
      <div class="d-flex flex-column align-items-center text-center">
<!--        TODO: таблица с историей игр: номер, название игры, противник, итог, время игры
            При нажатии в крестиках ноликах на номер - поле + длительность игры (может быть последовательность ходов)
            При нажатии на противника - профиль противника-->
          <h4>История игр</h4>
        <div class="table-scroll table-div">
          <table  class="table" style="overflow-y:scroll; scroll-behavior:smooth">
            <thead style="height: 20px">
            <tr>
              <th scope="col">№</th>
              <th scope="col">Игра</th>
              <th scope="col">Противник</th>
              <th scope="col">Итог</th>
              <th scope="col">Время игры</th>
            </tr>
            </thead>
          </table>
            <div class="table-scroll-body">
              <table>

                <tbody  >
                <tr>
                  <th scope="row">1</th>
                  <td>TTT</td>
                  <td>KIRILL_KIRILL_KIRILL_KIRILL</td>
                  <td>Победа</td>
                  <td>11.03.2001 03:00:55</td>
                </tr>
                <tr>
                  <th scope="row">2</th>
                  <td>TTT</td>
                  <td>KIRILL_KIRILL_KIRILL_KIRILL</td>
                  <td>Победа</td>
                  <td>11.03.2001 03:00:55</td>
                </tr>
                <tr>
                  <th scope="row">3</th>
                  <td>TTT</td>
                  <td>KIRILL_KIRILL</td>
                  <td>Победа</td>
                  <td>11.03.2001 03:00:55</td>
                </tr>
                <tr>
                  <th scope="row">3</th>
                  <td>TTT</td>
                  <td>KIRILL_KIRILL</td>
                  <td>Победа</td>
                  <td>11.03.2001 03:00:55</td>
                </tr>
                <tr>
                  <th scope="row">3</th>
                  <td>TTT</td>
                  <td>KIRILL_KIRILL</td>
                  <td>Победа</td>
                  <td>11.03.2001 03:00:55</td>
                </tr>
                <tr>
                  <th scope="row">3</th>
                  <td>TTT</td>
                  <td>KIRILL_KIRILL</td>
                  <td>Победа</td>
                  <td>11.03.2001 03:00:55</td>
                </tr>
                <tr>
                  <th scope="row">3</th>
                  <td>TTT</td>
                  <td>KIRILL_KIRILL</td>
                  <td>Победа</td>
                  <td>11.03.2001 03:00:55</td>
                </tr>
                <tr>
                  <th scope="row">3</th>
                  <td>TTT</td>
                  <td>KIRILL_KIRILL</td>
                  <td>Победа</td>
                  <td>11.03.2001 03:00:55</td>
                </tr>
                <tr>
                  <th scope="row">3</th>
                  <td>TTT</td>
                  <td>KIRILL_KIRILL</td>
                  <td>Победа</td>
                  <td>11.03.2001 03:00:55</td>
                </tr>
                <tr>
                  <th scope="row">3</th>
                  <td>TTT</td>
                  <td>KIRILL_KIRILL</td>
                  <td>Победа</td>
                  <td>11.03.2001 03:00:55</td>
                </tr>
                <tr>
                  <th scope="row">3</th>
                  <td>TTT</td>
                  <td>KIRILL_KIRILL</td>
                  <td>Победа</td>
                  <td>11.03.2001 03:00:55</td>
                </tr>
                <tr>
                  <th scope="row">3</th>
                  <td>TTT</td>
                  <td>KIRILL_KIRILL</td>
                  <td>Победа</td>
                  <td>11.03.2001 03:00:55</td>
                </tr>
                <tr>
                  <th scope="row">3</th>
                  <td>TTT</td>
                  <td>KIRILL_KIRILL</td>
                  <td>Победа</td>
                  <td>11.03.2001 03:00:55</td>
                </tr>
                <tr>
                  <th scope="row">3</th>
                  <td>TTT</td>
                  <td>KIRILL_KIRILL</td>
                  <td>Победа</td>
                  <td>11.03.2001 03:00:55</td>
                </tr>
                <tr>
                  <th scope="row">3</th>
                  <td>TTT</td>
                  <td>KIRILL_KIRILL</td>
                  <td>Победа</td>
                  <td>11.03.2001 03:00:55</td>
                </tr>

                </tbody>
              </table>
            </div>
        </div>

      </div>
    </div>

  </div>



</template>

<script>
import axios from "axios";
import updateAuthUserInLocalStorage from "../service/auth.js";
import fromArrayToDate from "../service/datetime.js";

export default {
  name: "ProfilePage",
  data:function(){
    return {
      player:{},
      signUpTime:''
    }
  },
  beforeCreate() {
    updateAuthUserInLocalStorage().then(()=>{//get player
      this.player = JSON.parse(localStorage.getItem("player"));
      this.signUpTime = fromArrayToDate(this.player.signUpTime);
      console.log(this.player);
    })
  }
}
</script>

<style>


.card {
  box-shadow: 0 1px 3px 0 rgba(0,0,0,.1), 0 1px 2px 0 rgba(0,0,0,.06);
}

.card {
  position: relative;
  display: flex;
  flex-direction: column;
  width:45%;
  /*margin:10px auto;*/
  margin-top:10px;
  word-wrap: break-word;
  background-clip: border-box;
  border: 0 solid rgba(0,0,0,.125);
  border-radius: .25rem;
  float: left;
}


.card-body {
  background-color: #fafafa;
  flex: 1 1 auto;
  min-height: 1px;
  padding: 1rem;
}

.gutters-sm {
  margin-right: -8px;
  margin-left: -8px;
}

.gutters-sm>.col, .gutters-sm>[class*=col-] {
  padding-right: 8px;
  padding-left: 8px;
}
.mb-3, .my-3 {
  margin-bottom: 1rem!important;
}

.bg-gray-300 {
  background-color: #e2e8f0;
}
.h-100 {
  height: 100%!important;
}
.shadow-none {
  box-shadow: none!important;
}
</style>