
const subjectName = document.querySelector('.subject-name');

//歌单详情
function collectionDetail(subjectId) {
    let url = "/collectiondetail?subjectId="+subjectId;
    window.location.href=url;
}

//主页
const mhz = document.querySelector('.mhz');
mhz.addEventListener("click",function(){
  window.location.href = "/index";
})

//我的页面
const my = document.querySelector('.my');
my.addEventListener("click",function(){
  window.location.href = "/my";
})