<template>
  <div id="app" class="main-div">
    <Nav></Nav>

    <router-view>
    </router-view>
  </div>
</template>

<script>
import Nav from "components/Nav.vue"
import axios from "axios";
import updateAuthUserInLocalStorage from "../service/auth.js";

export default {
  components: {
    Nav
  },
  created() {
    updateAuthUserInLocalStorage();
  }
}

</script>

<style>
</style>