<template>
  <div id="app" class="main-div">
    <Nav :player="player"></Nav>

    <router-view>
    </router-view>
  </div>
</template>

<script>
import Nav from "components/Nav.vue"
import axios from "axios";
import updateAuthUserInStorage from "../service/auth.js";

export default {
  components: {
    Nav
  },
  data: function () {
    return {
      player: null
    }
  },
  mounted() {
    updateAuthUserInStorage(this.$store).then(() => {
          if (this.$store.state.player) {
            this.player = this.$store.state.player;
          } else {
            this.player = null;
          }
          console.log(this.player)
        }
    )
  },
  created() {
    updateAuthUserInStorage(this.$store).then(() => {
          if (this.$store.state.player) {
            this.player = this.$store.state.player;
          } else {
            this.player = null;
          }
          console.log(this.player)
        }
    );

  }
}

</script>

<style>
</style>