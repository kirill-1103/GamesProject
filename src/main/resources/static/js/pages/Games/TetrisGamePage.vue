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
			this.game = game
		},
	},
}
</script>

<style></style>
