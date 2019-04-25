package edu.neu.cpabe.demo.course;

import com.fasterxml.jackson.annotation.JsonIgnore;
import edu.neu.cpabe.demo.student.Student;
import lombok.Data;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Data
@Entity
@Table(name = "course")
@Access(AccessType.FIELD)
public class Course {

    @Id
    @GeneratedValue
    private Long id;

    @NaturalId
    private String courseId;

    private String name;

    @JsonIgnore
    @ManyToMany(mappedBy = "courses")
    private List<Student> students;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return courseId.equals(course.courseId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(courseId);
    }
}
