package edu.neu.cpabe.demo.teacher;

import edu.neu.cpabe.demo.encrypt.DemoEncryptUtilImpl;
import lombok.Data;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

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
        DemoEncryptUtilImpl demoEncryptUtil = new DemoEncryptUtilImpl();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        TeacherWork tw = TeacherWork.TeacherWorkBuilder.aTeacherWork()
                .withTeacher(t)
                .withPolicy(teacherWorkDTO.getPolicy())
                .withDeadline(sdf.parse(teacherWorkDTO.getDeadline()))
                .withEncContent(demoEncryptUtil.encrypt(teacherWorkDTO.getContent()))
                .build();
        teacherWorkRepository.save(tw);
    }

    @Data
    public static class TeacherWorkDTO {

        private String teacherId;

        private String content;

        private String policy;

        private String deadline;

    }

}
