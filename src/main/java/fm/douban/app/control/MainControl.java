package fm.douban.app.control;

import com.mongodb.client.result.DeleteResult;
import fm.douban.model.*;
import fm.douban.param.SongQueryParam;
import fm.douban.service.FavoriteService;
import fm.douban.service.SingerService;
import fm.douban.service.SongService;
import fm.douban.service.SubjectService;
import fm.douban.util.FavoriteUtil;
import fm.douban.util.SubjectUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
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
public class MainControl {

    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private SongService songService;
    @Autowired
    private SingerService singerService;
    @Autowired
    private SubjectService subjectService;
    @Autowired
    private FavoriteService favoriteService;

    @GetMapping("/index")
    public String index(Model model, HttpSession session){
        int PAGENUM= 17;
        if(session.getAttribute("PAGENUM")!=null)
            PAGENUM=(Integer)session.getAttribute("PAGENUM");
        else {
            session.setAttribute("PAGENUM",PAGENUM);
        }
//        List l = mongoTemplate.findAll(Song.class);
        if(PAGENUM<=0) {
            PAGENUM=5024;
            session.setAttribute("PAGENUM",PAGENUM);
        }
        if(PAGENUM>5024) {
            PAGENUM=1;
            session.setAttribute("PAGENUM",PAGENUM);
        }
        SongQueryParam songQueryParam = new SongQueryParam();
        songQueryParam.setPageNum(PAGENUM);
        songQueryParam.setPageSize(1);
        Page<Song> songs = songService.list(songQueryParam);
        for (Song song : songs){
            if(song.getName()==null) {
                return "error";
            }
            List<String> singerIds = song.getSingerIds();
            List<Singer> singers = new ArrayList<>();
            for (String singerId :singerIds){
                Singer singer = singerService.get(singerId);
                if(singer!=null){
                    singers.add(singer);
                }
            }
            model.addAttribute("song",song);
            model.addAttribute("singers",singers);
        }
        List<Subject> mhzSubjects = subjectService.getSubjects(SubjectUtil.TYPE_MHZ);
        List<Subject> artistDatas = new ArrayList<>();
        List<Subject> moodDatas = new ArrayList<>();
        List<Subject> ageDatas = new ArrayList<>();
        List<Subject> styleDatas = new ArrayList<>();
        for (Subject mhzSubject : mhzSubjects){
            if (mhzSubject.getSubjectSubType().equals(SubjectUtil.TYPE_SUB_ARTIST)){
                artistDatas.add(mhzSubject);
            }else if (mhzSubject.getSubjectSubType().equals(SubjectUtil.TYPE_SUB_MOOD)){
                moodDatas.add(mhzSubject);
            }else if (mhzSubject.getSubjectSubType().equals(SubjectUtil.TYPE_SUB_AGE)){
                ageDatas.add(mhzSubject);
            }else {
                styleDatas.add(mhzSubject);
            }
        }
        List<MhzViewModel> mhzViewModels = new ArrayList<>();
        MhzViewModel mhzViewModel = new MhzViewModel();
        mhzViewModel.setTitle("心情 / 场景");
        mhzViewModel.setSubjects(moodDatas);
        mhzViewModels.add(mhzViewModel);

        mhzViewModel = new MhzViewModel();
        mhzViewModel.setTitle("语言 / 年代");
        mhzViewModel.setSubjects(ageDatas);
        mhzViewModels.add(mhzViewModel);

        mhzViewModel = new MhzViewModel();
        mhzViewModel.setTitle("风格 / 流派");
        mhzViewModel.setSubjects(styleDatas);
        mhzViewModels.add(mhzViewModel);
        model.addAttribute("artistDatas",artistDatas);
        model.addAttribute("mhzViewModels",mhzViewModels);
        return "index.html";
    }

    //搜索页
    @GetMapping("/search")
    public String search(Model model) {
        return "search";
    }

    //搜索结果
    @GetMapping("/searchContent")
    @ResponseBody
    public Map searchContent(@RequestParam(name = "keyword") String keyword) {
        if (StringUtils.hasText(keyword)){
            Query query = new Query(Criteria.where("name").regex(keyword));
            List<Singer> singers = mongoTemplate.find(query,Singer.class);
            if (singers.isEmpty()){
                return null;
            }else {
                Map<String,List<Singer>> map = new HashMap<>();
                map.put("singers",singers);
                return map;
            }
        }else {
            return null;
        }
    }
    
