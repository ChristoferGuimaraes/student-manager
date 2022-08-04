package com.example.demo.services;

import com.example.demo.dto.PayloadErrorDTO;
import com.example.demo.dto.StudentDTO;
import com.example.demo.entities.CourseEntity;
import com.example.demo.entities.StudentEntity;
import com.example.demo.repositories.CourseRepository;
import com.example.demo.repositories.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
@Transactional
@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final ModelMapper modelMapper;


    // Do the conversion of an Entity to a DTO
    private StudentDTO toStudentDTO(StudentEntity studentEntity) {
        return modelMapper.map(studentEntity, StudentDTO.class);
    }


    @Transactional(readOnly = true)
    public ResponseEntity<Object> getAllStudents(PageRequest pageRequest) {
        Page<Object> page = studentRepository.findAll(pageRequest).map(this::toStudentDTO);

        if (page.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new PayloadErrorDTO("This page is empty!"));
        }
        return ResponseEntity.status(HttpStatus.OK).body(page);
    }


    @Transactional(readOnly = true)
    public ResponseEntity<Object> getStudentById(Long studentId) {
        boolean exists = studentRepository.existsById(studentId);
        if (!exists) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new PayloadErrorDTO("Student with id " + studentId + " does not exists!"));
        }
        Optional<StudentDTO> studentDTO = studentRepository.findById(studentId).map(this::toStudentDTO);
        return ResponseEntity.status(HttpStatus.OK).body(studentDTO);
    }


    public ResponseEntity<Object> addNewStudent(@NotNull StudentDTO student) {
        Boolean studentByEmailExists = studentRepository.existsStudentByEmail(student.getEmail());

        if (studentByEmailExists) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new PayloadErrorDTO("This e-mail is already in use!"));
        }

        if (!student.getCourses().isEmpty()) {
            for (int i = 0; i < student.getCourses().size(); i++) {
                Optional<CourseEntity> nameCourse = courseRepository.
                        findByNameIgnoreCase(student.getCourses().get(i).getName());
                if (nameCourse.isPresent()) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body(new PayloadErrorDTO("Course " + nameCourse.get().getName() + " already registered!"));
                }
            }
        }

        StudentEntity studentEntity = new StudentEntity(student);

        studentRepository.save(studentEntity);

        return ResponseEntity.status(HttpStatus.CREATED).body(toStudentDTO(studentEntity));
    }


    public ResponseEntity<Object> deleteStudent(Long studentId) {
        boolean exists = studentRepository.existsById(studentId);
        if (!exists) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new PayloadErrorDTO("Student with id " + studentId + " does not exists!"));
        }
        studentRepository.deleteById(studentId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new PayloadErrorDTO("Student with id " + studentId + " was excluded!"));
    }


    public ResponseEntity<Object> updateStudent(Long studentId, String firstName, String lastName, String email) {
        Optional<StudentEntity> studentEntity = studentRepository.findById(studentId);

        if (studentEntity.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new PayloadErrorDTO("Student with id " + studentId + " does not exists!"));
        }

        if (firstName != null && firstName.length() > 0
                && !Objects.equals(studentEntity.get().getFirstName(), firstName)) {
            studentEntity.get().setFirstName(firstName);
        }

        if (lastName != null && lastName.length() > 0
                && !Objects.equals(studentEntity.get().getLastName(), lastName)) {
            studentEntity.get().setLastName(lastName);
        }

        if (email != null && email.length() <= 10) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new PayloadErrorDTO("Invalid e-mail! Min. 10 characters!"));
        }

        if (Objects.equals(studentEntity.get().getEmail(), email)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new PayloadErrorDTO("This e-mail is already in use!"));
        }

        if (email != null) {
            studentEntity.get().setEmail(email);
        }

        return ResponseEntity.status(HttpStatus.OK).body(toStudentDTO(studentEntity.get()));
    }
}