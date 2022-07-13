package com.example.demo.controller;

import com.example.demo.dto.StudentDTO;
import com.example.demo.entity.StudentEntity;
import com.example.demo.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class StudentController {

    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("students")
    public List<StudentDTO> getStudents() {
        return studentService.getStudents();
    }

    @GetMapping(path = "/student/{studentId}")
    public Optional<StudentDTO> getStudent(@PathVariable("studentId") Long studentId) {
        return studentService.getStudent(studentId);
    }

    @PostMapping("/student")
    public ResponseEntity<StudentEntity> registerNewStudent(@RequestBody StudentEntity student) {
        return studentService.addNewStudent(student);
    }

    @DeleteMapping("/student/{studentId}")
    public void deleteStudent(@PathVariable("studentId") Long studentId) {
        studentService.deleteStudent(studentId);
    }

    @PutMapping("/student/{studentId}")
    public void updateStudent(
            @PathVariable("studentId") Long studentId,
            @RequestParam(name = "first-name", required = false) String firstName,
            @RequestParam(name = "last-name", required = false) String lastName,
            @RequestParam(name = "email", required = false) String email)
    {
        studentService.updateStudent(studentId, firstName, lastName, email);
    }

}
