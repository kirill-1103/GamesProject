<template>
  <div class="div_chat">
    <div class="container " style="margin:0;width: 503px;height:503px">
      <div style="margin:0;margin-left:-12px;width: 503px;height:503px" class="card card-bordered">
        <div class="card-header">
          <h4 class="card-title"><strong>Chat</strong></h4>
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
                <p class="meta" style="color:gray;font-size: 13px"><time datetime="2023">(Me){{message.time}}</time></p>

              </div>
            </div>
          </div>

          <div class="ps-scrollbar-x-rail" style="left: 0px; bottom: 0px;"><div class="ps-scrollbar-x" tabindex="0" style="left: 0px; width: 0px;"></div></div><div class="ps-scrollbar-y-rail" style="top: 0px; height: 0px; right: 2px;"><div class="ps-scrollbar-y" tabindex="0" style="top: 0px; height: 2px;"></div></div></div>

      </div>
    </div>


  </div>
</template>

<script>
export default{
  name:'ReplayChatComponent',
  props:['player','messages'],
  data:function() {
    return {
      config: {
        headers: {
          'Content-Type': 'application/json',
          'Accept': 'application/json'
        }
      },
    }
  },
  methods:{
    isMe(message){
      return message.senderId === this.player;
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

</style>