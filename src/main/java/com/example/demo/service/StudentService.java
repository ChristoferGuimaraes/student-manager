package com.example.demo.service;

import com.example.demo.dto.StudentDTO;
import com.example.demo.entity.StudentEntity;
import com.example.demo.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }


    public List<StudentEntity> getStudents() {
        return studentRepository.findAll();
    }

    public Optional<StudentEntity> getStudent(Long studentId) {
        boolean exists = studentRepository.existsById(studentId);
        if (!exists) {
            throw new IllegalStateException("Student with id " + studentId + " does not exists!");
        }

        return studentRepository.findById(studentId);
    }

    public ResponseEntity<StudentEntity> addNewStudent(StudentEntity student) {
        Optional<StudentEntity> studentByEmail = studentRepository.findStudentByEmail(student.getEmail());

        if (studentByEmail.isPresent()) {
            throw new IllegalStateException("This e-mail is already taken!");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(studentRepository.save(student));
    }

    public void deleteStudent(Long studentId) {
        boolean exists = studentRepository.existsById(studentId);
        if (!exists) {
            throw new IllegalStateException("Student with id " + studentId + "does not exists!");
        }

        studentRepository.deleteById(studentId);
    }

    @Transactional
    public void updateStudent(Long studentId, String firstName, String lastName, String email) {
        StudentEntity studentEntity = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalStateException("Student with id " + studentId + " does not exists!"));
        StudentDTO studentDTO = new StudentDTO(studentEntity);

        if (firstName != null && firstName.length() > 0 && !Objects.equals(studentDTO.getFirstName(), firstName)) {
            studentDTO.setFirstName(firstName);
        }

        if (lastName != null && lastName.length() > 0 && !Objects.equals(studentDTO.getLastName(), lastName)) {
            studentDTO.setLastName(lastName);
        }

        if (email != null && email.length() > 0 && !Objects.equals(studentDTO.getEmail(), email)) {
            Optional<StudentEntity> studentOptional = studentRepository. findStudentByEmail(email);
            if (studentOptional.isPresent()) {
                throw new IllegalStateException("This e-mail is already taken!");
            }
            studentDTO.setEmail(email);
        }

    }
}
