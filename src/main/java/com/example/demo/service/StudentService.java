package com.example.demo.service;

import com.example.demo.dto.StudentDTO;
import com.example.demo.entity.StudentEntity;
import com.example.demo.repository.StudentRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final ModelMapper modelMapper;

    public StudentService(StudentRepository studentRepository, ModelMapper modelMapper) {
        this.studentRepository = studentRepository;
        this.modelMapper = modelMapper;
    }

    // Do the conversion of an Entity to a DTO
    private StudentDTO toStudentDTO(StudentEntity studentEntity) {
        return modelMapper.map(studentEntity, StudentDTO.class);
    }

    public List<StudentDTO> getStudents() {
        return studentRepository.findAll()
                .stream()
                .map(this::toStudentDTO)
                .collect(Collectors.toList());
    }

    public Optional<StudentDTO> getStudent(Long studentId) {
        boolean exists = studentRepository.existsById(studentId);
        if (!exists) {
            throw new IllegalStateException("Student with id " + studentId + " does not exists!");
        }

        return studentRepository.findById(studentId).map(this::toStudentDTO);
    }

    public StudentEntity addNewStudent(StudentDTO student) {
        Optional<StudentDTO> studentByEmail = studentRepository.findStudentByEmail(student.getEmail()).map(this::toStudentDTO);

        if (studentByEmail.isPresent()) {
            throw new IllegalStateException("This e-mail is already taken!");
        }

        StudentEntity studentEntity = new StudentEntity(student);

        return studentRepository.save(studentEntity);
    }

    @Transactional
    public void deleteStudent(Long studentId) {
        boolean exists = studentRepository.existsById(studentId);
        if (!exists) {
            throw new IllegalStateException("Student with id " + studentId + "does not exists!");
        }
        studentRepository.deleteById(studentId);

    }

    @Transactional
    public StudentDTO updateStudent(Long studentId, String firstName, String lastName, String email) {
        StudentEntity studentEntity = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalStateException("Student with id " + studentId + " does not exists!"));

        if (firstName != null && firstName.length() > 0 && !Objects.equals(studentEntity.getFirstName(), firstName)) {
            studentEntity.setFirstName(firstName);
        }

        if (lastName != null && lastName.length() > 0 && !Objects.equals(studentEntity.getLastName(), lastName)) {
            studentEntity.setLastName(lastName);
        }

        if (email != null && email.length() > 0) {
            if (Objects.equals(studentEntity.getEmail(), email)) {
                throw new IllegalStateException("This e-mail is already taken!");
            }

            studentEntity.setEmail(email);
        }

        return toStudentDTO(studentEntity);
    }
}
