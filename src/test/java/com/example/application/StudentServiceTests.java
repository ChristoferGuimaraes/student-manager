package com.example.application;

import com.example.demo.dto.StudentDTO;
import com.example.demo.entities.StudentEntity;
import com.example.demo.repositories.CourseRepository;
import com.example.demo.repositories.StudentRepository;

import com.example.demo.services.StudentService;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import org.mockito.*;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


public class StudentServiceTests {

    private String firstName;
    private String lastName;
    private String email;

    private StudentService studentService;

    private StudentEntity studentEntity;
    private StudentDTO studentDTO;

    @Mock
    private static StudentRepository studentRepository;

    @Mock
    private static CourseRepository courseRepository;

    @Mock
    private ModelMapper modelMapper;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
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

        firstName = "Test";
        lastName = "Testing";
        email = "test@test.com";

    }

    @Test
    public void shouldPersistWhenEmailAndCourseNotExistInDatabase_ReturnStatusCode201() {
        doReturn(false).when(studentRepository).existsStudentByEmail(anyString());
        doReturn(Optional.empty()).when(courseRepository).findByNameIgnoreCase(anyString());
        doReturn(studentEntity).when(studentRepository).save(any());

        ResponseEntity<Object> student = studentService.addNewStudent(studentDTO);

        verify(studentRepository, times(1)).save(any());

        Assert.assertEquals(HttpStatus.CREATED, student.getStatusCode());
    }


    @Test
    public void shouldNotPersistWheEmailAlreadyExists_ReturnStatusCode400() {
        doReturn(true).when(studentRepository).existsStudentByEmail(anyString());

        ResponseEntity<Object> student = studentService.addNewStudent(studentDTO);

        verify(studentRepository, never()).save(any());

        Assert.assertEquals(HttpStatus.BAD_REQUEST, student.getStatusCode());
    }


    @Test
    public void shouldReturnAllStudents_ReturnStatusCode200() {
        List<StudentEntity> list = Collections.singletonList(studentEntity);

        PageRequest page = PageRequest.of(0,10);

        Page<StudentEntity> studentPage = new PageImpl<>(list);

        doReturn(studentPage).when(studentRepository).findAll(page);

        ResponseEntity<Object> student = studentService.getAllStudents(page);

        Assert.assertEquals(HttpStatus.OK, student.getStatusCode());
    }


    @Test
    public void shouldReturnStatusCode404_WhenPageIsNotFound() {
        PageRequest page = PageRequest.of(0,10);

        Page<StudentEntity> studentPage = Page.empty();

        doReturn(studentPage).when(studentRepository).findAll(page);

        ResponseEntity<Object> student = studentService.getAllStudents(page);

        Assert.assertEquals(HttpStatus.NOT_FOUND, student.getStatusCode());
    }


    @Test
    public void shouldReturnStatusCode200_WhenExistsById() {
        doReturn(true).when(studentRepository).existsById(anyLong());

        ResponseEntity<Object> student = studentService.getStudentById(studentDTO.getId());

        Assert.assertEquals(HttpStatus.OK, student.getStatusCode());
    }


    @Test
    public void shouldReturnStatusCode400_WhenDoNotExistsById() {
        doReturn(false).when(studentRepository).existsById(anyLong());

        ResponseEntity<Object> student = studentService.getStudentById(studentDTO.getId());

        Assert.assertEquals(HttpStatus.NOT_FOUND, student.getStatusCode());
    }


    @Test
    public void shouldVerifyIfWasCalledDeleteByIdOneTime_ReturnStatusCode200() {
        doReturn(true).when(studentRepository).existsById(anyLong());

        ResponseEntity<Object> student = studentService.deleteStudent(anyLong());

        verify(studentRepository, times(1)).deleteById(anyLong());

        Assert.assertEquals(HttpStatus.OK, student.getStatusCode());
    }


    @Test
    public void shouldVerifyIfRepositoryIsNotCalled_ReturnStatusCode404() {
        doReturn(false).when(studentRepository).existsById(anyLong());

        ResponseEntity<Object> student = studentService.deleteStudent(anyLong());

        verify(studentRepository, never()).deleteById(anyLong());

        Assert.assertEquals(HttpStatus.NOT_FOUND, student.getStatusCode());
    }


    @Test
    public void shouldUpdateStudentByAllArguments_ReturnStatusCode200() {
        Optional<StudentEntity> studentEntityOptional = Optional.of(studentEntity);
        doReturn(studentEntityOptional).when(studentRepository).findById(anyLong());

        Long id = studentEntityOptional.get().getId();

        ResponseEntity<Object> student = studentService.updateStudent(id, firstName, lastName, email);

        Assert.assertEquals(studentEntityOptional.get().getFirstName(), firstName);
        Assert.assertEquals(studentEntityOptional.get().getLastName(), lastName);
        Assert.assertEquals(studentEntityOptional.get().getEmail(), email);

        Assert.assertEquals(HttpStatus.OK, student.getStatusCode());
    }


    @Test
    public void shouldNotFindStudent_ReturnStatusCode404() {
        doReturn(Optional.empty()).when(studentRepository).findById(anyLong());

        ResponseEntity<Object> student = studentService.updateStudent(anyLong(), firstName, lastName, email);

        Assert.assertEquals(HttpStatus.NOT_FOUND, student.getStatusCode());
    }


    @Test
    public void shouldVerifyEmailIsEqual_ReturnStatusCode400() {
        Optional<StudentEntity> studentEntityOptional = Optional.of(studentEntity);
        doReturn(studentEntityOptional).when(studentRepository).findById(anyLong());

        String existEmail = "christofer.guimaraes@projuris.com.br";

        ResponseEntity<Object> student = studentService.updateStudent(anyLong(), firstName, lastName, existEmail);

        Assert.assertEquals(studentEntityOptional.get().getEmail(), existEmail);
        Assert.assertEquals(HttpStatus.BAD_REQUEST, student.getStatusCode());
    }


    @Test
    public void shouldVerifyIfEmailLengthIsMoreThan10Char_ReturnStatusCode400() {
        Optional<StudentEntity> studentEntityOptional = Optional.of(studentEntity);
        doReturn(studentEntityOptional).when(studentRepository).findById(anyLong());

        String invalidEmail = "email@it";

        ResponseEntity<Object> student = studentService.updateStudent(anyLong(), firstName, lastName, invalidEmail);


        Assert.assertNotEquals(invalidEmail, studentEntityOptional.get().getEmail());
        Assert.assertEquals(HttpStatus.BAD_REQUEST, student.getStatusCode());

    }

}

