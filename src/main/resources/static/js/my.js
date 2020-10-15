//主页
const mhz = document.querySelector('.mhz');
mhz.addEventListener("click",function(){
  window.location.href = "/index";
})

//歌单页面
const songList = document.querySelector('.songList');
songList.addEventListener("click",function(){
  window.location.href = '/collection';
})