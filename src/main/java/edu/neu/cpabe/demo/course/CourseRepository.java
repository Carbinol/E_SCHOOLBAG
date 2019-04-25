package edu.neu.cpabe.demo.course;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long> {

    Optional<Course> findByCourseId(String courseId);
}
