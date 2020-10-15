const search = document.querySelector('.search');
const singer = document.querySelector('.singer');
const cancel = document.querySelector('.cancel');

//取消
cancel.addEventListener('click',function(){
    history.go(-1);
})

//
search.addEventListener('input',function(){
    let url = '/searchContent?keyword='+search.value;
    fetch(url)
    .then(function(response){
         return response.json();
    })
    .then(function(myJson){
        singer.innerHTML = '';
        let singers = myJson.singers;
        if (singers.length>=5){
            for (let i=0;i<5;i++) {
               const li = createItem(singers[i]);
               singer.appendChild(li);
            }
        }else {
            for (let i=0;i<=singers.length;i++) {
               const li = createItem(singers[i]);
               singer.appendChild(li);
            }
        }
    })
});

function createItem(item) {
    const li = document.createElement('li');
    li.innerHTML = `
    <img src = "${item.avatar}">
    <p>${item.name}</p>
    `;
    return li;
}