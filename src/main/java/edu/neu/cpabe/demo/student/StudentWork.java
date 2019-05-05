package edu.neu.cpabe.demo.student;

import edu.neu.cpabe.demo.teacher.TeacherWork;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Data
@Entity
@Table(name = "student_work")
@Access(AccessType.FIELD)
public class StudentWork {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, targetEntity = Student.class)
    private Student student;

    @ManyToOne(fetch = FetchType.EAGER, targetEntity = TeacherWork.class)
    private TeacherWork teacherWork;

    @Column(columnDefinition = "TEXT")
    private String content;

    private Float score;

    @Column(columnDefinition = "TEXT")
    private String remark;

    private Date submitTime;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StudentWork that = (StudentWork) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
