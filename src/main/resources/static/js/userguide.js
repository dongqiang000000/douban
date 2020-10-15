const loves = document.querySelectorAll('.love');
const avatarBackgrounds = document.querySelectorAll('.avatar-background');
const avatarSelecteds = document.querySelectorAll('.avatar-selected');
const selectedSingerAvatars = document.querySelectorAll('.selected-singer-avatar');
const change = document.querySelector('.change');
const singerInfo = document.querySelector('.singer-info');

//登录
const headerLogin = document.querySelector('.header-login');
headerLogin.addEventListener('click',function(){
  window.location.href = "/login";
})

//点击喜欢
let num = 0;
let arraySingerAvatar = new Array();
let arraySingerId = [];
function loveAdd(i) {
    if (num<5){
        let x = document.querySelectorAll(`.avatar-background-${i}`);
        x[3].onclick = '';
        x[1].style.display='block';
        x[2].style.display='block';
        arraySingerAvatar[num] = x[0].getAttribute('src');
        arraySingerId[num] = i;
        avatarAdd();
        num++;
    }
}

for (let i=0;i<5;i++) {
    selectedSingerAvatars[i].addEventListener('click',function(){
        if (arraySingerId[i]!=null) {
            let x = document.querySelectorAll(`.avatar-background-${arraySingerId[i]}`);
            if(x.length>0){
                x[1].style.display='none';
                x[2].style.display='none';
                x[3].setAttribute("onclick",`loveAdd(${arraySingerId[i]})`);
            }
            arraySingerId.splice(i,1);
            arraySingerAvatar.splice(i,1);
            num--;
            avatarAdd();
            avatarDelete();
        }
    })
}

function avatarDelete() {
    for (let j=num;j<=4;j++) {
        selectedSingerAvatars[j].style.background = `url("../images/selected-singer.png") no-repeat center/50px 50px`;
    }
}

function avatarAdd() {
    for (let j=0;j<=num;j++) {
        selectedSingerAvatars[j].style.background = `url(${arraySingerAvatar[j]}) no-repeat center/50px 50px`;
    }
}

//自己搜
$('.select').on("click",function(){
    window.location.href = "/search";
});

//换一批歌手
change.addEventListener('click',function(){
    let url = '/singer-info';
    $('.singer-info').load(url);
});

//跳过去听歌
const skip = document.querySelector('.skip');
skip.addEventListener('click',function(){
    window.location.href = '/index';
})




