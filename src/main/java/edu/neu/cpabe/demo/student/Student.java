package edu.neu.cpabe.demo.student;

import edu.neu.cpabe.demo.course.Course;
import lombok.Data;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

/**
 * 学生
 */
@Data
@Entity
@Table(name = "student")
@Access(AccessType.FIELD)
public class Student {

    @Id
    @GeneratedValue
    private Long id;

    @NaturalId
    private String studentId;

    private String name;

    private String sex;

    private String tel;

    private String attribute;

    @ManyToMany
    private List<Course> courses;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return studentId.equals(student.studentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(studentId);
    }
}
