package fm.douban.service.impl;

import com.mongodb.client.result.DeleteResult;
import fm.douban.model.Favorite;
import fm.douban.service.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FavoriteServiceImpl implements FavoriteService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public Favorite add(Favorite fav) {
        return mongoTemplate.insert(fav);
    }

    @Override
    public List<Favorite> list(Favorite favParam) {
        Criteria criteria = new Criteria();
        List<Criteria> favCris = new ArrayList<>();
        if (favParam.getUserId() !=null){
            favCris.add(Criteria.where("userId").is(favParam.getUserId()));
        }
        if (favParam.getItemType() !=null){
            favCris.add(Criteria.where("itemType").is(favParam.getItemType()));
        }
        if (favParam.getItemId() !=null) {
            favCris.add(Criteria.where("itemId").is(favParam.getItemId()));
        }
        criteria.andOperator(favCris.toArray(new Criteria[]{}));
        Query query = new Query(criteria);
        List<Favorite> favorites = mongoTemplate.find(query,Favorite.class);
        return favorites;
    }

    @Override
    public boolean delete(Favorite favParam) {
        DeleteResult result = mongoTemplate.remove(favParam);
        return result!=null&&result.getDeletedCount()>0;
    }
}
