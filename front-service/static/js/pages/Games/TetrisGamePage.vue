<template>
	<div style="text-align: center">
		<h2>ТЕТРИС</h2>
	</div>
	<StartGameComponent
		v-if="!$store.state.playerGameId || $store.state.playerGameId === -1"
	/>
	<div class="container" v-else>
		<GameComponent
			:player="player"
			:entity="entity"
			:game="game"
		></GameComponent>
	</div>
</template>

<script>
import StartGameComponent from '../../components/tetris_game_components/StartGameComponent.vue'
import updateAuthUserInStorage from '../../service/auth'
import axios from 'axios'
import { connectToTetrisGame, connectToTttGame } from '../../service/ws'
import GameComponent from '../../components/tetris_game_components/GameComponent.vue'
export default {
	name: 'TetrisGamePage',
	components: { GameComponent, StartGameComponent },
	data: function() {
		return {
			player: {},
			entity: {},
			game: {},
			config: {
				headers: {
					'Content-Type': 'multipart/form-data;application/json',
					'Access-Control-Allow-Origin': '*',
				},
			},
      gameStopMessageShowed: false
		}
	},
	created() {
		if (this.$store.state.player) {
			this.player = this.$store.state.player
			this.player.img_data = this.$store.state.playerPhoto
			this.startGame()
		} else {
			updateAuthUserInStorage(this.$store).then(() => {
				this.player = this.$store.state.player
				this.player.img_data = this.$store.state.playerPhoto
				this.startGame()
			})
		}
	},
	methods: {
		startGame() {
			this.getGameAndConnect()
		},
		getGameAndConnect() {
			if (this.$store.state.playerGameId) {
				this.updateGameFromDb()
			} else {
				updateAuthUserInStorage(this.$store, this.updateGameFromDb)
			}
		},
		updateGameFromDb() {
			axios
				.get('/api/tetris_game/processing/' + this.$store.state.playerGameId)
				.then(response => {
					this.game = response.data
					connectToTetrisGame(this.game.gameId, this.updateState, this.$store)
				})
				.catch(error => {
					console.log(error)
				})
		},
		updateState(game) {
			// console.log(game)
			this.game = game;
      if(this.game.player1Time < 0 || this.game.player2Time < 0 ){ //кто-то сдался
        if(this.game.player1Time < 0 && this.game.player1.id == this.player.id
        || this.game.player2Time < 0 && this.game.player2.id == this.player.id){
          alert("Вы сдались!")
        }else{
          alert("Соперник сдался!")
        }
      }else{
        if(this.game.game1Stop == true || this.game.game2Stop == true){
          if(this.game.game1Stop == true && this.game.player1.id == this.player.id
        || this.game2Stop == true && this.game.player2.id == this.player.id){
            if(!this.gameStopMessageShowed){
              alert("Ваша игра завершена! Вы набрали " + this.game.player1Points + " очков")
              this.gameStopMessageShowed = true
            }
          }
          if(this.game.player2){
            if(this.game.game1Stop && this.game.game2Stop){
              if(this.game.player1Points > this.game.player2Points){
                if(this.game.player1.id == this.player.id){
                  alert("Вы выиграли!")
                }else{
                  alert("Вы проиграли!")
                }
              }else if(this.game.player1Points < this.game.player2Points){
                if(this.game.player2.id == this.player.id){
                  alert("Вы выиграли!")
                }else{
                  alert("Вы проиграли!")
                }
              }else{
                alert("Ничья!")
              }
            }
          }
        }
      }
		},
	},
}
</script>

<style></style>
