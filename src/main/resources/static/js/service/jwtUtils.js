import axios from "axios";

export function tokenIsExpired(token){
    const tokenT =tokenTime(token)
    return tokenT == null || tokenT <= 0;
}

export function tokenTime(token){
    const tokenParts = token.split('.');
    if(tokenParts.length !== 3){
        return null;
    }
    const encodedPayload = tokenParts[1];
    try{
        const decodedPayload = atob(encodedPayload);
        const payloadObj = JSON.parse(decodedPayload);

        if (!payloadObj.exp) {
            return null;
        }
        const tokenExpiration = payloadObj.exp * 1000;
        const currentTimeStamp = Date.now();
        return (tokenExpiration-currentTimeStamp);
    }catch (err){
        return null;
    }
}

export function updateToken(){
    axios.post("/updateToken")
        .then((data)=>{
            localStorage['jwtToken'] = data.data;
        }).catch((err)=>{
            console.log(err);
        })
}