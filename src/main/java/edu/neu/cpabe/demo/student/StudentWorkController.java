package edu.neu.cpabe.demo.student;

import edu.neu.cpabe.demo.teacher.Teacher;
import edu.neu.cpabe.demo.teacher.TeacherWork;
import edu.neu.cpabe.demo.teacher.TeacherWorkRepository;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/students/work")
public class StudentWorkController {

    private final TeacherWorkRepository teacherWorkRepository;

    private final StudentWorkRepository studentWorkRepository;

    public StudentWorkController(TeacherWorkRepository teacherWorkRepository, StudentWorkRepository studentWorkRepository) {
        this.teacherWorkRepository = teacherWorkRepository;
        this.studentWorkRepository = studentWorkRepository;
    }

    /**
     * 根据教师题目id查询可批阅的作业
     *
     * @param teacher       教师登陆信息
     * @param teacherWorkId 题目id
     * @return
     */
    @GetMapping("/{teacherWorkId}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<?> findStudentWork(@AuthenticationPrincipal Teacher teacher,
                                             @PathVariable Long teacherWorkId) {
        List<TeacherWork> teacherWork = teacherWorkRepository.findByTeacher(teacher);
        if (teacherWork.isEmpty()) throw new IllegalArgumentException("未找到题目");
        TeacherWork t = teacherWork.stream().filter(v -> v.getId().equals(teacherWorkId))
                .findFirst().orElseThrow(() -> new IllegalArgumentException("无权限"));
        return ResponseEntity.ok(studentWorkRepository.findByTeacherWork(t));
    }


    /**
     * 批阅作业
     *
     * @param studentWorkId
     * @param teacher
     * @param scoreDTO
     * @return
     */
    @PostMapping("/score/{studentWorkId}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<?> score(@PathVariable Long studentWorkId,
                                   @AuthenticationPrincipal Teacher teacher,
                                   @RequestBody ScoreDTO scoreDTO) {
        StudentWork studentWork = studentWorkRepository.findById(studentWorkId)
                .orElseThrow(() -> new IllegalArgumentException("未找到该作业"));
        List<TeacherWork> teacherWorks = teacherWorkRepository.findByTeacher(teacher);
        Long teacherWorkId = studentWork.getTeacherWork().getId();
        TeacherWork t = teacherWorks.stream().filter(v -> v.getId().equals(teacherWorkId))
                .findFirst().orElseThrow(() -> new IllegalArgumentException("无权限"));
        studentWork.setScore(scoreDTO.getScore());
        studentWork.setRemark(scoreDTO.getRemark());
        studentWorkRepository.save(studentWork);
        return ResponseEntity.ok().build();
    }

    /**
     * 新建作业
     *
     * @param student
     * @param dto
     * @return
     */
    @PostMapping
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<?> uploadStudentWork(@AuthenticationPrincipal Student student,
                                               @RequestBody StudentWorkUploadDTO dto) {
        TeacherWork t = teacherWorkRepository.findById(dto.getTeacherWorkId())
                .orElseThrow(() -> new IllegalArgumentException("无此题目"));
        if (t.getDeadline().before(new Date()))
            throw new IllegalStateException("已过提交期限");
        StudentWork sw = new StudentWork();
        sw.setStudent(student);
        sw.setContent(dto.getContent());
        sw.setSubmitTime(new Date());
        sw.setTeacherWork(t);
        return ResponseEntity.ok(studentWorkRepository.save(sw));
    }

    /**
     * 查询提交作业列表
     *
     * @param student
     * @return
     */
    @GetMapping
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<?> findStudentWork(@AuthenticationPrincipal Student student) {
        return ResponseEntity.ok(studentWorkRepository.findByStudent(student));
    }

    /**
     * 修改提交作业
     *
     * @param student
     * @param dto
     * @return
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<?> modifyStudentWork(@AuthenticationPrincipal Student student,
                                               @RequestBody StudentWorkUploadDTO dto,
                                               @PathVariable Long id) {
        List<StudentWork> submitted = studentWorkRepository.findByStudent(student);
        StudentWork studentWork = submitted.stream().filter(v -> v.getId().equals(id)).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("不存在此作业"));
        if (studentWork.getTeacherWork().getDeadline().before(new Date()))
            throw new IllegalStateException("已过提交期限");
        if (dto.getContent() != null)
            studentWork.setContent(dto.getContent());
        studentWorkRepository.save(studentWork);
        return ResponseEntity.noContent().build();
    }


    @Data
    public static class StudentWorkUploadDTO {

        private Long id;

        private String content;

        private Long teacherWorkId;
    }

    @Data
    public static class ScoreDTO {

        private Float score;

        private String remark;

    }

}
