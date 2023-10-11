const PREFIX_TETRIS = "/api/tetris_game"

export const COMPUTER_PATH = PREFIX_TETRIS + "/computer"
export const TETRIS_SEARCH_PATH = PREFIX_TETRIS + "/search"
export const TETRIS_SEARCH_STOP_PATH = PREFIX_TETRIS +"/stop_search"
export const tetrisOneInProcessByIdPath = (id) => PREFIX_TETRIS + "/processing/" + id;
export const TETRIS_MOVE_PATH = PREFIX_TETRIS + "/move"

export const TETRIS_SURRENDER_PATH = PREFIX_TETRIS + "/surrender"

