package fm.douban.spider;

import com.alibaba.fastjson.JSON;
import fm.douban.model.Singer;
import fm.douban.model.Song;
import fm.douban.service.SingerService;
import fm.douban.service.SongService;
import fm.douban.service.SubjectService;
import fm.douban.util.HttpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class SingerSongSpider {
    @Autowired
    private SubjectService subjectService;
    @Autowired
    private SingerService singerService;
    @Autowired
    SongService songService;

    //@PostConstruct
    public void init(){
        System.out.println("启动2");
        doExcute();
    }

    public void doExcute(){
        getSongDataBySingers();
    }

    private void getSongDataBySingers(){
        List<Singer> singers = singerService.getAll();
        for (Singer singer :singers){
            System.out.println(2);
            String url = "https://douban.fm/j/v2/artist/"+singer.getId()+"/";
            Map<String,String> headers = new HashMap<>();
            String content = HttpUtil.getContent(url,headers);
            Map returnData = JSON.parseObject(content,Map.class);

            Map songlistData = (Map)returnData.get("songlist");
            if(songlistData!=null){
                List<Map> songsData = (List)songlistData.get("songs");
                for (Map song : songsData) {
                    if (songService.get((String)song.get("sid"))==null) {
                        List<Map> singers1 = (List) song.get("singers");
                        List<String> singerIds = new ArrayList<>();
                        for (Map singer1 : singers1) {
                            singerIds.add((String) singer1.get("id"));
                        }
                        Song song1 = new Song();
                        song1.setId((String) song.get("sid"));
                        song1.setName((String) song.get("title"));
                        song1.setCover((String) song.get("picture"));
                        song1.setUrl((String) song.get("url"));
                        song1.setSingerIds(singerIds);
                        Song s = songService.add(song1);
                    }
                }
            }

            Map related_channelData = (Map)returnData.get("related_channel");

            if(related_channelData!=null){
                List<Map> similar_artists = (List)related_channelData.get("similar_artists");
                List<String> similarSingerIds = new ArrayList<>();
                for (Map similar_artist : similar_artists) {
                    similarSingerIds.add((String)similar_artist.get("id"));
                    if(singerService.get((String)similar_artist.get("id")) == null) {
                        Singer singer1 = new Singer();
                        singer1.setId((String)similar_artist.get("id"));
                        singer1.setName((String)similar_artist.get("name"));
                        singer1.setAvatar((String)similar_artist.get("avatar"));
                        Singer singer2 = singerService.addSinger(singer1);
                    }
                }
                if (singer.getSimilarSingerIds()==null){
                    singer.setSimilarSingerIds(similarSingerIds);
                    boolean b =singerService.modify(singer);
                }
            }
        }
    }
}
