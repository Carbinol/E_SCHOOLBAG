package edu.neu.cpabe.demo.teacher;

import edu.neu.cpabe.demo.course.Course;
import edu.neu.cpabe.demo.course.CourseRepository;
import edu.neu.cpabe.demo.encrypt.DemoEncryptUtilImpl;
import lombok.Data;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/teachers/work")
public class TeacherWorkController {

    private TeacherWorkRepository teacherWorkRepository;

    private CourseRepository courseRepository;

    public TeacherWorkController(TeacherWorkRepository teacherWorkRepository, CourseRepository courseRepository) {
        this.teacherWorkRepository = teacherWorkRepository;
        this.courseRepository = courseRepository;
    }

    @PostMapping
    @PreAuthorize("hasRole('TEACHER')")
    public void uploadWork(@AuthenticationPrincipal(expression = "teacher") Teacher t,
                           @RequestBody TeacherWorkDTO teacherWorkDTO) throws ParseException {
        DemoEncryptUtilImpl demoEncryptUtil = new DemoEncryptUtilImpl();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Course course = courseRepository.findByCourseId(teacherWorkDTO.getCourseId()).orElseThrow(() -> new IllegalArgumentException("查不到课程"));
        TeacherWork tw = TeacherWork.TeacherWorkBuilder.aTeacherWork()
                .withTeacher(t)
                .withPolicy(teacherWorkDTO.getPolicy())
                .withDeadline(sdf.parse(teacherWorkDTO.getDeadline()))
                .withEncContent(demoEncryptUtil.encrypt(teacherWorkDTO.getContent()))
                .withTitle(teacherWorkDTO.getTitle())
                .build();
        tw.setCourse(course);
        teacherWorkRepository.save(tw);
    }

    @GetMapping
    @PreAuthorize("hasRole('TEACHER')")
    public List<TeacherWorkDTO> findTeacherWork(@AuthenticationPrincipal(expression = "teacher") Teacher teacher) {
        return teacherWorkRepository.findByTeacher(teacher).stream().map(v->{
            TeacherWorkDTO dto = new TeacherWorkDTO();
            dto.setTeacherId(v.getTeacher().getTeacherId());
            dto.setContent(v.getEncContent());
            dto.setCourseId(v.getCourse().getCourseId());
            dto.setDeadline(DateFormatUtils.format(v.getDeadline(),"yyyy-MM-dd"));
            dto.setPolicy(v.getPolicy());
            dto.setTitle(v.getTitle());
            dto.setTeacherWorkId(v.getId());
            return dto;
        }).collect(toList());
    }

    @PutMapping("/{teacherWorkId}")
    @PreAuthorize("hasRole('TEACHER')")
    public void modifyWork(@AuthenticationPrincipal(expression = "teacher") Teacher t,
                           @RequestBody TeacherWorkDTO teacherWorkDTO,
                           @PathVariable String teacherWorkId) throws ParseException {
        List<TeacherWork> byTeacher = teacherWorkRepository.findByTeacher(t);
        TeacherWork tw = teacherWorkRepository.findById(Long.parseLong(teacherWorkId))
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
        if(teacherWorkDTO.getTitle() != null)
            tw.setTitle(teacherWorkDTO.getTitle());
        teacherWorkRepository.save(tw);
    }


    @Data
    public static class TeacherWorkDTO {

        private Long teacherWorkId;

        private String teacherId;

        private String content;

        private String policy;

        private String deadline;

        private String courseId;

        private String title;

    }

}
