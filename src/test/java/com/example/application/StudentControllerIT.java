package com.example.application;

import com.example.demo.controllers.StudentController;
import com.example.demo.dto.StudentDTO;
import com.example.demo.entities.StudentEntity;
import com.example.demo.repositories.CourseRepository;
import com.example.demo.repositories.StudentRepository;
import com.example.demo.services.StudentService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@Transactional
public class StudentControllerIT {

    private StudentEntity studentEntity;
    private StudentDTO studentDTO;

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    StudentService studentService;

    @Autowired
    StudentRepository studentRepository;


    private ObjectMapper objectMapper;
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(new StudentController(studentService)).build();

        studentEntity = StudentEntity.builder()
                .firstName("Christofer")
                .lastName("Guimarães")
                .email("christofer.guimaraes@projuris.com.br")
                .birthDate(LocalDate.parse("1993-11-22"))
                .build();

        studentDTO = StudentDTO.builder()
                .firstName(studentEntity.getFirstName())
                .lastName(studentEntity.getLastName())
                .email(studentEntity.getEmail())
                .birthDate(studentEntity.getBirthDate())
                .build();

    }

    @Test
    public void insertStudent() throws Exception {

        mockMvc.perform(post("/api/student")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsBytes(studentDTO)))
                .andExpect(status().isCreated());
        assertEquals(1, studentRepository.findAll().size());
    }

    @Test
    public void findAllStudents() throws Exception {
        studentRepository.save(studentEntity);
        studentEntity.setEmail("christoferguiam@gmail.com");
        studentRepository.save(studentEntity);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/students")
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.totalElements", notNullValue()))
                .andExpect(jsonPath("$.totalPages", notNullValue()));
    }

}
