package fm.douban.app.control;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import fm.douban.service.SingerService;
import fm.douban.model.Singer;

import java.util.List;

@RestController
public class SingerTestControl {

    @Autowired
    private SingerService singerService;

    @GetMapping("/test/singer/add")
    public Singer testAddSinger() {
        Singer singer = new Singer();
        singer.setId("0");
        Singer singer1 = singerService.addSinger(singer);
        return singer1;
    }
    @GetMapping("/test/singer/getAll")
    public List<Singer> testGetAll(){
        List<Singer> singers = singerService.getAll();
        return singers;
    }
    @GetMapping("/test/singer/getOne")
    public Singer testGetSinger(){
        return singerService.get("0");
    }
    @GetMapping("/test/singer/modify")
    public boolean testModifySinger(){
        Singer singer = new Singer();
        singer.setId("0");
        singer.setName("xx");
        boolean b = singerService.modify(singer);
        return b;
    }
    @GetMapping("/test/singer/del")
    public boolean testDelSinger(){
        return singerService.delete("0");
    }
    @GetMapping("/test/singer/delAll")
    public boolean testDelAll(){
        return singerService.deleteAll();
    }
}
