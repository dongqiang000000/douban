package fm.douban.app.control;

import fm.douban.util.SubjectUtil;
import fm.douban.model.Subject;
import fm.douban.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SubjectTestControl {
    @Autowired
    private SubjectService subjectService;

    @GetMapping("/test/subject/add")
    public Subject testAdd(){
        Subject subject = new Subject();
        subject.setId("0");
        subject.setSubjectType(SubjectUtil.TYPE_MHZ);
        subject.setSubjectSubType(SubjectUtil.TYPE_SUB_MOOD);
        Subject subject1 = subjectService.addSubject(subject);
        return subject1;
    }
    @GetMapping("/test/subject/get")
    public Subject testGet(){
        return subjectService.get("0");
    }
    @GetMapping("/test/subject/getByType")
    public List<Subject> testGetByType(){
        List<Subject> subjects = subjectService.getSubjects(SubjectUtil.TYPE_COLLECTION);
        return subjects;
    }
    @GetMapping("/test/subject/getBySubType")
    public List<Subject> testGetBySubType(){
        List<Subject> subjects = subjectService.getSubjects(SubjectUtil.TYPE_MHZ,SubjectUtil.TYPE_SUB_AGE);
        return subjects;
    }
    @GetMapping("/test/subject/del")
    public boolean testDelete(){
       return subjectService.delete("0");
    }
    @GetMapping("/test/subject/delAll")
    public boolean testDeleteAll(){
        Query query = new Query(Criteria.where("subjectType").is(SubjectUtil.TYPE_COLLECTION));
        return subjectService.deleteAll(query);
    }
}
