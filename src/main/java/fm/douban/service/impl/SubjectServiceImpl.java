package fm.douban.service.impl;

import com.mongodb.client.result.DeleteResult;
import fm.douban.model.Subject;
import fm.douban.service.SubjectService;
import fm.douban.util.SubjectUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class SubjectServiceImpl implements SubjectService {
    @Autowired
    private static final Logger LOG = LoggerFactory.getLogger(fm.douban.service.impl.SongServiceImpl.class);
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public Subject addSubject(Subject subject) {
        mongoTemplate.insert(subject);
        return subject;
    }

    @Override
    public Subject get(String subjectId) {
        return mongoTemplate.findById(subjectId,Subject.class);
    }

    @Override
    public List<Subject> getSubjects(String type) {
        Query query = new Query(Criteria.where("subjectType").is(type));
        List<Subject> subjects = mongoTemplate.find(query,Subject.class);
        return subjects;
    }

    @Override
    public List<Subject> getSubjects(String type, String subType) {
        Criteria criteria = new Criteria();
        List<Criteria> subCris = new ArrayList<>();
        if(StringUtils.hasText(type)){
            subCris.add(Criteria.where("subjectType").is(type));
        }
        if(StringUtils.hasText(subType)){
            subCris.add(Criteria.where("subjectSubType").is(subType));
        }
        
        if (subCris.isEmpty()){
            LOG.error("主题不存在");
            return null;
        }
        criteria.andOperator(subCris.toArray(new Criteria[]{}));
        Query query = new Query(criteria);
        List<Subject> subjects = mongoTemplate.find(query,Subject.class);
        return subjects;
    }

    @Override
    public List<Subject> getSubjects(Subject subjectParam) {
        if (subjectParam == null){
            return null;
        }
        String master = subjectParam.getMaster();
        Query query = new Query(Criteria.where("master").is(master));
        return mongoTemplate.find(query,Subject.class);
    }

    @Override
    public boolean delete(String subjectId) {
        if (!StringUtils.hasText(subjectId)){
            return false;
        }
        Subject subject = new Subject();
        subject.setId(subjectId);
        DeleteResult result = mongoTemplate.remove(subject);
        return result!=null&&result.getDeletedCount()>0;
    }

    @Override
    public boolean deleteAll(Query query) {
        DeleteResult result = mongoTemplate.remove(query,Subject.class);
        return result!=null;
    }

}
