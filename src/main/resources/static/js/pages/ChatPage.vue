<template>
  <div class="content">
    <div class="container p-0">


      <div class="card card3">
        <div class="row g-0">
          <div class="col-12 col-lg-5 col-xl-3 border-right">

            <div class="px-4 d-none d-md-block">
              <div class="d-flex align-items-center">
                <div class="flex-grow-1">
                  <p type="text" class="">
                    <br>
                    <br>
                  </p>
                </div>
              </div>
            </div>
            <div v-if="dialogsIsLoaded" class="players">
              <div v-for="dialog of dialogs" class="companion" v-bind:class="{selected : companion && dialog.companion.id == companion.id}">
                <a @click="openDialog(dialog)" href="#"
                   class="list-group-item list-group-item-action border-0 companion-item">
                  <div v-show="dialog.hasUnread" class="badge bg-success float-right">!</div>
                  <div class="d-flex align-items-start">
                    <img v-if="dialog.companion.photo && dialog.companion.imageUrl" :src="dialog.companion.imageUrl"
                         class="rounded-circle mr-1"
                         alt="img" width="65" height="65">
                    <div v-else-if="dialog.companion.photo && !dialog.companion.imageUrl" style="margin-top:50%;"
                         class="spinner-border text-primary" role="status">
                      <span class="visually-hidden">Loading..</span>
                    </div>
                    <img v-else-if="!dialog.companion.photo" src="../../img/default.png" class="rounded-circle mr-1"
                         alt="img" width="65" height="65">
                    <div class="flex-grow-1 ml-3" style="margin-left: 10px;">
                      {{ dialog.companion.login }}
                      <div v-if="isOnline(dialog.companion.id)" class="small"><span class="fas fa-circle chat-online"></span> Online</div>
                      <div v-else class="small"><span class="fas fa-circle chat-offline"></span> Offline</div>
                    </div>
                  </div>
                </a>
                <br/>
              </div>
            </div>

            <div v-else="dialog.companion.photo && !dialog.companion.imageUrl" style="margin-top:50%; margin-left:50%"
                 class="spinner-border text-primary" role="status">
              <span class="visually-hidden">Loading..</span>
            </div>


            <hr class="d-block d-lg-none mt-1 mb-0">
          </div>

          <div class="col-12 col-lg-7 col-xl-9">
            <div v-if="companion != null" class="py-2 px-4 border-bottom d-none d-lg-block">
              <div class="d-flex align-items-center py-1" style="text-align: center">
                <div class="position-relative">
                  <img v-if="companion.photo && companion.imageUrl" :src="companion.imageUrl"
                       class="rounded-circle mr-1"
                       alt="img" width="65" height="65">
                  <div v-else-if="companion.photo && !companion.imageUrl" style="margin-top:50%;"
                       class="spinner-border text-primary" role="status">
                    <span class="visually-hidden">Loading..</span>
                  </div>
                  <img v-else-if="!companion.photo" src="../../img/default.png" class="rounded-circle mr-1"
                       alt="img" width="65" height="65">
                </div>
                <div class="flex-grow-1 pl-3" style="margin-left: 0.5rem;">
                  <strong>{{ companion.login }}</strong>
                </div>
              </div>
            </div>

            <div class="position-relative" style="height: 100%">
              <div v-show="companion != null" class="chat-messages p-4" ref="chat">
                <button v-if = "unreadMessageInCurrentDialog" class="scroll-button" v-on:click="scrollChat">
                  <i class="fa fa-arrow-down" aria-hidden="true"></i>
                </button>
                <div v-if="companion!=null" v-for="message of messages" v-bind:class="{'chat-message-right':message.sender.id !== companion.id,
                'mb-4':message.sender.id !== companion.id, 'pb-4':message.sender.id === companion.id,
                 'chat-message-left':message.sender.id === companion.id  }">
                  <div>

                    <div v-if="message.sender.id === companion.id">
                      <img v-if=" companion.photo && companion.imageUrl" :src="companion.imageUrl"
                           class="rounded-circle mr-1"
                           alt="img" width="40" height="40">
                      <div v-else-if="companion.photo && !companion.imageUrl"
                           class="spinner-border text-primary" role="status">
                        <span class="visually-hidden">Loading..</span>
                      </div>
                      <img v-else-if="!companion.photo" src="../../img/default.png" class="rounded-circle mr-1"
                           alt="img" width="40" height="40">
                    </div>
                    <div v-else>
                      <img v-if=" player.photo && imgSrc" :src="imgSrc"
                           class="rounded-circle mr-1"
                           alt="img" width="40" height="40">
                      <div v-else-if="player.photo && !imgSrc"
                           class="spinner-border text-primary" role="status">
                        <span class="visually-hidden">Loading..</span>
                      </div>
                      <img v-else-if="!player.photo" src="../../img/default.png" class="rounded-circle mr-1"
                           alt="img" width="40" height="40">
                    </div>


                    <div class="text-muted small text-nowrap mt-2">{{ getStringDate(message.sendingTime)[0] }}</div>
                    <div class="text-muted small text-nowrap mt-2">{{ getStringDate(message.sendingTime)[1] }}</div>
                  </div>
                  <div class="flex-shrink-1 bg-light rounded py-2 px-3 mr-3">
                    <div v-if="message.sender.id === player.id" class="font-weight-bold mb-1 loginInMessage">You</div>
                    <div v-else class="font-weight-bold mb-1 loginInMessage">{{ message.sender.login }}</div>
                    {{ message.messageText }}
                  </div>
                </div>

              </div>
              <div v-if="companion!=null" class="flex-grow-0 py-3 px-4 border-top">
                <div class="input-group">
                  <input type="text" class="form-control" placeholder="Type your message" v-model="messageInput">
                  <button class="btn btn-primary" @click="sendMessage" :disabled="messageInput.length === 0">Send
                  </button>
                </div>
              </div>

              <div class="chat-messages" v-else-if="companion == null"
                   style="width:100%; height: 100%; text-align: center; ">
                <p style="margin: auto">Choose dialog</p>
              </div>
            </div>


          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import updateAuthUserInStorage from "../service/auth";
