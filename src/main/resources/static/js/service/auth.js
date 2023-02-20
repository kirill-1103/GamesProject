import axios from "axios";

let config = {
    headers: {
        'Content-Type': 'multipart/form-data;application/json',
        "Access-Control-Allow-Origin": "*",
    }
};

export default function updateAuthUserInStorage(store,callback=null){
    store.commit("setPlayerGameCode",null)
    store.commit("setPlayerGameId",null)
    return axios.get("/api/player/authenticated").then(response=>{
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
        axios.post("/api/player/image", {img_name: store.state.player.photo}, config).then((result) => {
            store.commit("setPlayerPhoto","data:image/;base64, " + result.data)
        }).catch(err => {
            console.log("ERR:");
            console.log(err)
        })
    }
}

function getGameCodeAndId(store,callback){
    if(store.state.player){
        axios.post("/api/player/currentGameCode",{id:store.state.player.id},config)
            .then((result)=>{
                if(result.data){
                    store.commit("setPlayerGameCode",result.data);
                }
            }).then(()=>{
                if(store.state.player ){
                    axios.post("/api/player/currentGameId",{id:store.state.player.id},config)
                        .then(result=>{
                            console.log("id:"+result.data)
                            if(!result.data){
                                store.commit("setPlayerGameId",-1);
                            }
                            if(result.data){
                                store.commit("setPlayerGameId",result.data)
                                if(callback){
                                    callback();
                                }
                                console.log("game-id:"+result.data)
                            }
                        })
                }
        })
    }
}