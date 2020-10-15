package fm.douban.service.impl;

import fm.douban.model.User;
import fm.douban.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class);
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public User add(User user) {
        if (user == null) {
            LOG.error("input User data is null.");
            return null;
        }
       return mongoTemplate.insert(user);
    }

    @Override
    public User get(String name) {
        if (!StringUtils.hasText(name)){
            LOG.error("input name is null.");
            return null;
        }
        return mongoTemplate.findById(name,User.class);
    }
}
