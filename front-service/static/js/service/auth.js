import axios from "axios";
import {img_path} from "./api/player";
import {playerApi} from "./openapi/config/player_openapi_config";

let config = {
    headers: {
        'Content-Type': 'multipart/form-data;application/json',
        "Access-Control-Allow-Origin": "*",
    }
};


export default  function updateAuthUserInStorage(store, callback = null) {
    store.commit("setPlayerGameCode", null)
    store.commit("setPlayerGameId", null)
    return  new Promise(async (resolve,reject) => {
        playerApi
            .getAuthenticatedUser((error, data, response) => {
                if (error) {
                    store.commit("setPlayer", null);
                    reject(error)
                } else {
                    store.commit("setPlayer", data);
                    getPhoto(store)
                    getGameCodeAndId(store, callback)
                    resolve(data)
                }
            })
    })
    // return axios.get(AUTHENTICATED_PATH).then(response => {
    //     // console.log(response.data);
    //     if (response.data.error) {
    //         // localStorage.removeItem("player");
    //         store.commit("setPlayer", null);
    //     } else {
    //         // localStorage.setItem("player",JSON.stringify(response.data));
    //         store.commit("setPlayer", response.data)
    //         // console.log("STORE:",store.state.player)
    //     }
    //
    // }).catch((err) => {
    //     console.log(err.message)
    // }).then(() => getPhoto(store)).then(() => getGameCodeAndId(store, callback))
}

export function getPhoto(store) {
    if (store.state.player) {
        if (store.state.player.photo) {
            axios.get(img_path(store.state.player.photo), config).then((result) => {
                store.commit("setPlayerPhoto", "data:image/;base64, " + result.data.base64)
            }).catch(err => {
                console.log("ERR:");
                console.log(err)
            })
        }
    }
}

function getGameCodeAndId(store, callback) {
    if (store.state.player) {
        playerApi.getCurrentGameCode(store.state.player.id,(err,data,resp)=>{
            if(data){
                store.commit("setPlayerGameCode", data);
            }
            if(store.state.player){
                playerApi.getCurrentGameId(store.state.player.id,(err,data,resp)=>{
                    if(!data){
                        store.commit("setPlayerGameId", -1);
                    }else{
                        store.commit("setPlayerGameId", data)
                        if (callback) {
                            callback();
                        }
                    }
                })
            }
        })
    }
}