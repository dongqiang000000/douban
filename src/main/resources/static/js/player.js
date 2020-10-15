var played = document.querySelector('.played');//播放暂停
var audio = document.querySelector('.audio');
var remainingTime = document.querySelector('.remaining-time');//世间
var progress = document.querySelector('.progress');//进度条
var musicProgress = document.querySelector('.music-progress');
var nextSong = document.querySelector('.next-song');
var backSong = document.querySelector('.back-song');
var love = document.querySelector('.love');
var share = document.querySelector('.share');
var yiPing_share = document.querySelector('.yiPing-share');
var state = document.querySelector('.state');//播放状态



var erPing_progress = document.querySelector('.erPing-progress');//进度条
var erPing_music_progress = document.querySelector('.erPing-music-progress');
var erPing_music_time = document.querySelector('.erPing-music-time');
var erPing_back_song = document.querySelector('.erPing-back-song');
var erPing_next_song = document.querySelector('.erPing-next-song');
var erPing_play = document.querySelector('.erPing-play');
var erPing_love = document.querySelector('.erPing-love');
var erPing_song_control = document.querySelector('.erPing-song-control');


//播放状态
var control = 1;
state.addEventListener('click',songControl);
erPing_song_control.addEventListener('click',songControl);
function  songControl(){
    if(control == 1){
        state.style.background = `url("../images/state.png") no-repeat center/14px 12px`;
        erPing_song_control.src = "./images/state.png";
        control = 2;
        console.log(control);
    }else{
        state.style.background = `url("../images/song-control.png") no-repeat center/14px 12px`;
        erPing_song_control.src = "./images/song-control.png";
        control = 1;
        console.log(control);
    }
}
//分享
var f = true;
share.addEventListener('click',function(){
  if(f){
    yiPing_share.style.display='block';
    f = false;
  }else{
    yiPing_share.style.display='none';
    f = true;
  }

})

//点击红心
function loveSong(itemType,itemId){
url = '/fav?itemType='+itemType+"&itemId="+itemId;
  fetch(url)
  .then(function(res){
    return res.json();
  })
  .then(function(data){
    if(data.message == "notLogin") {
      window.location.href = '/login';
    }else if(data.message == "addSuccessful"){
      love.style.background = `url("../images/red-love.png") no-repeat center /28px 29px`;
      erPing_love.src = "./images/red-love.png";
    }else{
       love.style.background = `url("../images/love.png") no-repeat center  /28px 29px`;
       erPing_love.src = "./images/love.png";
    }
  })
}

 //播放、暂停
played.addEventListener('click',musicPlay);
erPing_play.addEventListener('click',musicPlay)
function musicPlay(){
   if(audio.paused){
      let timer = setInterval(time,1000);
      played.style.background=`url("../images/pause.png") no-repeat`;
      erPing_play.src="./images/pause.png";
      audio.play();
   }else{
      played.style.background=`url("../images/play.png") no-repeat center/22px 22px`;
      erPing_play.src="./images/play.png";
      audio.pause();
   }
}

//计时和进度条
function time() {
   var durationTime = audio.duration;//歌曲总时间
   let currentTime = audio.currentTime;//当前已播放时间
   let time = durationTime-currentTime;//歌曲剩余时间
   let minute = time/60;
   let minutes = parseInt(minute);
   if(minutes<10){
       minutes="0"+minutes;
   }
   let second = time%60;
   let seconds = Math.floor(second);
   if(seconds<10){
       seconds = "0"+seconds;
   }
   remainingTime.innerHTML = `-${minutes}:${seconds}`; //时间
   erPing_music_time.innerHTML = `-${minutes}:${seconds}`;
   progress.style.width = `${currentTime/durationTime*100}`+'%';//进度条
   erPing_music_progress.style.width = `${currentTime/durationTime*100}`+'%';
   if(time===0){
       console.log(control);
       if(control == 1){
           next_song();
       }
       if(control == 2){
           audio.currentTime = 0;
           audio.pause();
           audio.play();
       }
   }

}

//下一曲
nextSong.addEventListener('click',next_song);
erPing_next_song.addEventListener('click',next_song)
function next_song(){
     var a = 1;
     if(control==2){
         a=2;
     }
  url = '/nextSong';
  $('.refresh').load(url,null,function(){
       control = a;
       if(control == 2){
           state.style.background = `url("../images/state.png") no-repeat center/14px 12px`;
           erPing_song_control.src = "./images/state.png";
       }else{
           state.style.background = `url("../images/song-control.png") no-repeat center/14px 12px`;
           erPing_song_control.src = "./images/song-control.png";
       }
      let timer = setInterval(time,1000);
      played.style.background=`url("../images/pause.png") no-repeat`;
      erPing_play.src="./images/pause.png";
      audio.play();
  });
}
//上一曲
backSong.addEventListener('click',back_song)
erPing_back_song.addEventListener('click',back_song);
function back_song(){
     var a = 1;
     if(control==2){
         a=2;
     }
     url = '/backSong';
     $('.refresh').load(url,null,function(){
         control = a;
         if(control == 2){
             state.style.background = `url("../images/state.png") no-repeat center/14px 12px`;
             erPing_song_control.src = "./images/state.png";
         }else{
             state.style.background = `url("../images/song-control.png") no-repeat center/14px 12px`;
             erPing_song_control.src = "./images/song-control.png";
         }
         let timer = setInterval(time,1000);
         played.style.background=`url("../images/pause.png") no-repeat`;
         erPing_play.src="./images/pause.png";
         audio.play();
     });
}

//进度条快进
musicProgress.addEventListener('click',function(event){
    let x=event.offsetX;
    progress.style.width = `${x/500*100}`+'%';
    audio.currentTime = audio.duration*x/500;
});

erPing_progress.addEventListener('click',function(event){
    let x=event.offsetX;
    erPing_music_progress.style.width = `${x/390*100}`+'%';
    audio.currentTime = audio.duration*x/390;
});

//登录注册
var login = document.querySelector(".login");
var mask = document.querySelector(".mask");
var model = document.querySelector(".model");
var modelCross = document.querySelector(".model-cross");
login.addEventListener('click',function(){
      window.location.href = "/login";
});
modelCross.addEventListener('click',function(){
    mask.style.display = 'none';
    model.style.display='none';
});


var middle_login_point = document.querySelector(".middle-login-point");
middle_login_point.addEventListener('click',function(){
      window.location.href = "/login";
});
