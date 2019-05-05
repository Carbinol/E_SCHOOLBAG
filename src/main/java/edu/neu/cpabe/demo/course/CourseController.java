package edu.neu.cpabe.demo.course;

import edu.neu.cpabe.demo.student.Student;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/courses")
public class CourseController {

    private CourseRepository courseRepository;

    public CourseController(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @GetMapping("/{courseId}/students")
    @PreAuthorize("hasRole('TEACHER')")
    public List<Student> findByCourseId(@PathVariable String courseId) {
        Course course = courseRepository.findByCourseId(courseId).orElseThrow(
                () -> new IllegalArgumentException("不存在此课程"));
        return course.getStudents();
    }
}
