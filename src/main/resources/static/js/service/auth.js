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
            // console.log("STORE:",store.state.player)
        }
    }).catch((err)=>{
        console.log(err.message)
    }).then(()=>{//get photo
        let config = {
            headers: {
                'Content-Type': 'multipart/form-data;application/json',
                    "Access-Control-Allow-Origin": "*",
            }
        };
        if(store.state.player){
            axios.post("/api/player/image", {img_name: store.state.player.photo}, config).then((result) => {
                // console.log(result);
                // console.log(typeof (result.data))
                store.commit("setPlayerPhoto","data:image/;base64, " + result.data)
                // console.log(this.imgSrc)
            }).catch(err => {
                console.log("ERR:");
                console.log(err)
            })
        }
    })
}