import {fromArrayToDate, fromArrayToDateWithTime} from "../service/datetime";

export default {
  name: "ChatPage",
  data: function () {
    return {
      player: null,
      dialogs: [],
      messages: null,
      companion: null,
      imgSrc: null,
      messageInput: "",
      dialogsIsLoaded: false,
      newMessages:[],
      onlineIds:[],
      unreadMessageInCurrentDialog:false,
      newCompanionId: null,
      photosIsLoaded:false,
      config: {
        headers: {
          'Content-Type': 'multipart/form-data;application/json',
          "Access-Control-Allow-Origin": "*",
        }
      },
    }
  },
  created() {
    updateAuthUserInStorage(this.$store).then(() => {//get player
      this.player = this.$store.state.player;
      this.signUpTime = fromArrayToDate(this.player.signUpTime);
      if (this.player.photo && this.player.photo !== '') {
        axios.post("/api/player/image", {img_name: this.player.photo}, this.config).then((result) => {
          this.imgSrc = "data:image/;base64, " + result.data
          // let img = document.getElementById("player_photo");
          // img['src'] = this.imgSrc;
        }).catch(err => {
          console.log("ERR:");
          console.log(err)
        })
      }
    }).then(() => {
      this.getDialogs();
      this.subscribeOnNewMessages()
      if(this.$route.params.id){
        if(this.$route.params.id != this.player.id){
          this.newCompanionId = this.$route.params.id;
          this.getNewCompanion();
        }
      }
    })
    this.startOnlineListListener();
  },
  mounted() {
    this.addScrollListener();
  },
  methods: {
    getDialogs() {
      axios.get("/api/chat/info", {
        params: {
          player_id: this.player.id
        }
      }).then((res) => {
        for (let dialog of res.data) {
          this.addDialog(this.getCompanionFromDialog(dialog),[],this.dialogHasUnreadMessage(dialog),dialog.lastMessage.sendingTime);
        }
        this.sortDialogs();
        this.dialogsIsLoaded = true;
      }).then(() => {
        this.getPhotos();
      })
    },
    getPhotos() {
      let names = [];
      for (let dialog of this.dialogs) {
        names.push(dialog.companion.photo);
      }
      axios.post("/api/player/images", names).then((res) => {
        for (let i = 0; i < res.data.length; i++) {
          if (res.data[i] !== null) {
            res.data[i] = "data:image/;base64, " + res.data[i];
          }
          this.dialogs[i].companion.imageUrl = res.data[i];
        }
        this.photosIsLoaded = true;
      })
    },
    getCompanionFromDialog(dialog) {
      return dialog.player1.id === this.player.id ? dialog.player2 : dialog.player1
    },
    dialogHasUnreadMessage(dialog) {
      return dialog.lastMessage.sender.id !== this.player.id && dialog.lastMessage.readingTime == null;
    },
    compareDates(date1, date2) {
      for (let i = 0; i < date1.length; i++) {
        if (date1[i] !== date2[i]) {
          return date2[i] - date1[i];
        }
      }
      return 0;
    },
    openDialog(dialog) {
      this.companion = null;
      this.openingDialog = true;
      this.unreadMessageInCurrentDialog = false;
      if (dialog.messages.isLoaded) {
        this.messages = dialog.messages.messages;
        console.log("IN OPEN:")
        console.log(this.messages)
        console.log(dialog)
        this.companion = dialog.companion;
        setTimeout(()=>{
          this.scrollChat()
        },100)
        this.setReadLabelOnDialog(dialog);
      } else {
        axios.get("/api/chat/dialog", {
          params: {
            player1_id: dialog.companion.id,
            player2_id: this.player.id
          }
        }).then((res) => {
          this.messages = res.data.messages;
          dialog.messages.messages = res.data.messages;
          dialog.messages.isLoaded = true;
          this.companion = dialog.companion
          setTimeout(()=>{
            this.scrollChat()
          },100)
          this.setReadLabelOnDialog(dialog);
        })
      }
    },
    getStringDate(dateArray) {
      if(typeof(dateArray) === 'string'){
        dateArray = dateArray.replaceAll('-','.')
        return [dateArray.substring(8,10)+"."+dateArray.substring(5,7)+"."+dateArray.substring(0,4),dateArray.substring(11,19)]
      }
      let dateStr = fromArrayToDateWithTime(dateArray);
      return [dateStr.substring(0, 10), dateStr.substring(12, 21)]
    },
    sendMessage() {
      let messageText = this.messageInput;
      this.messageInput = "";

      axios.post("/api/chat/send", {
        sender_id: this.player.id,
        recipient_id: this.companion.id,
        message_text: messageText
      })
          .catch(err => console.log(err));

    },
    subscribeOnNewMessages(){
      this.$store.subscribe((mutation, state)=>{
        if(mutation.type === 'addNewMessage'){
          this.addNewMessages(state.newMessages);
        }
      });
    },
    addNewMessages(messages){
      console.log("messages:")
      console.log(messages)
      for(let message of messages){
        if(message.added){
          continue
        }
        this.setSenderAndRecipient(message).then(()=>{
          let fromCurrentCompanion = this.companion && this.companion.id == message.senderId;
          let toCurrentCompanion = this.companion && this.companion.id == message.recipientId;
          let added = false;
          for(let dialog of this.dialogs){
            if (dialog.companion.id == message.senderId){
                dialog.hasUnread = true;
              if(dialog.messages.isLoaded){
                dialog.messages.messages.push(message);
              }
              dialog.lastMessageTime = message.sendingTime
              added = true;
            } else if(dialog.companion.id == message.recipientId){
              console.log("DIALOG:")
              console.log(dialog)
              console.log("MESSAGE:")
              console.log(message)
              if(dialog.messages.isLoaded){
                dialog.lastMessageTime = message.sendingTime
              }
              dialog.messages.messages.push(message);
              added = true;
            }
          }
          if(!added){
            let companion = message.sender;
            this.addDialog(companion,[message], true, message.sendingTime)
          }
          if(fromCurrentCompanion){
            this.unreadMessageInCurrentDialog = true;
            for(let d of this.dialogs){
              if(d.companion.id == this.companion.id){
                this.setReadLabelOnDialog(d)
                return;
              }
            }
          }
          if(toCurrentCompanion){
            setTimeout(()=>{
              this.scrollChat();
            },10)
          }
          setTimeout(()=>{
            console.log("THIS MESSAGES 1")
            console.log(this.messages)
            this.insertLineBreaks(this.messages)
          },10)
          message.added = true;
          this.sortDialogs()
        })
      }
    },
    addScrollListener(){
      let interval = setInterval(()=>{
        let chat = this.$refs.chat;
        if(chat){
          chat.addEventListener("scroll",(event)=>{
            if(Math.abs(chat.scrollHeight - chat.scrollTop - chat.offsetHeight)<10){
              this.unreadMessageInCurrentDialog = false;
            }
          })
          clearInterval(interval)

        }
      }, 500)

    },
    setSenderAndRecipient(message){
      let sender = null
      let recipient = null
      if(message.senderId == this.player.id){
        sender = this.player;
      }else if(message.recipientId == this.player.id){
        recipient = this.player;
      }

      for(let dialog of this.dialogs){
        if(dialog.companion.id == message.recipientId){
          recipient = dialog.companion;
        }else if(dialog.companion.id == message.senderId){
          sender = dialog.companion;
        }
      }
      if(sender != null && recipient != null){
        return new Promise((resolve,reject)=>{
          message.sender = sender;
          message.recipient = recipient;
          resolve()
        })
      }else{
        if(sender == null){
          return axios.get("/api/player/"+message.senderId)
              .then(res=>{
                message.sender = res.data;
                message.recipient = recipient;
              })
        }else if(recipient == null){
          return axios.get("/api/player/"+message.recipient)
              .then(res=>{
                message.sender = sender;
                message.recipient = res.data;
              })
        }
      }
    },
    insertLineBreaks(messages){
      if(messages == null){
        return;
      }
      for(let message of messages){
        message.messageText = message.messageText.replace(/(.{80}(?!\s))/g, '$1\n');
      }
    },
    scrollChat(){
      this.unreadMessageInCurrentDialog = false;
      let chat = this.$refs.chat;
      chat.scrollTo(0,chat.scrollHeight);
    },
    addDialog(companion,messages,hasUnread,lastMessageTime, isLoaded=false){
      this.dialogs.push({
        companion: companion,
        messages: {messages: messages, isLoaded: isLoaded},
        hasUnread: hasUnread,
        lastMessageTime: lastMessageTime
      })
    },
    sortDialogs(){
      this.dialogs.sort((d1, d2) =>
      {
        if(!d1.lastMessageTime){
          return -1;
        }else if (!d2.lastMessageTime){
          return 1;
        }
        let d1Time = null
        let d2Time = null
        if(typeof (d1.lastMessageTime) == 'string'){
          d1Time = this.getArrayTimeFromString(d1);
        }else{
          d1Time = d1.lastMessageTime
        }
        if(typeof (d2.lastMessageTime) == 'string'){
          d2Time = this.getArrayTimeFromString(d2)
        }else{
          d2Time = d2.lastMessageTime;
        }
        return this.compareDates(d1Time,d2Time)
      })
    },
    getArrayTimeFromString(d){
      let a=[]
      a.push(parseInt(d.lastMessageTime.substring(0,4)))
      a.push(parseInt(d.lastMessageTime.substring(5,7)))
      a.push(parseInt(d.lastMessageTime.substring(8,10)))
      a.push(parseInt(d.lastMessageTime.substring(11,13)))
      a.push(parseInt(d.lastMessageTime.substring(14,16)))
      a.push(parseInt(d.lastMessageTime.substring(17,19)))
      a.push(parseInt(d.lastMessageTime.substring(20,24)))
      return a
    },
    setReadLabelOnDialog(dialog){
      if(!dialog.hasUnread) return
      let messages = dialog.messages.messages;
      let messagesToChange = []
      for(let message of messages){
        if(message.readingTime == null){
          messagesToChange.push(message);
        }
      }
      axios.post("/api/chat/set_reading_time",messagesToChange);
      dialog.hasUnread = false;
      console.log(dialog.messages)
    },
    startOnlineListListener(){
      setInterval(()=>{
        axios.get("/api/chat/online").then((res)=>{
          this.onlineIds = res.data;
          // console.log(this.onlineIds)
        })
      },1000)
    },
    isOnline(playerId){
      for(let id of this.onlineIds){
        if(id == playerId){
          return true;
        }
      }
      return false;
    },
    getNewCompanion(){
      let interval = setInterval(()=>{
        if(this.player && this.photosIsLoaded){
          axios.get("/api/chat/dialog",{params:{
              player1_id: this.player.id,
              player2_id: this.newCompanionId
            }}).then((res)=>{
              if(res.data.error){
                console.log(res.data.message)
              }else{
                let dialogCompanion = this.getCompanionFromDialog(res.data);
                let dialogByCompanion = this.getDialogByCompanionIfExists(dialogCompanion.id)
                if(dialogByCompanion){
                  // this.companion = dialogByCompanion.companion;
                  this.openDialog(dialogByCompanion)
                }else{
                  axios.post("/api/player/image", {img_name:dialogCompanion.photo}, this.config).then((res) => {
                    if(!res.data.error){
                      dialogCompanion.imageUrl ="data:image/;base64, " + res.data;
                    }
                    this.addDialog(dialogCompanion,[],false,null,true);
                    this.openDialog(this.getDialogByCompanionIfExists(dialogCompanion.id))
                    this.sortDialogs()
                  })
                }
              }
          })
          clearInterval(interval)
        }
      },100)
    },
    getDialogByCompanionIfExists(companionId){
      let dialog = null;
      this.dialogs.forEach((d)=>{
        if(d.companion.id == companionId){
          dialog = d;
        }
      })
      return dialog;
    }
  },
  watch:{
    messages(newMessages, oldMessages){
      this.messages = newMessages;
      console.log("THIS MESSAGES:")
      console.log(this.messages)
      this.insertLineBreaks(this.messages)
    },
    unreadMessageInCurrentDialog(newM,oldM){
      console.log(newM)
    }
  }
}
</script>

