package com.example.demo.controllers;

import com.example.demo.dto.StudentDTO;
import com.example.demo.services.StudentService;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("students")
    public ResponseEntity<Object> getAllStudents(
            @RequestParam(value="page", defaultValue = "0" )Integer page,
            @RequestParam(value="size", defaultValue = "10") Integer size){
        PageRequest pageRequest = PageRequest.of(page, size);
        return studentService.getAllStudents(pageRequest);
    }

    @GetMapping(path = "/student/{studentId}")
    public ResponseEntity<Object> getStudentById(@PathVariable("studentId") Long studentId) {
        return studentService.getStudentById(studentId);
    }

    @PostMapping("/student")
    public ResponseEntity<Object> registerNewStudent(@RequestBody StudentDTO student) {
        return studentService.addNewStudent(student);
    }

    @PutMapping("/student/{studentId}")
    public ResponseEntity<Object> updateStudent(
            @PathVariable("studentId") Long studentId,
            @RequestParam(name = "first_name", required = false) String firstName,
            @RequestParam(name = "last_name", required = false) String lastName,
            @RequestParam(name = "email", required = false) String email)
    {
        return studentService.updateStudent(studentId, firstName, lastName, email);
    }

    @DeleteMapping("/student/id/{studentId}")
    public ResponseEntity<Object> deleteStudent(@PathVariable("studentId") Long studentId) {
        return studentService.deleteStudent(studentId);
    }


}
