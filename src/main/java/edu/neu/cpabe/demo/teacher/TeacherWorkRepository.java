package edu.neu.cpabe.demo.teacher;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeacherWorkRepository extends JpaRepository<TeacherWork, Long> {

    List<TeacherWork> findByTeacher(Teacher teacher);
}