    //我的页面
    @GetMapping("/my")
    public String myPage(Model model, HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        if ((String) session.getAttribute("userLoginInfo") == null){
            return "login";
        }
        System.out.println("我的id："+session.getAttribute("userLoginInfo"));
        Favorite favorite = new Favorite();
        favorite.setUserId((String) session.getAttribute("userLoginInfo"));
        favorite.setItemType(FavoriteUtil.ITEM_TYPE_SONG);
        List<Favorite> favorites = favoriteService.list(favorite);
        List<Song> songs = new ArrayList<>();
        if (!favorites.isEmpty()){
            for (Favorite favorite1 :favorites) {
                Song song = songService.get(favorite1.getItemId());
                if (song!=null){
                    songs.add(song);
                }
            }
        }
        model.addAttribute("favorites",favorites);
        model.addAttribute("songs",songs);
        return "my";
    }

    //喜欢或不喜欢操作
    @GetMapping("/fav")
    @ResponseBody
    public Map doFav(@RequestParam(name="itemType") String itemType,@RequestParam(name= "itemId")String itemId,HttpServletRequest request, HttpServletResponse response){
        Map<String,String> map = new HashMap<>();
        HttpSession session = request.getSession();
        if ((String) session.getAttribute("userLoginInfo") == null){
            map.put("message","notLogin");
            return map;
        }

        Favorite favorite = new Favorite();
        favorite.setUserId((String) session.getAttribute("userLoginInfo"));
        favorite.setItemType(itemType);
        favorite.setItemId(itemId);
        List<Favorite> favorites = favoriteService.list(favorite);
        if (favorites.size()>0) {
            boolean b = favoriteService.delete(favorites.get(0));
            map.put("message","delSuccessful");
        }else {
            Favorite fav = favoriteService.add(favorite);
            map.put("message","addSuccessful");
        }
        return map;
    }

    //下一曲
    @GetMapping("/nextSong")
    public String flash(Model model, HttpSession session){
        int PAGENUM= 17;
        if(session.getAttribute("PAGENUM")!=null){
             PAGENUM=(Integer)session.getAttribute("PAGENUM");
            PAGENUM++;
            session.setAttribute("PAGENUM", PAGENUM);
        }
//        List l = mongoTemplate.findAll(Song.class);
        if(PAGENUM<=0) {
            PAGENUM=5024;
            session.setAttribute("PAGENUM",PAGENUM);
        }
        if(PAGENUM>5024) {
            PAGENUM=1;
            session.setAttribute("PAGENUM",PAGENUM);
        }
        SongQueryParam songQueryParam = new SongQueryParam();
        songQueryParam.setPageNum(PAGENUM);
        songQueryParam.setPageSize(1);
        Page<Song> songs = songService.list(songQueryParam);
        for (Song song : songs){
            if(song.getName()==null) {
                return "error";
            }
            List<String> singerIds = song.getSingerIds();
            List<Singer> singers = new ArrayList<>();
            for (String singerId :singerIds){
                Singer singer = singerService.get(singerId);
                if(singer!=null){
                    singers.add(singer);
                }
            }
            model.addAttribute("song",song);
            model.addAttribute("singers",singers);
        }
        List<Subject> mhzSubjects = subjectService.getSubjects(SubjectUtil.TYPE_MHZ);
        List<Subject> artistDatas = new ArrayList<>();
        List<Subject> moodDatas = new ArrayList<>();
        List<Subject> ageDatas = new ArrayList<>();
        List<Subject> styleDatas = new ArrayList<>();
        for (Subject mhzSubject : mhzSubjects){
            if (mhzSubject.getSubjectSubType().equals(SubjectUtil.TYPE_SUB_ARTIST)){
                artistDatas.add(mhzSubject);
            }else if (mhzSubject.getSubjectSubType().equals(SubjectUtil.TYPE_SUB_MOOD)){
                moodDatas.add(mhzSubject);
            }else if (mhzSubject.getSubjectSubType().equals(SubjectUtil.TYPE_SUB_AGE)){
                ageDatas.add(mhzSubject);
            }else {
                styleDatas.add(mhzSubject);
            }
        }
        List<MhzViewModel> mhzViewModels = new ArrayList<>();
        MhzViewModel mhzViewModel = new MhzViewModel();
        mhzViewModel.setTitle("心情 / 场景");
        mhzViewModel.setSubjects(moodDatas);
        mhzViewModels.add(mhzViewModel);

        mhzViewModel = new MhzViewModel();
        mhzViewModel.setTitle("语言 / 年代");
        mhzViewModel.setSubjects(ageDatas);
        mhzViewModels.add(mhzViewModel);

        mhzViewModel = new MhzViewModel();
        mhzViewModel.setTitle("风格 / 流派");
        mhzViewModel.setSubjects(styleDatas);
        mhzViewModels.add(mhzViewModel);
        model.addAttribute("artistDatas",artistDatas);
        model.addAttribute("mhzViewModels",mhzViewModels);
        return "index::refresh";
    }

