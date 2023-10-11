const TTT_GAME_PREFIX = "/api/ttt_game"
const TTT_MOVE_PREFIX  = "/api/ttt_move"

export const TTT_NEW_PATH = TTT_GAME_PREFIX + "/new"
export const tttOneByPlayerIdPath = (id) => TTT_GAME_PREFIX + "/" + id
export const TTT_SURRENDER_PATH = TTT_GAME_PREFIX + "/surrender"
export const TTT_MAKE_MOVE_PATH = TTT_GAME_PREFIX + "/make_move"
export const TTT_SEARCH_PATH = TTT_GAME_PREFIX + "/search"

export const TTT_STOP_SEARCH_PATH = TTT_GAME_PREFIX + "/stop_search"

export const TTT_MOVE_ALL = TTT_MOVE_PREFIX + "/all"

