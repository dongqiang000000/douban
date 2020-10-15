package fm.douban.service.impl;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import fm.douban.model.Song;
import fm.douban.param.SongQueryParam;
import fm.douban.service.SongService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.LongSupplier;

@Service
public class SongServiceImpl implements SongService {

    private static final Logger LOG = LoggerFactory.getLogger(fm.douban.service.impl.SingerServiceImpl.class);
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public Song add(Song song) {
        if (song == null){
            LOG.error("song is null");
            return null;
        }
        mongoTemplate.insert(song);
        return song;
    }

    @Override
    public Song get(String songId) {
        Song song= mongoTemplate.findById(songId,Song.class);
        return song;
    }

    @Override
    public Page<Song> list(SongQueryParam songQueryParam) {
        if(songQueryParam == null){
            return null;
        }
        Criteria criteria = new Criteria();
        List<Criteria> subCris = new ArrayList<>();
        if (StringUtils.hasText(songQueryParam.getName())){
            subCris.add(Criteria.where("name").is(songQueryParam.getName()));
        }
        if (StringUtils.hasText(songQueryParam.getLyrics())){
            subCris.add(Criteria.where("lyrics").is(songQueryParam.getLyrics()));
        }
        if (subCris.isEmpty()){
            Query query = new Query(Criteria.where("gmtCreated").is(null));
            long count = mongoTemplate.count(query,Song.class);
            Pageable pageable = PageRequest.of(songQueryParam.getPageNum() - 1,songQueryParam.getPageSize());
            query.with(pageable);
            List<Song> songs = mongoTemplate.find(query,Song.class);
            Page<Song> pageResult = PageableExecutionUtils.getPage(songs, pageable, new LongSupplier() {
                @Override
                public long getAsLong() {
                    return count;
                }
            });
            return pageResult;
        }
        criteria.andOperator(subCris.toArray(new Criteria[]{}));
        Query query = new Query(criteria);
        long count = mongoTemplate.count(query,Song.class);
        Pageable pageable = PageRequest.of(songQueryParam.getPageNum() - 1,songQueryParam.getPageSize());
        query.with(pageable);
        List<Song> songs = mongoTemplate.find(query,Song.class);
        Page<Song> pageResult = PageableExecutionUtils.getPage(songs, pageable, new LongSupplier() {
            @Override
            public long getAsLong() {
                return count;
            }
        });

        return pageResult;
    }

    @Override
    public boolean modify(Song song) {
        if(song == null || !StringUtils.hasText(song.getId())){
            return false;
        }
        Query query = new Query(Criteria.where("id").is(song.getId()));
        Update update = new Update();

        if(song.getCover() !=null){
            update.set("cover",song.getCover());
        }
        if(song.getGmtCreated() != null){
            update.set("gmtCreated",song.getGmtCreated());
        }
        if(song.getGmtModified() != null){
            update.set("gmtModified",song.getGmtModified());
        }
        if(song.getLyrics() != null){
            update.set("lyrics",song.getLyrics());
        }
        if(song.getName() != null){
            update.set("name",song.getName());
        }
        if(song.getSingerIds() != null){
            update.set("singerIds",song.getSingerIds());
        }
        if(song.getUrl() != null){
            update.set("url",song.getUrl());
        }
        UpdateResult result = mongoTemplate.updateFirst(query,update,Song.class);
        return result!=null&&result.getModifiedCount()>0;
    }

    @Override
    public boolean delete(String songId) {
        if(!StringUtils.hasText(songId)){
            return false;
        }
        Song song = new Song();
        song.setId(songId);
        DeleteResult result = mongoTemplate.remove(song);
        return result!=null&&result.getDeletedCount()>0;
    }
}
