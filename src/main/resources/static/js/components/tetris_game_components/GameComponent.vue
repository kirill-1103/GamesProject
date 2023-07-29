<template>
	<div v-if="!game.field2" style="text-align: center">
		<Canvas
			:field="game.field1"
			:isActive="true"
			:isShowing="false"
			:makeMove="makeMove"
		/>
	</div>
	<div v-else></div>
</template>

<script>
import axios from 'axios'
import Canvas from './Canvas.vue'
export default {
	name: 'GameComponent',
	components: { Canvas },
	props: ['player', 'entity', 'game'],
	data: function() {
		return {
			config: {
				headers: {
					'Content-Type': 'multipart/form-data;application/json',
					'Access-Control-Allow-Origin': '*',
				},
			},
		}
	},
	methods: {
		makeMove(moveCode) {
			let data = {
				player_id: this.player.id,
				game_id: this.game.gameId,
				move_code: moveCode,
			}
			axios.post('/api/tetris_game/move', data, this.config).then(response => {
				console.log(response)
			})
		},
	},
}
</script>

<style></style>
