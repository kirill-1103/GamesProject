import { PlayerApi} from "../generated_sources/player/src";
import apiClient from "../generated_sources/player/src/ApiClient";
import {view_server_url} from "../../props";


setToken()
setInterval(()=>{
    setToken(apiClient.instance)
},10000)

export const playerApi = new PlayerApi(apiClient.instance)

export function setToken(){
    let bearerAuth = apiClient.instance.authentications['bearerAuth']
    bearerAuth.accessToken = localStorage['jwtToken']
    setDefaultHeaders([['Authorization','Bearer '+localStorage['jwtToken']]])
}

export function setDefaultHeaders(additionalHeaders){
    apiClient.instance.defaultHeaders = {
        'Access-Control-Allow-Origin': view_server_url,
        'Access-Control-Allow-Credentials': true,
        'Access-Control-Allow-Headers': "Origin, X-Requested-With, Content-Type, Accept, Key, Authorization, Access-Control-Allow-Headers, Origin,Accept, X-Requested-With, Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers"
    }
    if(additionalHeaders){
        for(let header of additionalHeaders){
            apiClient.instance.defaultHeaders[header[0]] = header[1];
        }
    }
}
