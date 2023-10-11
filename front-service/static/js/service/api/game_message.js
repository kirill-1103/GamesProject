const PREFIX_GAMES_MESSAGE = "/api/game_message"

export const gameMessagesByCodeAndIdPath = (id,code) => PREFIX_GAMES_MESSAGE + "/" + id + "/" + code;
export const NEW_PATH = PREFIX_GAMES_MESSAGE + "/new"

