const register = document.querySelector('.register');
const enterBtn = document.querySelector('.enter-btn');
const name = document.querySelector('.name');
const message = document.querySelector('.message');
const password = document.querySelector('.password');

//跳转注册页面
register.addEventListener('click',function(){
    window.location.href = "/sign";
})

//提交登录信息
enterBtn.addEventListener('click',function(){
    fetch("/authenticate",
       {
         method: 'post',
         body:`name=${name.value}&password=${password.value}`,
         headers:{
            "Content-Type": "application/x-www-form-urlencoded;charset=utf-8"
//           'content-type': 'application/json'
         }
       }
    )
    .then(function(response){
      return response.json();
    })
    .then(function(myjson){
    if (JSON.stringify(myjson) != "{}"){
        if(myjson.message == "登录成功"){
            window.location.href = "/index"
        }else{
            message.innerHTML = `${myjson.message}`;
        }
    }
    })
})