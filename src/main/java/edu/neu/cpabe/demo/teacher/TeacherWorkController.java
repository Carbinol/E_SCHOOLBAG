package edu.neu.cpabe.demo.teacher;

import edu.neu.cpabe.demo.encrypt.CpabeEncryptUtilImpl;
import lombok.Data;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

@RestController
@RequestMapping("/teachers/work")
public class TeacherWorkController {

    private TeacherWorkRepository teacherWorkRepository;

    private TeacherRepository teacherRepository;

    public TeacherWorkController(TeacherWorkRepository teacherWorkRepository,
                                 TeacherRepository teacherRepository) {
        this.teacherWorkRepository = teacherWorkRepository;
        this.teacherRepository = teacherRepository;
    }

    @PostMapping
    public void uploadWork(@RequestBody TeacherWorkDTO teacherWorkDTO) throws ParseException {
        Teacher t = teacherRepository.findByTeacherId(teacherWorkDTO.getTeacherId()).orElseThrow(() -> new IllegalArgumentException("无此教师"));
        CpabeEncryptUtilImpl demoEncryptUtil = new CpabeEncryptUtilImpl();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        TeacherWork tw = TeacherWork.TeacherWorkBuilder.aTeacherWork()
                .withTeacher(t)
                .withPolicy(teacherWorkDTO.getPolicy())
                .withDeadline(sdf.parse(teacherWorkDTO.getDeadline()))
                .withEncContent(demoEncryptUtil.encrypt(teacherWorkDTO.getContent(),teacherWorkDTO.getPolicy()))
                .build();
        teacherWorkRepository.save(tw);
    }

    @GetMapping("/{teacherId}")
    public List<TeacherWork> findTeacherWork(@PathVariable String teacherId){
        Teacher t = teacherRepository.findByTeacherId(teacherId).orElseThrow(() -> new IllegalArgumentException("无此教师"));
        return teacherWorkRepository.findByTeacher(t);
    }

    @Data
    public static class TeacherWorkDTO {

        private String teacherId;

        private String content;

        private String policy;

        private String deadline;

    }

}
