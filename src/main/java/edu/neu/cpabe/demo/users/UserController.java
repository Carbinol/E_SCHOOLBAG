package edu.neu.cpabe.demo.users;

import edu.neu.cpabe.demo.student.Student;
import edu.neu.cpabe.demo.student.StudentRepository;
import edu.neu.cpabe.demo.teacher.Teacher;
import edu.neu.cpabe.demo.teacher.TeacherRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

/**
 * 用户端点
 */
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final StudentRepository studentRepository;

    private final TeacherRepository teacherRepository;

    public UserController(UserRepository userRepository, PasswordEncoder passwordEncoder, StudentRepository studentRepository, TeacherRepository teacherRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.studentRepository = studentRepository;
        this.teacherRepository = teacherRepository;
    }

    /**
     * 用户信息端点
     *
     * @param user 用户信息
     * @return
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public UserDetails user(@AuthenticationPrincipal EschoolBagUserDetails user) {
        return user;
    }

    /**
     * 创建新用户
     *
     * @param dto 用户信息
     * @return
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User createUser(@RequestBody UserDTO dto) {
        User user = new User(dto.getUsername(), dto.getEmail(), passwordEncoder.encode(dto.getPassword()),
                true, true, true, true);
        String role = dto.getRole();
        if (role.equals("student")) {
            Student s = studentRepository.findByStudentId(dto.getRoleId()).orElseThrow(() -> new IllegalStateException("未找到此学生"));
            user.setStudent(s);
            user.setRole("ROLE_USER,ROLE_STUDENT");
        } else if (role.equals("teacher")) {
            Teacher t = teacherRepository.findByTeacherId(dto.getRoleId()).orElseThrow(() -> new IllegalStateException("未找到此教师"));
            user.setTeacher(t);
            user.setRole("ROLE_USER,ROLE_TEACHER");
        } else {
            throw new IllegalArgumentException("角色错误");
        }
        return userRepository.save(user);
    }



    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserDTO {

        private String username;

        private String password;

        private String email;

        private String role;

        private String roleId;

    }


}
