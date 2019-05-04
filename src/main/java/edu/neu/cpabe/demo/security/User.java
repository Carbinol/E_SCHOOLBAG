package edu.neu.cpabe.demo.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import edu.neu.cpabe.demo.student.Student;
import edu.neu.cpabe.demo.teacher.Teacher;

import javax.persistence.*;
import java.io.Serializable;


/**
 * 用户信息
 */
@Entity
@Table(name = "SYS_USER")
public class User implements Serializable {

    private static final long serialVersionUID = 9192751259398918318L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    /**
     * 用户名
     */
    private String name;

    /**
     * email
     */
    private String email;

    /**
     * 密码
     */
    @JsonIgnore
    private String password;

    /**
     * 角色 ROLE_TEACHER, ROLE_STUDENT, ROLE_USER
     */
    private String role;

    @ManyToOne
    private Student student;

    @ManyToOne
    private Teacher teacher;

    /**
     * 账户是否吊销
     */
    private Boolean isAccountNonExpired;

    /**
     * 是否锁定
     */
    private Boolean isAccountNonLocked;

    /**
     * 密码是否吊销
     */
    private Boolean isCredentialsNonExpired;

    /**
     * 是否可用
     */
    private Boolean isEnabled;

    private User() {
    }

    public User(String name, String email, String password, String role, Student student, Teacher teacher, Boolean isAccountNonExpired, Boolean isAccountNonLocked, Boolean isCredentialsNonExpired, Boolean isEnabled) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.student = student;
        this.teacher = teacher;
        this.isAccountNonExpired = isAccountNonExpired;
        this.isAccountNonLocked = isAccountNonLocked;
        this.isCredentialsNonExpired = isCredentialsNonExpired;
        this.isEnabled = isEnabled;
    }

    public User(String name, String email, String password, Boolean isAccountNonExpired, Boolean isAccountNonLocked, Boolean isCredentialsNonExpired, Boolean isEnabled) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.isAccountNonExpired = isAccountNonExpired;
        this.isAccountNonLocked = isAccountNonLocked;
        this.isCredentialsNonExpired = isCredentialsNonExpired;
        this.isEnabled = isEnabled;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getAccountNonExpired() {
        return isAccountNonExpired;
    }

    public void setAccountNonExpired(Boolean accountNonExpired) {
        isAccountNonExpired = accountNonExpired;
    }

    public Boolean getAccountNonLocked() {
        return isAccountNonLocked;
    }

    public void setAccountNonLocked(Boolean accountNonLocked) {
        isAccountNonLocked = accountNonLocked;
    }

    public Boolean getCredentialsNonExpired() {
        return isCredentialsNonExpired;
    }

    public void setCredentialsNonExpired(Boolean credentialsNonExpired) {
        isCredentialsNonExpired = credentialsNonExpired;
    }

    public Boolean getEnabled() {
        return isEnabled;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public void setEnabled(Boolean enabled) {
        isEnabled = enabled;
    }
}
