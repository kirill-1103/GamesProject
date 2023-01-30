<template>
  <div class="head">
    <nav class="navbar navbar-expand-lg head" >
      <div class="container-fluid">
          <span class="navbar-brand" href="#">
            <img src="../../img/logo1.png" alt="logo" width="34" height="28" class="d-inline-block align-text-top">
            Games
          </span>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
          <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarSupportedContent">
          <ul class="navbar-nav me-auto mb-2 mb-lg-0">
            <li class="nav-item">
              <a class="nav-link" href="/me">Профиль</a>
            </li>
            <li class="nav-item">
              <a class="nav-link" href="/game_list">Список игр</a>
            </li>

            <li class="nav-item">
              <a class="nav-link" href="/rating">Рейтинг</a>
            </li>

            <li class="nav-item">
              <a class="nav-link" href="/player_list">
                Игроки
              </a>
            </li>

            <li class="nav-item">
              <a class="nav-link" href="/chat">
                Чат
              </a>
            </li>

            <li class="nav-item">
              <a class="nav-link disabled" href="#">Текущая игра</a>
            </li>
          </ul>

          <a class="nav-link" href="/logout">
            Выход
          </a>
        </div>
      </div>
    </nav>
  </div>
</template>

<script>
export default {
  name:"Nav"
}
</script>