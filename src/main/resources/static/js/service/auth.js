import axios from "axios";

export default function updateAuthUserInLocalStorage(){
    return axios.get("/api/player/authenticated").then(response=>{
        // console.log(response.data);
        localStorage.setItem("player",JSON.stringify(response.data));
    }).catch((err)=>{
        console.log(this.err.message)
    })
}