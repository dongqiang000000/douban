package fm.douban.service;

import fm.douban.model.Subject;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

public interface SubjectService {
    Subject addSubject(Subject subject);
    Subject get(String subjectId);
    List<Subject> getSubjects(String type);
    List<Subject> getSubjects(String type,String subType);
    List<Subject> getSubjects(Subject subjectParam);
    boolean delete(String subjectId);
    boolean deleteAll(Query query);
}
