import axios from "axios";

export default function updateAuthUserInStorage(store){
    return axios.get("/api/player/authenticated").then(response=>{
        // console.log(response.data);
        if(response.data.error){
            // localStorage.removeItem("player");
            store.commit("setPlayer",null);
        }else{
            // localStorage.setItem("player",JSON.stringify(response.data));
            store.commit("setPlayer",response.data)
            console.log("STORE:",store.state.player)
        }
    }).catch((err)=>{
        console.log(err.message)
    })
}