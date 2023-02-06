import SockJS from 'sockjs-client'

import {Stomp} from '@stomp/stompjs'

let stompClient = null

const handlers = []

const config_ = {
    headers:{
        'Content-Type':'multipart/form-data;application/json',
            "Access-Control-Allow-Origin": "*",
    }
}

export function connect(){
    const socket = new SockJS('/socket');
    stompClient = Stomp.over(socket);
    stompClient.connect({},frame=>{
        console.log("Connected: "+frame)
        stompClient.subscribe("/topic/ttt_move",move=>{
            console.log('move:',move)
            handlers.forEach(handler => handler(JSON.parse(move.body)))
        },(error)=>console.log("error"+JSON.stringify(error)))
    })
    // const socket2 = new SockJS('http://app/localhost:8080/socket/websocket');
    // // const socket2 = new SockJS('/socket');
    // stompClient = Stomp.over(socket2);
    // stompClient.connect({},frame=>{
    //     console.log("Connected: "+frame)
    //     stompClient.subscribe("/topic/ttt_move",move=>{
    //         handlers.forEach(handler => handler(JSON.parse(move.body)))
    //     },(error)=>console.log("error"+JSON.stringify(error)))
    // })
    // const socket3 = new SockJS('http://localhost:8080/socket/websocket');
    // stompClient = Stomp.over(socket3);
    // stompClient.connect({},frame=>{
    //     console.log("Connected: "+frame)
    //     stompClient.subscribe("/topic/ttt_move",move=>{
    //         handlers.forEach(handler => handler(JSON.parse(move.body)))
    //     },(error)=>console.log("error"+JSON.stringify(error)))
    // })
}

export function addHandler(handler){
    handlers.push(handler);
}

export function disconnect(){
    if(stompClient != null){
        stompClient.disconnect();
    }
    console.log('Disconnected');
}

export function sendMessage(move){
    if(stompClient){
        stompClient.send("/websocket/api/ttt_move/temp",{}, JSON.stringify(move))
        console.log('send move')
    }else{
        console.log('cannot send move')
    }
}