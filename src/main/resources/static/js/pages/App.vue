<template>
  <div id="app">
    <div class="head">
      <form action="/logout" method="POST">
        <input type="submit" value="logout">
      </form>
    </div>
    Starting...

    <router-view>
    </router-view>
  </div>
</template>