package edu.neu.cpabe.demo.student;

import edu.neu.cpabe.demo.course.Course;
import edu.neu.cpabe.demo.teacher.TeacherWork;
import edu.neu.cpabe.demo.teacher.TeacherWorkController;
import edu.neu.cpabe.demo.teacher.TeacherWorkRepository;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/students")
public class StudentController {

    private final TeacherWorkRepository teacherWorkRepository;

    private final StudentRepository studentRepository;

    public StudentController(TeacherWorkRepository teacherWorkRepository, StudentRepository studentRepository) {
        this.teacherWorkRepository = teacherWorkRepository;
        this.studentRepository = studentRepository;
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
        return studentRepository.findByStudentId(student.getStudentId()).get().getCourses();
    }

    /**
     * 查询课程下所有的题目
     * @param courseId
     * @param student
     * @return
     */
    @GetMapping("/courses/{courseId}/teacherWork")
    @PreAuthorize("hasRole('STUDENT')")
    public List<TeacherWorkController.TeacherWorkDTO> findTeacherWork(@PathVariable String courseId,
                                                                      @AuthenticationPrincipal(expression = "student") Student student) {
        List<Course> courses = studentRepository.findByStudentId(student.getStudentId()).get().getCourses();
        Course c = courses.stream().filter(v -> v.getCourseId().equals(courseId))
                .findFirst().orElseThrow(() -> new IllegalArgumentException("没有选择此课程"));
        return teacherWorkRepository.findByCourseAndDeadlineAfter(c, new Date()).stream().map(v->{
            TeacherWorkController.TeacherWorkDTO dto = new TeacherWorkController.TeacherWorkDTO();
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


}
