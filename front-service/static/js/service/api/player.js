import {log} from "util";

const PREFIX_PLAYER = "/api/player"

export const oneByIdPath = (id) => PREFIX_PLAYER + "/" + id;
export const oneByLoginPath = (login) => PREFIX_PLAYER + "/login/" + login;
export const topPlayerByIdPath = (id) => PREFIX_PLAYER + "/top/" + id;
export const AUTHENTICATED_PATH = PREFIX_PLAYER + "/authenticated";
export const IMAGE_PATH = PREFIX_PLAYER + "/image";
export const IMAGES_PATH = PREFIX_PLAYER + "/images"
export const UPDATE_PATH = PREFIX_PLAYER + "/update"
export const CURRENT_GAME_ID_PATH = PREFIX_PLAYER + "/currentGameId"
export const CURRENT_GAME_CODE_PATH = PREFIX_PLAYER + "/currentGameCode"
export const RATING_PATH = PREFIX_PLAYER + "/rating"

export const SEARCH_PATH = PREFIX_PLAYER + "/search"

export const UPDATE_ACTIVE_PATH = PREFIX_PLAYER + "/update-active"