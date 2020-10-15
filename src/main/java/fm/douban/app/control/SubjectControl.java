package fm.douban.app.control;

import fm.douban.model.Favorite;
import fm.douban.model.Singer;
import fm.douban.model.Song;
import fm.douban.model.Subject;
import fm.douban.service.FavoriteService;
import fm.douban.service.SingerService;
import fm.douban.service.SongService;
import fm.douban.service.SubjectService;
import fm.douban.util.FavoriteUtil;
import fm.douban.util.SubjectUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class SubjectControl {

    @Autowired
    private SubjectService subjectService;
    @Autowired
    private SingerService singerService;
    @Autowired
    private SongService songService;
    @Autowired
    private FavoriteService favoriteService;
    @Autowired
    private MongoTemplate mongoTemplate;

    //歌曲详情
    @GetMapping("/artist")
    public String mhzDetail(Model model, @RequestParam(name="subjectId")String subjectId) {

        //主题
        Subject subject = subjectService.get(subjectId);
        //歌手
        String singerId = subject.getMaster();
        Singer singer = singerService.get(singerId);
        //相似歌手
        List<String> simSingerIds = singer.getSimilarSingerIds();
        List<Singer> simSingers = new ArrayList<>();
        if (simSingerIds!=null){
            for (String simSingerId : simSingerIds) {
                Singer simSinger = singerService.get(simSingerId);
                simSingers.add(simSinger);
            }
        }
        //歌曲
        Query query = new Query(Criteria.where("singerIds").regex(singerId));
        List<Song> songs = mongoTemplate.find(query,Song.class);

        Favorite favorite = new Favorite();
        favorite.setItemType(FavoriteUtil.ITEM_TYPE_SINGER);
        favorite.setItemId(singerId);
        List<Favorite> favorites = favoriteService.list(favorite);

        model.addAttribute("favorites",favorites);
        model.addAttribute("subject",subject);
        model.addAttribute("singer",singer);
        model.addAttribute("simSingers",simSingers);
        model.addAttribute("songs",songs);
        return "mhzdetail";
    }


    //歌单列表
    @GetMapping("/collection")
    public String collection(@NotNull Model model) {
        List<Subject> subjects = subjectService.getSubjects(SubjectUtil.TYPE_COLLECTION);
        List<List<Song>> lists = new ArrayList<>();
        List<Singer> singers = new ArrayList<>();
        for (Subject subject : subjects) {
            List<Song> songs = new ArrayList<>();
            Singer singer = singerService.get(subject.getMaster());
            singers.add(singer);
            List<String> songIds = subject.getSongIds();
            if (!songIds.isEmpty()) {
                for (String songId : songIds) {
                    if (songService.get(songId) != null) {
                        songs.add(songService.get(songId));
                    }
                }
            }
            lists.add(songs);
        }

        model.addAttribute("lists",lists);
        model.addAttribute("subjects",subjects);
        model.addAttribute("singers",singers);
        return "collection";
    }

    //歌单详情
    @GetMapping("/collectiondetail")
    public  String collectionDetail(Model model,@RequestParam(name="subjectId") String subjectId) {
        Subject subject = subjectService.get(subjectId);

        Singer singer = singerService.get(subject.getMaster());

        List<String> songIds = subject.getSongIds();
        List<Song> songs = new ArrayList<>();
            for (String songId : songIds){
                Song song = songService.get(songId);
                if (song != null){
                    songs.add(song);
                }

            }


        List<Subject> otherSubjects = subjectService.getSubjects(subject);

        model.addAttribute("subject",subject);
        model.addAttribute("singer",singer);
        model.addAttribute("songs",songs);
        model.addAttribute("otherSubjects",otherSubjects);
        System.out.println(songs);
        return "collectiondetail";
    }
}
