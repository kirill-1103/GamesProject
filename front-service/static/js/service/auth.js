import axios from "axios";
import {AUTHENTICATED_PATH, CURRENT_GAME_CODE_PATH, CURRENT_GAME_ID_PATH, IMAGE_PATH} from "./api/player";

let config = {
    headers: {
        'Content-Type': 'multipart/form-data;application/json',
        "Access-Control-Allow-Origin": "*",
    }
};

export default function updateAuthUserInStorage(store,callback=null){
    store.commit("setPlayerGameCode",null)
    store.commit("setPlayerGameId",null)
    return axios.get(AUTHENTICATED_PATH).then(response=>{
        // console.log(response.data);
        if(response.data.error){
            // localStorage.removeItem("player");
            store.commit("setPlayer",null);
        }else{
            // localStorage.setItem("player",JSON.stringify(response.data));
            store.commit("setPlayer",response.data)
            // console.log("STORE:",store.state.player)
        }

    }).catch((err)=>{
        console.log(err.message)
    }).then(()=>getPhoto(store)).then(()=>getGameCodeAndId(store,callback))
}

function getPhoto(store){
    if(store.state.player){
        if(store.state.player.photo){
            axios.post(IMAGE_PATH, {img_name: store.state.player.photo}, config).then((result) => {
                store.commit("setPlayerPhoto","data:image/;base64, " + result.data)
            }).catch(err => {
                console.log("ERR:");
                console.log(err)
            })
        }
    }
}

function getGameCodeAndId(store,callback){
    if(store.state.player){
        axios.post(CURRENT_GAME_CODE_PATH,{id:store.state.player.id},config)
            .then((result)=>{
                if(result.data){
                    store.commit("setPlayerGameCode",result.data);
                }
            }).then(()=>{
                if(store.state.player ){
                    axios.post(CURRENT_GAME_ID_PATH,{id:store.state.player.id},config)
                        .then(result=>{
                            if(!result.data){
                                store.commit("setPlayerGameId",-1);
                            }
                            if(result.data){
                                store.commit("setPlayerGameId",result.data)
                                if(callback){
                                    callback();
                                }
                            }
                        })
                }
        })
    }
}