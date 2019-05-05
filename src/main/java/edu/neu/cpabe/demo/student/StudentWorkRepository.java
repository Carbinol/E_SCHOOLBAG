package edu.neu.cpabe.demo.student;

import edu.neu.cpabe.demo.teacher.TeacherWork;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentWorkRepository extends JpaRepository<StudentWork, Long> {

    List<StudentWork> findByTeacherWork(TeacherWork teacherWork);

    List<StudentWork> findByStudent(Student student);
}
