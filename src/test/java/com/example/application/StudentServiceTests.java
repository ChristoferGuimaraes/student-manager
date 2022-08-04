package com.example.application;

import com.example.demo.dto.CourseDTO;
import com.example.demo.dto.StudentDTO;
import com.example.demo.entities.CourseEntity;
import com.example.demo.entities.StudentEntity;
import com.example.demo.repositories.CourseRepository;
import com.example.demo.repositories.StudentRepository;

import com.example.demo.services.StudentService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;

import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class StudentServiceTests {

    private String firstName;
    private String lastName;
    private String email;

    private StudentService studentService;

    private StudentEntity studentEntity;
    private StudentDTO studentDTO;

    private CourseEntity courseEntity;
    private CourseDTO courseDTO;

    @Mock
    private static StudentRepository studentRepository;

    @Mock
    private static CourseRepository courseRepository;

    @Mock
    private ModelMapper modelMapper;

    @BeforeEach
    public void setup() {
        studentService = new StudentService(studentRepository, courseRepository, modelMapper);

        LocalDate birthDate = LocalDate.parse("1993-11-22");
        LocalDate createdAt = LocalDate.parse("2022-07-28");

        studentEntity = StudentEntity.builder()
                .id(1L)
                .firstName("Christofer")
                .lastName("Guimarães")
                .email("christofer.guimaraes@projuris.com.br")
                .age(28)
                .birthDate(birthDate)
                .createdAt(createdAt)
                .courses(List.of())
                .build();

        studentDTO = StudentDTO.builder()
                .id(studentEntity.getId())
                .firstName(studentEntity.getFirstName())
                .lastName(studentEntity.getLastName())
                .email(studentEntity.getEmail())
                .age(studentEntity.getAge())
                .birthDate(studentEntity.getBirthDate())
                .createdAt(studentEntity.getCreatedAt())
                .courses(List.of())
                .build();

        LocalDate startDate = LocalDate.parse("2021-11-22");

        courseEntity = CourseEntity.builder()
                .id(1)
                .name("POO")
                .teacherName("Johannes")
                .classNumber(3)
                .startDate(startDate)
                .build();

        courseDTO = CourseDTO.builder()
                .name(courseEntity.getName())
                .teacherName(courseEntity.getTeacherName())
                .classNumber(courseEntity.getClassNumber())
                .startDate(courseEntity.getStartDate())
                .build();


        firstName = "Test";
        lastName = "Testing";
        email = "test@test.com";

    }


    @Test
    public void shouldFindStudentInASize10Page_ReturnStatusCode200() {
        List<StudentEntity> list = Collections.singletonList(studentEntity);

        PageRequest page = PageRequest.of(0,10);

        Page<StudentEntity> studentPage = new PageImpl<>(list);

        doReturn(studentPage).when(studentRepository).findAll(page);

        ResponseEntity<Object> student = studentService.getAllStudents(page);

        assertEquals(HttpStatus.OK, student.getStatusCode());
    }


    @Test
    public void shouldNotFindStudentPage_ReturnStatusCode404() {
        PageRequest page = PageRequest.of(0,10);

        Page<StudentEntity> studentPage = Page.empty();

        doReturn(studentPage).when(studentRepository).findAll(page);

        ResponseEntity<Object> student = studentService.getAllStudents(page);

        assertEquals(HttpStatus.NOT_FOUND, student.getStatusCode());
    }


    @Test
    public void shouldExistsById_ReturnStatusCode200() {
        doReturn(true).when(studentRepository).existsById(1L);

        ResponseEntity<Object> student = studentService.getStudentById(studentDTO.getId());

        assertEquals(HttpStatus.OK, student.getStatusCode());
    }


    @Test
    public void shouldNotExistsById_ReturnStatusCode404() {
        doReturn(false).when(studentRepository).existsById(anyLong());

        ResponseEntity<Object> student = studentService.getStudentById(studentDTO.getId());

        assertEquals(HttpStatus.NOT_FOUND, student.getStatusCode());
    }


    @Test
    public void shouldAddStudentIfEmailAndCourseNotExistsInDbAndPersist_ReturnStatusCode201() {
        doReturn(false).when(studentRepository).existsStudentByEmail(studentEntity.getEmail());

        doReturn(studentEntity).when(studentRepository).save(studentEntity);

        ResponseEntity<Object> student = studentService.addNewStudent(studentDTO);

        verify(studentRepository, times(1)).save(studentEntity);

        assertEquals(HttpStatus.CREATED, student.getStatusCode());
    }


    @Test
    public void shouldNotAddStudentWhenEmailAlreadyExistsAndNotPersist_ReturnStatusCode400() {
        doReturn(true).when(studentRepository).existsStudentByEmail(studentEntity.getEmail());

        ResponseEntity<Object> student = studentService.addNewStudent(studentDTO);

        verify(studentRepository, never()).save(any());

        assertEquals(HttpStatus.BAD_REQUEST, student.getStatusCode());
    }


    @Test
    public void shouldDeleteStudentByIdAndPersistOneTime_ReturnStatusCode200() {
        doReturn(true).when(studentRepository).existsById(1L);

        ResponseEntity<Object> student = studentService.deleteStudent(1L);

        verify(studentRepository, times(1)).deleteById(1L);

        assertEquals(HttpStatus.OK, student.getStatusCode());
    }


    @Test
    public void shouldNotDeleteIfNotExistsByIdAndRepositoryIsNotCalled_ReturnStatusCode404() {
        doReturn(false).when(studentRepository).existsById(anyLong());

        ResponseEntity<Object> student = studentService.deleteStudent(anyLong());

        verify(studentRepository, never()).deleteById(anyLong());

        assertEquals(HttpStatus.NOT_FOUND, student.getStatusCode());
    }


    @Test
    public void shouldUpdateStudentWithAllArguments_ReturnStatusCode200() {
        Optional<StudentEntity> studentEntityOptional = Optional.of(studentEntity);
        doReturn(studentEntityOptional).when(studentRepository).findById(1L);

        Long id = studentEntityOptional.get().getId();

        ResponseEntity<Object> student = studentService.updateStudent(id, firstName, lastName, email);

        assertEquals(studentEntityOptional.get().getFirstName(), firstName);
        assertEquals(studentEntityOptional.get().getLastName(), lastName);
        assertEquals(studentEntityOptional.get().getEmail(), email);

        assertEquals(HttpStatus.OK, student.getStatusCode());
    }


    @Test
    public void shouldNotUpdateStudentWhenNotFindStudentById_ReturnStatusCode404() {
        doReturn(Optional.empty()).when(studentRepository).findById(anyLong());

        ResponseEntity<Object> student = studentService.updateStudent(anyLong(), firstName, lastName, email);

        assertEquals(HttpStatus.NOT_FOUND, student.getStatusCode());
    }


    @Test
    public void shouldNotPassIfEmailIsEqual_ReturnStatusCode400() {
        doReturn(Optional.of(studentEntity)).when(studentRepository).findById(1L);

        String existEmail = "christofer.guimaraes@projuris.com.br";

        ResponseEntity<Object> student = studentService.updateStudent(1L, firstName, lastName, existEmail);

        assertEquals(studentEntity.getEmail(), existEmail);
        assertEquals(HttpStatus.BAD_REQUEST, student.getStatusCode());
    }


    @Test
    public void shouldNotPassIfEmailLengthIsMoreThan10Char_ReturnStatusCode400() {
        doReturn(Optional.of(studentEntity)).when(studentRepository).findById(1L);

        String invalidEmail = "email@br";

        ResponseEntity<Object> student = studentService.updateStudent(1L, firstName, lastName, invalidEmail);

        assertNotEquals(invalidEmail, studentEntity.getEmail());
        assertEquals(HttpStatus.BAD_REQUEST, student.getStatusCode());
    }


    @Test
    public void shouldNotPassIfCourseIsAlreadyInDbAndNotPersist_ReturnStatusCode400() {
        studentDTO.setCourses(List.of(courseDTO));

        doReturn(false).when(studentRepository).existsStudentByEmail(studentDTO.getEmail());

        doReturn(Optional.of(courseEntity)).when(courseRepository).findByNameIgnoreCase(courseEntity.getName());
        ResponseEntity<Object> student = studentService.addNewStudent(studentDTO);

        verify(studentRepository, never()).save(studentEntity);

        assertEquals(studentDTO.getCourses().size(), 1);
        assertEquals(HttpStatus.BAD_REQUEST, student.getStatusCode());
    }

}

