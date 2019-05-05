package edu.neu.cpabe.demo.teacher;

import edu.neu.cpabe.demo.course.Course;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/teachers")
public class TeacherController {


    /**
     * 查询教师的所有课程
     *
     * @param t
     * @return
     */
    @GetMapping("/{teacherId}/courses")
    @PreAuthorize("hasAuthority('ROLE_TEACHER')")
    private List<Course> findCourses(@AuthenticationPrincipal(expression = "teacher") Teacher t) {
        log.debug("teacherId = {}", t);
        return t.getCourses();
    }

}
