import SockJS from 'sockjs-client'

import {Stomp} from '@stomp/stompjs'

let stompClient = null

export function connect(gameId,callback,store){
    const socket = new SockJS('/socket');
    stompClient = Stomp.over(socket);
    stompClient.connect({},frame=>{
        console.log("Connected: "+frame)
        stompClient.subscribe("/topic/ttt_game/"+gameId,game=>{

            // console.log(JSON.parse(game.body))
            callback(JSON.parse(game.body))
            // console.log('connected to game with id '+ game.id)
            // handlers.forEach(handler => handler(JSON.parse(move.body)))
        },(error)=>console.log("error"+JSON.stringify(error)))
    })
}


export function disconnect(){
    if(stompClient != null){
        stompClient.disconnect();
    }
    console.log('Disconnected');
}

export function sendMessageToConnectWithTime(){
    // if(stompClient){
    //     stompClient.send("/websocket/api/ttt_game/connect/",{})
    //     console.log('connect with time handler')
    // }else{
    //     console.log('cannot connect')
    // }
}