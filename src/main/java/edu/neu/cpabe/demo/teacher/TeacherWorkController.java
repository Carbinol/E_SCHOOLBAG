package edu.neu.cpabe.demo.teacher;

import edu.neu.cpabe.demo.encrypt.DemoEncryptUtilImpl;
import lombok.Data;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

@RestController
@RequestMapping("/teachers/work")
public class TeacherWorkController {

    private TeacherWorkRepository teacherWorkRepository;

    public TeacherWorkController(TeacherWorkRepository teacherWorkRepository) {
        this.teacherWorkRepository = teacherWorkRepository;
    }

    @PostMapping
    @PreAuthorize("hasRole('TEACHER')")
    public void uploadWork(@AuthenticationPrincipal(expression = "teacher") Teacher t,
                           @RequestBody TeacherWorkDTO teacherWorkDTO) throws ParseException {
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

    @GetMapping
    @PreAuthorize("hasRole('TEACHER')")
    public List<TeacherWork> findTeacherWork(@AuthenticationPrincipal(expression = "teacher") Teacher teacher) {
        return teacherWorkRepository.findByTeacher(teacher);
    }

    @PutMapping("/{teacherWorkId)")
    @PreAuthorize("hasRole('TEACHER')")
    public void modifyWork(@AuthenticationPrincipal(expression = "teacher") Teacher t,
                           @RequestBody TeacherWorkDTO teacherWorkDTO,
                           @PathVariable Long teacherWorkId) throws ParseException {
        List<TeacherWork> byTeacher = teacherWorkRepository.findByTeacher(t);
        TeacherWork tw = teacherWorkRepository.findById(teacherWorkId)
                .orElseThrow(() -> new IllegalArgumentException("无此题目"));
        if (byTeacher.stream().noneMatch(v -> v.equals(tw)))
            throw new IllegalStateException("题目权限错误");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (teacherWorkDTO.getDeadline() != null)
            tw.setDeadline(sdf.parse(teacherWorkDTO.getDeadline()));
        if (teacherWorkDTO.getPolicy() != null)
            tw.setPolicy(teacherWorkDTO.getPolicy());
        if (teacherWorkDTO.getContent() != null)
            tw.setEncContent(teacherWorkDTO.getContent());
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
