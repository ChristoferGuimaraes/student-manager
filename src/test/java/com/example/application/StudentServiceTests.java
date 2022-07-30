package com.example.application;

import com.example.demo.dto.StudentDTO;
import com.example.demo.entities.StudentEntity;
import com.example.demo.repositories.StudentRepository;

import com.example.demo.services.StudentService;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import org.mockito.*;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.List;


public class StudentServiceTests {

    private StudentService studentService;
    private StudentEntity studentEntity;
    private StudentDTO studentDTO;

    @Mock
    private static StudentRepository repository;

    @Mock
    private ModelMapper modelMapper;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
        studentService = new StudentService(repository, modelMapper);

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
    }

    @Test
    public void shouldVerifyRepositoryPersistenceAndReturnStatusCodeCreated() {
        Mockito.doReturn(false).when(repository).existsStudentByEmail(any());
        Mockito.doReturn(studentEntity).when(repository).save(any());

        ResponseEntity<Object> student = studentService.addNewStudent(studentDTO);

        Mockito.verify(repository, Mockito.times(1)).save(any());

        Assert.assertEquals(HttpStatus.CREATED, student.getStatusCode());
    }

    @Test
    public void shouldVerifyIfNotPersistenceAndReturnStatusCodeBadRequest() {
        Mockito.doReturn(true).when(repository).existsStudentByEmail(any());

        ResponseEntity<Object> student = studentService.addNewStudent(studentDTO);

        Mockito.verify(repository, Mockito.never()).save(any());

        Assert.assertEquals(HttpStatus.BAD_REQUEST, student.getStatusCode());
    }


    @Test
    public void shouldReturnStatusCodeOkWhenExistsById() {
        Mockito.doReturn(true).when(repository).existsById(any());

        ResponseEntity<Object> student = studentService.getStudentById(studentDTO.getId());

        Assert.assertEquals(HttpStatus.OK, student.getStatusCode());
    }

    @Test
    public void shouldReturnStatusCodeBadRequestWhenDoNotExistsById() {
        Mockito.doReturn(false).when(repository).existsById(any());

        ResponseEntity<Object> student = studentService.getStudentById(studentDTO.getId());

        Assert.assertEquals(HttpStatus.NOT_FOUND, student.getStatusCode());
    }

}

