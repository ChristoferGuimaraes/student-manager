package com.guimaraes.application.controllers;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.guimaraes.studentmanager.StudentManagerApp;
import com.guimaraes.studentmanager.controllers.CourseController;
import com.guimaraes.studentmanager.dto.CourseDTO;
import com.guimaraes.studentmanager.entities.CourseEntity;
import com.guimaraes.studentmanager.repositories.CourseRepository;
import com.guimaraes.studentmanager.services.CourseService;
import org.junit.jupiter.api.AfterEach;
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
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
@ContextConfiguration(classes = StudentManagerApp.class)
@Transactional
public class CourseControllerIT {

    @Autowired
    private CourseService courseService;

    @Autowired
    private CourseRepository courseRepository;

    private ObjectMapper objectMapper;
    private MockMvc mockMvc;

    private CourseEntity courseEntity;
    private CourseDTO courseDTO;

    @BeforeEach
    public void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(new CourseController(courseService)).build();

        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        courseEntity = CourseEntity.builder()
                .name("Java")
                .teacherName("Jean Poiski")
                .classNumber(2)
                .startDate(LocalDate.parse("2022-04-11"))
                .build();

        courseDTO = CourseDTO.builder()
                .name(courseEntity.getName())
                .teacherName(courseEntity.getTeacherName())
                .classNumber(courseEntity.getClassNumber())
                .startDate(courseEntity.getStartDate())
                .build();

        courseRepository.save(courseEntity);

    }

    @AfterEach
    public void tearDown() {
        courseRepository.deleteAll();
    }

    @Test
    public void insertCourse() throws Exception {
        courseRepository.deleteAll();
        mockMvc.perform(post("/api/course")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsBytes(courseDTO)))
                .andExpect(status().isCreated());
        assertEquals(1, courseRepository.findAll().size());
    }

    @Test
    public void findAllCourses() throws Exception {
        courseEntity.setName("POO");
        courseRepository.save(courseEntity);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/courses")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.totalElements", notNullValue()))
                .andExpect(jsonPath("$.totalPages", notNullValue()));
    }


    @Test
    public void findCourseByName() throws Exception {
        mockMvc.perform(get("/api/course/{courseName}", courseEntity.getName())
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));
    }


    @Test
    public void deleteCourseByName() throws Exception {
        mockMvc.perform(delete("/api/course/{courseName}", courseEntity.getName())
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
        assertEquals(0, courseRepository.findAll().size());
    }


    @Test
    public void updateCourseByName() throws Exception {
        String courseName = "POO";
        String teacherName = "Johanes Neumann";
        Integer classNumber = 3;



        mockMvc.perform(put("/api/course/{courseName}", courseEntity.getName())
                        .sessionAttr("courseEntity", courseEntity)
                        .param("name", courseName)
                        .param("teacher_name", teacherName)
                        .param("class_number", String.valueOf(classNumber)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));

        CourseEntity course = courseRepository.findByNameIgnoreCase(courseEntity.getName()).get();

        assertEquals(courseName, course.getName());
        assertEquals(teacherName, course.getTeacherName());
        assertEquals(classNumber, course.getClassNumber());
    }

}
