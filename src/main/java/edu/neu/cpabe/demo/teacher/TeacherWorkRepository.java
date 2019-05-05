package edu.neu.cpabe.demo.teacher;

import edu.neu.cpabe.demo.course.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface TeacherWorkRepository extends JpaRepository<TeacherWork, Long> {

    List<TeacherWork> findByTeacher(Teacher teacher);

    List<TeacherWork> findByCourseAndDeadlineAfter(Course course, Date date);
}
