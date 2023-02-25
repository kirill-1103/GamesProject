<template>
  <div class="div_chat">
    <div class="container " style="margin:0;width: 503px;height:503px">
            <div style="margin:0;margin-left:-12px;width: 503px;height:503px" class="card card-bordered">
              <div class="card-header">
                <h4 class="card-title"><strong>Chat</strong></h4>
                <a class="btn btn-xs btn-secondary" href="#" data-abc="true">Let's Chat App</a>
              </div>

              <div class="ps-container ps-theme-default ps-active-y" ref="chat_content" id="chat-content" style="overflow-y: scroll !important; height:400px !important;">
                <div v-for="message of messages">
                  <div id = "{{message.id}}" v-if="!isMe(message)" class="media media-chat">
                    <div class="media-body">
                      <p>{{message.message}}</p>
                      <p class="meta" style="font-size: 13px"><time datetime="2023">{{message.time}}</time></p>
                    </div>
                  </div>

                  <div v-else id="{{message.id}}" class="media media-chat media-chat-reverse" style="">
                    <div class="media-body" style="width: 100%">
                      <p>{{message.message}}</p>
                      <p class="meta" style="color:gray;font-size: 13px"><time datetime="2023">{{message.time}}</time></p>
                    </div>
                  </div>
                </div>

                <div class="ps-scrollbar-x-rail" style="left: 0px; bottom: 0px;"><div class="ps-scrollbar-x" tabindex="0" style="left: 0px; width: 0px;"></div></div><div class="ps-scrollbar-y-rail" style="top: 0px; height: 0px; right: 2px;"><div class="ps-scrollbar-y" tabindex="0" style="top: 0px; height: 2px;"></div></div></div>

                <form ref="form_send_message">
                  <div class="publisher bt-1 border-light">
                  <input class="publisher-input" ref="textForSend" type="text" placeholder="Write something">
                  <a class="publisher-btn" href="#" data-abc="true"><i class="fa fa-smile"></i></a>
                  <button :disabled="!messagesLoaded" class="publisher-btn text-info btn-send" type="submit" data-abc="true"><i class="fa fa-paper-plane"></i></button>
                  </div>
                </form>

          </div>
        </div>


  </div>
</template>

<script>
import {connectToGameMessages} from "../service/ws";
import axios from "axios";
import {fromArrayToHoursMinutesSeconds, fromStringToHoursMinutesSeconds} from "../service/datetime";

export default{
  name:'GameChatComponent',
  props:['game','player'],
  data:function() {
    return {
      config: {
        headers: {
          'Content-Type': 'application/json',
          'Accept': 'application/json'
        }
      },
      messages:[],
      messagesLoaded :false
    }
  },
  created() {
    console.log('created')
    this.getMessages();
  },
  mounted() {
    this.$refs.form_send_message.addEventListener("submit",this.send);
  },
  methods:{
    send(ev){
      ev.preventDefault();
      let messageFromInput = this.$refs.textForSend.value
      if(messageFromInput.length === 0){
        return;
      }
      let data = {
        game_id:this.game.id,
        game_code:this.game.gameCode,
        sender_id: this.player.id,
        message: messageFromInput
      }
      axios.post("/api/game_message/new",data,this.config);
      this.$refs.textForSend.value = '';
    },
    isMe(message){
      return message.senderId === this.player.id;
    },
    getMessages(){
      let interval = setInterval(()=>{
        if(this.game && this.game.id && this.game.gameCode){
          axios.get("/api/game_message/"+this.game.id+"/"+this.game.gameCode)
              .then(messages=>{
                this.messages = messages.data;
                for (let mess of this.messages){
                  mess.time = fromArrayToHoursMinutesSeconds(mess.time)
                }
                this.messages = this.messages.reverse();
                connectToGameMessages(this.game.id, this.game.gameCode, this.addMessage);
                this.messagesLoaded = true;

              })
          clearInterval(interval)
        }
      },100)
    },
    addMessage(message){
      message.time = fromStringToHoursMinutesSeconds(message.time);
      this.messages.unshift(message)
    }
  }
}
</script>

<style>
.div_chat{
  width:80%;
  margin:auto;
  height:100%;
  background: white;
}
.btn-send{
  width:0;
  margin:0;
  margin-right: 10px;
  padding:0;
}
.btn-send:hover{
  background: none;
}
.btn-send:active{
  background: none;
  border:none;
}

</style>