import axios from "axios";

export default function updateAuthUserInLocalStorage(){
    return axios.get("/api/player/authenticated").then(response=>{
        // console.log(response.data);
        if(response.data.error){
            localStorage.removeItem("player");
        }else{
            localStorage.setItem("player",JSON.stringify(response.data));
        }
    }).catch((err)=>{
        console.log(err.message)
    })
}