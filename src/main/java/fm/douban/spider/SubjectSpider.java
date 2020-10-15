package fm.douban.spider;

import com.alibaba.fastjson.JSON;
import fm.douban.model.Singer;
import fm.douban.model.Song;
import fm.douban.service.SongService;
import fm.douban.util.HttpUtil;
import fm.douban.util.SubjectUtil;
import fm.douban.model.Subject;
import fm.douban.service.SingerService;
import fm.douban.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class SubjectSpider {
    @Autowired
    private SubjectService subjectService;
    @Autowired
    private SingerService singerService;
    @Autowired
    private SongService songService;

    //@PostConstruct
    public void init(){
        System.out.println("启动");
        doExcute();
    }


    public void doExcute(){
        getSubjectData();
        getCollectionsData();
    }

    private void getSubjectData(){
        String url = "https://douban.fm/j/v2/rec_channels?specific=all";
        Map<String,String> headers = new HashMap<>();
        System.out.println(1);
        String content = HttpUtil.getContent(url,headers);
        Map returnData = JSON.parseObject(content,Map.class);
        Map data = (Map)returnData.get("data");
        Map sourceData = (Map)data.get("channels");
        List<Map> artist = (List)sourceData.get("artist");
        List<Map> age = (List)sourceData.get("language");
        List<Map> mood = (List)sourceData.get("scenario");
        List<Map> style = (List)sourceData.get("genre");

        for (Map artist1 : artist) {
            if (subjectService.get(""+artist1.get("id"))==null) {
                Subject subject = new Subject();
                subject.setId("" + artist1.get("id"));
                subject.setName((String) artist1.get("name"));
                subject.setCover((String) artist1.get("cover"));
                subject.setMaster(""+artist1.get("artist_id"));
                subject.setDescription((String) artist1.get("rec_reason"));
                subject.setSubjectType(SubjectUtil.TYPE_MHZ);
                subject.setSubjectSubType(SubjectUtil.TYPE_SUB_ARTIST);
                subjectService.addSubject(subject);
            }

            List<Map> related_artists = (List) artist1.get("related_artists");
            for (Map related_artist : related_artists) {
                if (singerService.get((String)related_artist.get("id")) == null){
                    Singer singer = new Singer();
                    singer.setId((String)related_artist.get("id"));
                    singer.setName((String)related_artist.get("name"));
                    singer.setAvatar((String)related_artist.get("cover"));
                    Singer singer1 = singerService.addSinger(singer);
                }
            }
            getSubjectSongData("" + artist1.get("id"));
        }

        for (Map age1 : age){
            if (subjectService.get(""+age1.get("id"))==null) {
                Subject subject = new Subject();
                subject.setId("" + age1.get("id"));
                subject.setName((String) age1.get("name"));
                subject.setCover((String) age1.get("cover"));
                subject.setDescription((String) age1.get("intro"));
                subject.setSubjectType(SubjectUtil.TYPE_MHZ);
                subject.setSubjectSubType(SubjectUtil.TYPE_SUB_AGE);
                subjectService.addSubject(subject);
            }
            getSubjectSongData("" + age1.get("id"));
        }

        for (Map mood1 : mood){
            if (subjectService.get(""+mood1.get("id"))==null) {
                Subject subject = new Subject();
                subject.setId("" + mood1.get("id"));
                subject.setName((String) mood1.get("name"));
                subject.setCover((String) mood1.get("cover"));
                subject.setDescription((String) mood1.get("intro"));
                subject.setSubjectType(SubjectUtil.TYPE_MHZ);
                subject.setSubjectSubType(SubjectUtil.TYPE_SUB_MOOD);
                subjectService.addSubject(subject);
            }
            getSubjectSongData("" + mood1.get("id"));

        }

        for (Map style1 : style){
            if (subjectService.get(""+style1.get("id"))==null) {
                Subject subject = new Subject();
                subject.setId("" + style1.get("id"));
                subject.setName((String) style1.get("name"));
                subject.setCover((String) style1.get("cover"));
                subject.setDescription((String) style1.get("intro"));
                subject.setSubjectType(SubjectUtil.TYPE_MHZ);
                subject.setSubjectSubType(SubjectUtil.TYPE_SUB_STYLE);
                subjectService.addSubject(subject);
            }
            getSubjectSongData("" + style1.get("id"));

        }
    }

    private void getSubjectSongData(String subjectId){
        String url = "https://douban.fm/j/v2/playlist?channel="+subjectId+"&kbps=128&client=s%3Amainsite%7Cy%3A3.0&app_name=radio_website&version=100&type=n";
        Map<String,String> headers = new HashMap<>();
        System.out.println(0);
        String content = HttpUtil.getContent(url,headers);
        Map returnData = JSON.parseObject(content,Map.class);
        List<Map> songData = (List) returnData.get("song");
        for (Map song : songData) {
            if (songService.get((String)song.get("sid"))==null) {
                List<Map> singers = (List) song.get("singers");
                List<String> singerIds = new ArrayList<>();
                for (Map singer : singers) {
                    singerIds.add((String) singer.get("id"));
                }
                Song song1 = new Song();
                song1.setId((String) song.get("sid"));
                song1.setName((String) song.get("title"));
                song1.setCover((String) song.get("picture"));
                song1.setUrl((String) song.get("url"));
                song1.setSingerIds(singerIds);
                Song s = songService.add(song1);
            }
            List<Map> singers = (List)song.get("singers");
            for (Map singer : singers) {
                if (singerService.get((String)singer.get("id")) == null){
                    Singer singer1 = new Singer();
                    singer1.setId((String)singer.get("id"));
                    singer1.setName((String)singer.get("name"));
                    singer1.setAvatar((String)singer.get("cover"));
                    Singer singer2 = singerService.addSinger(singer1);
                }
            }
        }
    }

    private void getCollectionsData() {
        String url = "https://douban.fm/j/v2/songlist/explore?type=hot&genre=0&limit=20&sample_cnt=5";
        Map<String,String> headers = new HashMap<>();
        String content = HttpUtil.getContent(url,headers);
        List<Map> collectDatas = JSON.parseObject(content,List.class);
        for (Map collectData : collectDatas) {
            Map creatorData = (Map)collectData.get("creator");
            List<Map> sample_songs = (List<Map>) collectData.get("sample_songs");
            if (subjectService.get(""+collectData.get("id")) == null){
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime created_time = LocalDateTime.parse((String)collectData.get("created_time"), formatter);
                List<String> songIds = new ArrayList<>();
                for (Map song : sample_songs) {
                    songIds.add((String) song.get("sid"));
                }
                Subject subject = new Subject();
                subject.setId(""+collectData.get("id"));
                subject.setName((String)collectData.get("title"));
                subject.setCover((String)collectData.get("cover"));
                subject.setMaster((String)creatorData.get("id"));
                subject.setDescription((String)collectData.get("intro"));
                subject.setSongIds(songIds);
                subject.setPublishedDate(created_time);
                subject.setSubjectType(SubjectUtil.TYPE_COLLECTION);
                subjectService.addSubject(subject);
            }

            if (singerService.get((String)creatorData.get("id")) == null) {
                Singer singer = new Singer();
                singer.setId((String)creatorData.get("id"));
                singer.setName((String)creatorData.get("name"));
                singer.setHomepage((String)creatorData.get("url"));
                singer.setAvatar((String)creatorData.get("picture"));
                Singer singer1 = singerService.addSinger(singer);
            }
        }
    }

}
