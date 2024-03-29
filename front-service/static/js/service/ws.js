import SockJS from 'sockjs-client'

import {Stomp} from '@stomp/stompjs'
import {api_server_url, server_url} from "./props";


export function connectToTttGame(gameId, callback, store){
    let stompClient = getStompClient("/socket/ttt_new_game");
    stompClient.connect({},frame=>{
        stompClient.subscribe("/topic/ttt_game/"+gameId,game=>{
            callback(JSON.parse(game.body))
        },(error)=>console.log("error"+JSON.stringify(error)))
    })
}

export function connectToTetrisGame(gameId, callback, store){
    let stompClient = getStompClient("/socket/tetris_game");
    stompClient.connect({},frame=>{
        stompClient.subscribe("/topic/tetris_game/"+gameId,game=>{
            callback(JSON.parse(game.body))
        },(error)=>console.log("error"+JSON.stringify(error)))
    })
}

export function connectToSearchResult(playerId, callback){
    const stompClient = getStompClient("/socket/ttt_search");
    stompClient.connect({}, frame=>{
        stompClient.subscribe("/topic/ttt_player_search_ready/"+playerId,game=>{
            callback(JSON.parse(game.body));
        }, error=>console.log("error"+JSON.stringify(error)))
    })
}

export function connectToTetrisSearchResult(playerId, callback){
    const stompClient = getStompClient("/socket/tetris_search");
    stompClient.connect({}, frame=>{
        stompClient.subscribe("/topic/tetris_player_search_ready/"+playerId,game=>{
            callback(JSON.parse(game.body));
        }, error=>console.log("error"+JSON.stringify(error)))
    })
}

export function connectToGameMessages(gameId,gameCode,callback){
    const stompClient = getStompClient("/socket/game_messages");
    stompClient.connect({},frame=>{
        stompClient.subscribe("/topic/game_message/"+gameId+"/"+gameCode,message => {
            callback(JSON.parse(message.body));
        },error=>console.log("error:"+JSON.stringify(error)));
    })
}

export function connectToChats(playerId, callback){
    const stopmClient = getStompClient("/socket/chat");
    stopmClient.connect({},frame=>{
        stopmClient.subscribe("/topic/chat/"+playerId, message => {
            callback(JSON.parse(message.body));
        }, error=>console.log("error in socket:" + JSON.stringify(error)));
    })
}

function getStompClient(socket_name){
    const socket = new SockJS(socket_name);
    const stompClient = Stomp.over(socket);
    stompClient.debug = function(){};
    socket.url = api_server_url+socket_name
    return stompClient;
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