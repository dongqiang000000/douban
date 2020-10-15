package fm.douban.service.impl;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import fm.douban.model.Singer;
import fm.douban.service.SingerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class SingerServiceImpl implements SingerService {

    private static final Logger LOG = LoggerFactory.getLogger(fm.douban.service.impl.SingerServiceImpl.class);

    @Autowired
    private MongoTemplate mongoTemplate;
    //增加一个歌手
    public Singer addSinger(Singer singer){
        if(singer == null){
            LOG.error("歌手为空");
            return null;
        }
        return mongoTemplate.insert(singer);
    }
    //根据歌手id查询歌手
    public Singer get(String singerId){
        if (!StringUtils.hasText(singerId)) {
            LOG.error("input songId is blank.");
            return null;
        }
        Singer singer = mongoTemplate.findById(singerId,Singer.class);
        return singer;
    }
    //查询全部歌手
    public List<Singer> getAll(){
        List<Singer> singers = mongoTemplate.findAll(Singer.class);
        return singers;
    }
    //修改歌手
    public boolean modify(Singer singer){
        if(singer == null || !StringUtils.hasText(singer.getId())){
            LOG.error("歌手不存在");
            return false;
        }
        Query query = new Query(Criteria.where("id").is(singer.getId()));
        Update update = new Update();

        if(singer.getAvatar()!=null){
            update.set("avatar",singer.getAvatar());
        }
        if (singer.getGmtCreated() != null){
            update.set("gmtCreated",singer.getGmtCreated());
        }
        if (singer.getGmtModified() != null){
            update.set("gmtModified",singer.getGmtModified());
        }
        if (singer.getHomepage() != null){
            update.set("homepage",singer.getHomepage());
        }
        if (singer.getName() != null){
            update.set("name",singer.getName());
        }
        if (singer.getSimilarSingerIds() != null){
            update.set("similarSingerIds",singer.getSimilarSingerIds());
        }
        UpdateResult result = mongoTemplate.updateFirst(query,update,Singer.class);
        return result != null && result.getModifiedCount() > 0;
    }
    //根据id主键删除歌手
    public boolean delete(String singerId){
        if(!StringUtils.hasText(singerId)){
            LOG.error("歌手id为空");
            return false;
        }
        Singer singer = get(singerId);
        DeleteResult result = mongoTemplate.remove(singer);
        return result != null && result.getDeletedCount() > 0;
    }

    @Override
    public boolean deleteAll() {
        Query query = new Query(Criteria.where("homepage").is(null));
        DeleteResult result = mongoTemplate.remove(query,Singer.class);
        return result!=null;
    }

}
