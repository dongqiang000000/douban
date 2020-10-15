const all_songs_container_return = document.querySelector('.all-songs-container-return');

all_songs_container_return.addEventListener('click',function(){
  history.go(-1);
});

const cross = document.querySelector('.cross');
cross.addEventListener('click',function(){
  history.go(-1);
});
