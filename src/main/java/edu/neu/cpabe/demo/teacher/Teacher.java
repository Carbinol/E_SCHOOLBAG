package edu.neu.cpabe.demo.teacher;

import edu.neu.cpabe.demo.course.Course;
import lombok.Data;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Data
@Entity
@Table(name = "teacher")
@Access(AccessType.FIELD)
public class Teacher implements Serializable {

    private static final long serialVersionUID = -1273281813289480577L;

    @Id
    @GeneratedValue
    private Long id;

    @NaturalId
    private String teacherId;

    private String name;

    private String sex;

    private String tel;

    @OneToMany(orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Course> courses;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Teacher teacher = (Teacher) o;
        return teacherId.equals(teacher.teacherId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(teacherId);
    }


}
