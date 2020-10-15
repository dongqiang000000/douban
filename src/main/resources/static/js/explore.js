var artistCover = document.querySelectorAll(".artist-cover");
var artistPlay = document.querySelectorAll(".artist-play");
var myMhzPoint = document.querySelector(".my-mhz-point");
var moreMhz = document.querySelector(".more-mhz");
var artistList = document.querySelector(".artist-list");
//var flash = document.querySelector('.flash');
//var erPingMain = document.querySelector('.erPing-main')
var erPingSongList = document.querySelector('.erPing-songList');
var erPingMy = document.querySelector('.erPing-my');

//我的页面
erPingMy.addEventListener('click',function(){
    window.location.href = '/my';
})

erPingSongList.addEventListener('click',function(){
    window.location.href = '/collection';
})
//主题系列
function clickArtistName(subjectId){
    let url = "/artist?subjectId="+subjectId;
    window.location.href = url;
}
//从主题系列返回
//cross.addEventListener('click',function(){
//    erPingMain.style.display = "block";
//    mhzdetail.style.display = "none";
//})

//鼠标悬停头像换背景
for (let i = 0 ;i<artistCover.length;i++) {
    artistCover[i].addEventListener("mouseover",function(){
        artistPlay[i].style.display = "block";
    });
}
for (let i = 0 ;i<artistCover.length;i++) {
    artistCover[i].addEventListener("mouseleave",function(){
        artistPlay[i].style.display = "none";
    });
}
for (let i = 0 ;i<artistCover.length;i++) {
    artistPlay[i].addEventListener("mouseover",function(){
        artistPlay[i].style.display = "block";
    });
}

//我的私人兆赫
myMhzPoint.addEventListener('click',function(){
    window.location.href="/user-guide";
});

//加载更多兆赫
moreMhz.addEventListener('click',function(){
    artistList.style.height="990px";
    moreMhz.style.display='none';
});