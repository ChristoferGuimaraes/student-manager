package com.example.demo.controller;

import com.example.demo.dto.StudentDTO;
import com.example.demo.service.StudentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("students")
    public ResponseEntity<Page<StudentDTO>> getAllStudents(
            @RequestParam(value="page", defaultValue = "0" )Integer page,
            @RequestParam(value="size", defaultValue = "20") Integer size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<StudentDTO> list = studentService.getAllStudents(pageRequest);
        return ResponseEntity.ok(list);
    }

    @GetMapping(path = "/student/{studentId}")
    public Optional<StudentDTO> getStudentById(@PathVariable("studentId") Long studentId) {
        return studentService.getStudentById(studentId);
    }

    @PostMapping("/student")
    public ResponseEntity<Object> registerNewStudent(@RequestBody StudentDTO student) {

        return ResponseEntity.status(HttpStatus.CREATED).body(studentService.addNewStudent(student));
    }

    @PutMapping("/student/{studentId}")
    public ResponseEntity<Object> updateStudent(
            @PathVariable("studentId") Long studentId,
            @RequestParam(name = "first-name", required = false) String firstName,
            @RequestParam(name = "last-name", required = false) String lastName,
            @RequestParam(name = "email", required = false) String email)
    {
        return ResponseEntity.status(HttpStatus.OK).body(studentService.updateStudent(studentId, firstName, lastName, email));
    }

    @DeleteMapping("/student/id/{studentId}")
    public ResponseEntity<Object> deleteStudent(@PathVariable("studentId") Long studentId) {
        studentService.deleteStudent(studentId);
        return ResponseEntity.status(HttpStatus.OK).body("Student with id " + studentId + " was excluded!");
    }


}