    //上一曲
    @GetMapping("/backSong")
    public String back(Model model, HttpSession session){
        int PAGENUM= 17;
        if(session.getAttribute("PAGENUM")!=null){
            PAGENUM=(Integer)session.getAttribute("PAGENUM");
            PAGENUM--;
            session.setAttribute("PAGENUM", PAGENUM);
        }
//        List l = mongoTemplate.findAll(Song.class);
        if(PAGENUM<=0) {
            PAGENUM=5024;
            session.setAttribute("PAGENUM",PAGENUM);
        }
        if(PAGENUM>5024) {
            PAGENUM=1;
            session.setAttribute("PAGENUM",PAGENUM);
        }
        SongQueryParam songQueryParam = new SongQueryParam();
        songQueryParam.setPageNum(PAGENUM);
        songQueryParam.setPageSize(1);
        Page<Song> songs = songService.list(songQueryParam);
        for (Song song : songs){
            if(song.getName()==null) {
                return "error";
            }
            List<String> singerIds = song.getSingerIds();
            List<Singer> singers = new ArrayList<>();
            for (String singerId :singerIds){
                Singer singer = singerService.get(singerId);
                if(singer!=null){
                    singers.add(singer);
                }
            }
            model.addAttribute("song",song);
            model.addAttribute("singers",singers);
        }
        List<Subject> mhzSubjects = subjectService.getSubjects(SubjectUtil.TYPE_MHZ);
        List<Subject> artistDatas = new ArrayList<>();
        List<Subject> moodDatas = new ArrayList<>();
        List<Subject> ageDatas = new ArrayList<>();
        List<Subject> styleDatas = new ArrayList<>();
        for (Subject mhzSubject : mhzSubjects){
            if (mhzSubject.getSubjectSubType().equals(SubjectUtil.TYPE_SUB_ARTIST)){
                artistDatas.add(mhzSubject);
            }else if (mhzSubject.getSubjectSubType().equals(SubjectUtil.TYPE_SUB_MOOD)){
                moodDatas.add(mhzSubject);
            }else if (mhzSubject.getSubjectSubType().equals(SubjectUtil.TYPE_SUB_AGE)){
                ageDatas.add(mhzSubject);
            }else {
                styleDatas.add(mhzSubject);
            }
        }
        List<MhzViewModel> mhzViewModels = new ArrayList<>();
        MhzViewModel mhzViewModel = new MhzViewModel();
        mhzViewModel.setTitle("心情 / 场景");
        mhzViewModel.setSubjects(moodDatas);
        mhzViewModels.add(mhzViewModel);

        mhzViewModel = new MhzViewModel();
        mhzViewModel.setTitle("语言 / 年代");
        mhzViewModel.setSubjects(ageDatas);
        mhzViewModels.add(mhzViewModel);

        mhzViewModel = new MhzViewModel();
        mhzViewModel.setTitle("风格 / 流派");
        mhzViewModel.setSubjects(styleDatas);
        mhzViewModels.add(mhzViewModel);
        model.addAttribute("artistDatas",artistDatas);
        model.addAttribute("mhzViewModels",mhzViewModels);
        return "index::refresh";
    }
}