<style>
.companion {
  padding: 10px
}

.companion:hover {
  background: lightblue;
  color: white;
}

.companion:active{
  color:antiquewhite;
}

.chat-online {
  color: #34ce57
}


.chat-offline {
  color: #e4606d
}

.chat-messages {
  display: flex;
  flex-direction: column;
  height: 40rem;;
  overflow-y: scroll;
}

.chat-message-left,
.chat-message-right {
  display: flex;
  flex-shrink: 0
}

.chat-message-left {
  margin-right: auto
}

.chat-message-right {
  flex-direction: row-reverse;
  margin-left: auto
}

.py-3 {
  padding-top: 1rem !important;
  padding-bottom: 1rem !important;
}

.px-4 {
  padding-right: 1.5rem !important;
  padding-left: 1.5rem !important;
}

.flex-grow-0 {
  flex-grow: 0 !important;
}

.border-top {
  border-top: 1px solid #dee2e6 !important;
}

.card3 {
  width: 90%;
  margin: 0 5%;
  height: 40% !important;
}

.players {
  overflow-y: scroll;
  height: 40rem;
}

.loginInMessage {
  color: darkslateblue;
  font-family: "Bodoni MT";
}

.scroll-button{
  position: absolute;
  top: 20px;
  right:20px;
  width:50px;
  height:50px;
  border-radius: 50%;
  opacity:.6;
  z-index:1;
}

.selected{
  background: #addffa;
  color:white
}

</style>
