var allSongs = document.querySelector('.all-songs');
var allSongsContainer = document.querySelector('.all-songs-container');
var subjectInfo = document.querySelector('.subject-info');

//喜欢
const clickLove = document.querySelector('.love');
const conte = document.querySelector('.conte');
function love(itemType,singerId){
    url = '/fav?itemType='+itemType+"&itemId="+singerId;
    fetch(url)
    .then((res) => res.json())
    .then(function(data){
      if(data.message == "notLogin") {
            window.location.href = '/login';
          }else if(data.message == "addSuccessful"){
          console.log(clickLove.innerText);
            conte.innerText = `${parseInt(conte.innerText)+1}`;
            clickLove.style.background = 'pink';
          }else{
          console.log("xxx"+clickLove.innerText);
             conte.innerText = `${parseInt(conte.innerText)-1}`;
             clickLove.style.background = '';
          }
    })
}
//love.addEventListener('click',function(){
//    let url =
//    fetch(url)
//    .then((res) => res.json())
//    .then(function(data){
//      if(data.message == "notLogin") {
//            window.location.href = '/login';
//          }
//          if(data.message == "addSuccessful"){
//            love.innerText = `${love.innerText+1}`;
//          }else{
//             love.innerText = `${love.innerText-1}`;
//          }
//    })
//})



//查看所有歌曲
allSongs.addEventListener('click',function(){
    allSongsContainer.style.display = 'block';
    subjectInfo.style.display = 'none';
})

//从所有歌曲返回
var allSongsContainerReturn = document.querySelector('.all-songs-container-return');
allSongsContainerReturn.addEventListener('click',function(){
    allSongsContainer.style.display = 'none';
    subjectInfo.style.display = 'block';
})

//回到首页
const Return = document.querySelector('.return');
Return.addEventListener('click',function(){
    history.go(-1);
})

//回到首页
const cross = document.querySelector('.cross');
cross.addEventListener('click',function(){
    history.go(-1);
})