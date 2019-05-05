package edu.neu.cpabe.demo.student;

import edu.neu.cpabe.demo.course.Course;
import edu.neu.cpabe.demo.teacher.TeacherWork;
import edu.neu.cpabe.demo.teacher.TeacherWorkRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/students")
public class StudentController {

    private final TeacherWorkRepository teacherWorkRepository;

    public StudentController(TeacherWorkRepository teacherWorkRepository) {
        this.teacherWorkRepository = teacherWorkRepository;
    }

    /**
     * 查询课程
     *
     * @param student
     * @return
     */
    @GetMapping("/courses")
    @PreAuthorize("hasRole('STUDENT')")
    public List<Course> findCourses(@AuthenticationPrincipal(expression = "student") Student student) {
        return student.getCourses();
    }

    @GetMapping("/courses/{courseId}/teacherWork")
    @PreAuthorize("hasRole('STUDENT')")
    public List<TeacherWork> findTeacherWork(@PathVariable String courseId,
                                             @AuthenticationPrincipal(expression = "student") Student student) {
        List<Course> courses = student.getCourses();
        Course c = courses.stream().filter(v -> v.getCourseId().equals(courseId))
                .findFirst().orElseThrow(() -> new IllegalArgumentException("没有选择此课程"));
        return teacherWorkRepository.findByCourseAndDeadlineAfter(c, new Date());
    }


}
