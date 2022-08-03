package com.example.application;

import com.example.demo.dto.CourseDTO;
import com.example.demo.entities.CourseEntity;
import com.example.demo.repositories.CourseRepository;
import com.example.demo.services.CourseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


public class CourseServiceTests {

    private String courseName;
    private String teacherName;
    private Integer classNumber;

    private CourseService courseService;
    private CourseEntity courseEntity;
    private CourseDTO courseDTO;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private ModelMapper modelMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        courseService = new CourseService(courseRepository, modelMapper);

        LocalDate startDate = LocalDate.parse("2021-11-22");

        courseEntity = CourseEntity.builder()
                .id(1)
                .name("POO")
                .teacherName("Jean Poiski")
                .classNumber(5)
                .startDate(startDate)
                .build();

        courseDTO = CourseDTO.builder()
                .name(courseEntity.getName())
                .teacherName(courseEntity.getTeacherName())
                .classNumber(courseEntity.getClassNumber())
                .startDate(courseEntity.getStartDate())
                .build();

        courseName = "Teste";
        teacherName = "Testing";
        classNumber = 2;
    }


    @Test
    public void shouldFindCourseInASize10Page_ReturnStatusCode200() {
        List<CourseEntity> list = Collections.singletonList(courseEntity);

        PageRequest page = PageRequest.of(0,10);

        Page<CourseEntity> coursePage = new PageImpl<>(list);

        doReturn(coursePage).when(courseRepository).findAll(page);

        ResponseEntity<Object> course = courseService.getAllCourses(page);

        assertEquals(HttpStatus.OK, course.getStatusCode());
    }


    @Test
    public void shouldNotFindCoursePage_ReturnStatusCode404() {
        PageRequest page = PageRequest.of(0,10);

        Page<CourseEntity> coursePage = Page.empty();

        doReturn(coursePage).when(courseRepository).findAll(page);

        ResponseEntity<Object> course = courseService.getAllCourses(page);

        assertEquals(HttpStatus.NOT_FOUND, course.getStatusCode());
    }


    @Test
    public void shouldFindCourseByName_ReturnStatusCode200() {
        doReturn(Optional.of(courseEntity)).when(courseRepository).findByNameIgnoreCase(courseEntity.getName());

        ResponseEntity<Object> course = courseService.findCourseByName(courseDTO.getName());

        assertEquals(HttpStatus.OK, course.getStatusCode());
    }


    @Test
    public void shouldNotFindCourseByName_ReturnStatusCode404() {
        doReturn(Optional.empty()).when(courseRepository).findByNameIgnoreCase(courseDTO.getName());

        ResponseEntity<Object> course = courseService.findCourseByName(courseDTO.getName());

        assertEquals(HttpStatus.NOT_FOUND, course.getStatusCode());
    }


    @Test
    public void shouldPassIfFieldsAreNotNullOrBlankAndPersistOneTimeInDb_ReturnStatusCode200() {
        doReturn(Optional.empty()).when(courseRepository).findByNameIgnoreCase(courseDTO.getName());

        ResponseEntity<Object> course = courseService.addNewCourse(courseDTO);

        verify(courseRepository, times(1)).save(any());

        assertEquals(HttpStatus.CREATED, course.getStatusCode());
    }


    @Test
    public void shouldNotPersistIfCourseNameIsAlreadyInDb_ReturnStatusCode400() {
        doReturn(Optional.of(courseDTO)).when(courseRepository).findByNameIgnoreCase(courseDTO.getName());

        ResponseEntity<Object> course = courseService.addNewCourse(courseDTO);

        verify(courseRepository, never()).save(any());

        assertEquals(HttpStatus.BAD_REQUEST, course.getStatusCode());
    }


    @Test
    public void shouldNotPersistIfCourseNameIsNull_ReturnStatusCode400() {
        courseDTO.setName(null);

        doReturn(Optional.empty()).when(courseRepository).findByNameIgnoreCase(courseDTO.getName());

        ResponseEntity<Object> course = courseService.addNewCourse(courseDTO);

        verify(courseRepository, never()).save(any());

        assertEquals(HttpStatus.BAD_REQUEST, course.getStatusCode());
    }


    @Test
    public void shouldNotPersistIfCourseNameIsBlank_ReturnStatusCode400() {
        courseDTO.setName(" ");

        doReturn(Optional.empty()).when(courseRepository).findByNameIgnoreCase(courseDTO.getName());

        ResponseEntity<Object> course = courseService.addNewCourse(courseDTO);

        verify(courseRepository, never()).save(any());

        assertEquals(HttpStatus.BAD_REQUEST, course.getStatusCode());
    }


    @Test
    public void shouldNotPersistIfTeacherNameIsNull_ReturnStatusCode400() {
        courseDTO.setTeacherName(null);

        doReturn(Optional.empty()).when(courseRepository).findByNameIgnoreCase(courseDTO.getName());

        ResponseEntity<Object> course = courseService.addNewCourse(courseDTO);

        verify(courseRepository, never()).save(any());

        assertEquals(HttpStatus.BAD_REQUEST, course.getStatusCode());
    }


    @Test
    public void shouldNotPersistIfTeacherNameIsBlank_ReturnStatusCode400() {
        courseDTO.setTeacherName(" ");

        doReturn(Optional.empty()).when(courseRepository).findByNameIgnoreCase(courseDTO.getName());

        ResponseEntity<Object> course = courseService.addNewCourse(courseDTO);

        verify(courseRepository, never()).save(any());

        assertEquals(HttpStatus.BAD_REQUEST, course.getStatusCode());
    }


    @Test
    public void shouldNotPersistIfClassNumberIsNull_ReturnStatusCode400() {
        courseDTO.setClassNumber(null);

        doReturn(Optional.empty()).when(courseRepository).findByNameIgnoreCase(courseDTO.getName());

        ResponseEntity<Object> course = courseService.addNewCourse(courseDTO);

        verify(courseRepository, never()).save(any());

        assertEquals(HttpStatus.BAD_REQUEST, course.getStatusCode());
    }


    @Test
    public void shouldNotPersistIfStartDateIsNull_ReturnStatusCode400() {
        courseDTO.setStartDate(null);

        doReturn(Optional.empty()).when(courseRepository).findByNameIgnoreCase(courseDTO.getName());

        ResponseEntity<Object> course = courseService.addNewCourse(courseDTO);

        verify(courseRepository, never()).save(any());

        assertEquals(HttpStatus.BAD_REQUEST, course.getStatusCode());
    }


    @Test
    public void shouldUpdateCourseWithAllArguments_ReturnStatusCode200() {
        doReturn(Optional.of(courseEntity)).when(courseRepository).findByNameIgnoreCase(courseEntity.getName());

        String name = courseEntity.getName();

        doReturn(Optional.empty()).when(courseRepository).findByNameIgnoreCase(courseName);

        ResponseEntity<Object> course = courseService.updateCourse(name, courseName, teacherName, classNumber);

        assertEquals(courseEntity.getName(), courseName);
        assertEquals(courseEntity.getTeacherName(), teacherName);
        assertEquals(courseEntity.getClassNumber(), classNumber);

        assertEquals(HttpStatus.OK, course.getStatusCode());
    }


    @Test
    public void shouldNotUpdateCourseIfCourseNameIsTheSame_ReturnStatusCode400() {
        doReturn(Optional.of(courseEntity)).when(courseRepository).findByNameIgnoreCase(courseEntity.getName());

        String name = courseEntity.getName();

        doReturn(Optional.of(courseEntity)).when(courseRepository).findByNameIgnoreCase(courseName);

        ResponseEntity<Object> course = courseService.updateCourse(name, courseName, teacherName, classNumber);

        assertEquals(HttpStatus.BAD_REQUEST, course.getStatusCode());
    }

    @Test
    public void shouldNotUpdateCourseIfCourseNameIsNotInDb_ReturnStatusCode404() {
        doReturn(Optional.empty()).when(courseRepository).findByNameIgnoreCase(courseEntity.getName());

        String name = courseEntity.getName();

        ResponseEntity<Object> course = courseService.updateCourse(name, courseName, teacherName, classNumber);

        assertEquals(HttpStatus.NOT_FOUND, course.getStatusCode());
    }


    @Test
    public void shouldFindCourseByNameAndDeleteById_ReturnStatusCode200() {
        doReturn(Optional.of(courseEntity)).when(courseRepository).findByNameIgnoreCase(courseEntity.getName());

        ResponseEntity<Object> course = courseService.deleteCourseByName(courseEntity.getName());

        verify(courseRepository, times(1)).deleteById(courseEntity.getId());

        assertEquals(HttpStatus.OK, course.getStatusCode());
    }


    @Test
    public void shouldNotFindCourseByNameAndNotCallDeleteByIdMethod_ReturnStatusCode404() {
        doReturn(Optional.empty()).when(courseRepository).findByNameIgnoreCase(courseEntity.getName());

        ResponseEntity<Object> course = courseService.deleteCourseByName(courseEntity.getName());

        verify(courseRepository, never()).deleteById(any());

        assertEquals(HttpStatus.NOT_FOUND, course.getStatusCode());
    }

}
