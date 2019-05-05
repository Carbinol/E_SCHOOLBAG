package edu.neu.cpabe.demo.teacher;

import edu.neu.cpabe.demo.course.Course;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/teachers")
public class TeacherController {

    private final TeacherRepository teacherRepository;

    public TeacherController(TeacherRepository teacherRepository) {
        this.teacherRepository = teacherRepository;
    }

    @GetMapping("/{teacherId}/courses")
    @PreAuthorize("hasAuthority('ROLE_TEACHER')")
    private List<Course> findCourses(@PathVariable String teacherId) {
        log.debug("teacherId = {}", teacherId);
        Teacher teacher = teacherRepository.findByTeacherId(teacherId).orElseThrow(
                () -> new IllegalArgumentException("无此教师"));
        return teacher.getCourses();
    }

}
