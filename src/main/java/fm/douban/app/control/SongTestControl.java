package fm.douban.app.control;

import fm.douban.model.Song;
import fm.douban.param.SongQueryParam;
import fm.douban.service.SongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SongTestControl {

    @Autowired
    private SongService songService;

    @GetMapping("/test/song/add")
    public Song testAdd(){
        Song song = new Song();
        song.setId("0");
        Song song1 = songService.add(song);
        return song1;
    }
    @GetMapping("/test/song/get")
    public Song testGet(){
        return songService.get("0");
    }
    @GetMapping("/test/song/list")
    public Page<Song> testList(){
        SongQueryParam songQueryParam = new SongQueryParam();
        songQueryParam.setPageNum(1);
        Page<Song> songs = songService.list(songQueryParam);
        return  songs;
    }
    @GetMapping("/test/song/modify")
    public boolean testModify(){
        Song song = new Song();
        song.setId("0");
        song.setName("xxxx");
        return songService.modify(song);
    }
    @GetMapping("/test/song/del")
    public boolean testDelete(){
        return songService.delete("0");
    }
}
