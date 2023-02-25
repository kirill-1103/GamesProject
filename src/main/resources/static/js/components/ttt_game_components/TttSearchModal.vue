<template>
  <div style="width:100%;height: 100%;" ref="modal" class="modal" id="tttSearchModal" role="dialog"  tabindex="-1" aria-labelledby="mySmallModalLabel" aria-hidden="true">
    <div class="modal-dialog">
      <div class="modal-content" >
          <div style="margin:auto;margin-top:10px"  class="spinner-border text-primary" role="status">
            <span class="visually-hidden">Loading..</span>
          </div>
        <h3 ref='search' style="margin:auto">Поиск игры...</h3>
        <button ref='buttonStop' style="width:80%;margin:auto;margin-bottom: 20px;margin-top:5px;" data-bs-dismiss="modal" @click="stopSearch">Отмена</button>

      </div>
    </div>
  </div>
</template>


<script>
export default {
  name: "TttSearchModal",
  props: ['player','stopSearch','gameStarting'],
  data:function() {
    return {
      count:0
    }
  },
  created() {

  },
  mounted() {
    let interval = setInterval(()=>{
      try{
        this.$refs.search.innerText = 'Поиск игры';
        this.count = (this.count)%3 + 1;
        for(let i = 0;i<this.count;i++){
          this.$refs.search.innerText += '.';
        }
      }catch(e){
        clearInterval(interval);
      }
    },400)
    // this.$refs.modal.show({
    //   backdrop:false
    // })
    document.addEventListener("click",(ev)=>{
      if(ev.target.id === 'tttSearchModal'){
        this.stopSearch();
      }
    })

  },
  watch:{
    gameStarting(oldv,newv){
      if(newv){
        this.$refs.buttonStop.click();
      }
    }
  }
}
</script>

<style>

</style>