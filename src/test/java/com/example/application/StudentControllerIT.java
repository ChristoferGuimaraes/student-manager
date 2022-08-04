package com.example.application;

import com.example.demo.Application;
import com.example.demo.controllers.StudentController;
import com.example.demo.dto.StudentDTO;
import com.example.demo.entities.StudentEntity;
import com.example.demo.repositories.CourseRepository;
import com.example.demo.repositories.StudentRepository;
import com.example.demo.services.StudentService;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.Collections;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("tests")
@ContextConfiguration(classes = {Application.class})
public class StudentControllerIT {

    private StudentEntity studentEntity;
    private StudentDTO studentDTO;

    @Autowired
    private StudentService studentService;

    @Autowired
    private StudentRepository studentRepository;

    private ObjectMapper objectMapper;
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(new StudentController(studentService)).build();

        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        LocalDate birthDate = LocalDate.parse("1993-11-22");

        studentEntity = StudentEntity.builder()
                .firstName("Christofer")
                .lastName("Guimarães")
                .email("christofer.guimaraes@projuris.com.br")
                .birthDate(birthDate)
                .courses(Collections.emptyList())
                .build();

        studentDTO = StudentDTO.builder()
                .firstName(studentEntity.getFirstName())
                .lastName(studentEntity.getLastName())
                .email(studentEntity.getEmail())
                .birthDate(studentEntity.getBirthDate())
                .courses(Collections.emptyList())
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
        studentRepository.deleteAll();
    }

}
