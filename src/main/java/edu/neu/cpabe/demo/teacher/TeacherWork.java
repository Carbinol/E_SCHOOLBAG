package edu.neu.cpabe.demo.teacher;

import lombok.Data;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Data
@Entity
@Table(name = "teacher_work")
@Access(AccessType.FIELD)
public class TeacherWork {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, targetEntity = Teacher.class)
    private Teacher teacher;

    private String policy;

    @Column(columnDefinition = "TEXT")
    private String encContent;

    private Date deadline;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TeacherWork that = (TeacherWork) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }


    public static final class TeacherWorkBuilder {
        private Teacher teacher;
        private String policy;
        private String encContent;
        private Date deadline;

        private TeacherWorkBuilder() {
        }

        public static TeacherWorkBuilder aTeacherWork() {
            return new TeacherWorkBuilder();
        }

        public TeacherWorkBuilder withTeacher(Teacher teacher) {
            this.teacher = teacher;
            return this;
        }

        public TeacherWorkBuilder withPolicy(String policy) {
            this.policy = policy;
            return this;
        }

        public TeacherWorkBuilder withEncContent(String encContent) {
            this.encContent = encContent;
            return this;
        }

        public TeacherWorkBuilder withDeadline(Date deadline) {
            this.deadline = deadline;
            return this;
        }

        public TeacherWork build() {
            TeacherWork teacherWork = new TeacherWork();
            teacherWork.setTeacher(teacher);
            teacherWork.setPolicy(policy);
            teacherWork.setEncContent(encContent);
            teacherWork.setDeadline(deadline);
            return teacherWork;
        }
    }
}
