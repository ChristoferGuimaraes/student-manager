package com.guimaraes.studentmanager.services;

import com.guimaraes.studentmanager.dto.CourseDTO;
import com.guimaraes.studentmanager.dto.PayloadErrorDTO;
import com.guimaraes.studentmanager.entities.CourseEntity;
import com.guimaraes.studentmanager.repositories.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Transactional
@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final ModelMapper modelMapper;


    private CourseDTO toCourseDTO(CourseEntity courseEntity) {
        return modelMapper.map(courseEntity, CourseDTO.class);
    }


    @Transactional(readOnly = true)
    public ResponseEntity<Object> getAllCourses(PageRequest pageRequest) {
        Page<Object> page = courseRepository.findAll(pageRequest).map(this::toCourseDTO);

        if (page.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new PayloadErrorDTO("This page is empty!"));
        }

        return ResponseEntity.status(HttpStatus.OK).body(page);
    }


    @Transactional(readOnly = true)
    public ResponseEntity<Object> findCourseByName(@NotNull String name) {
        String replaceName = name.replaceAll("\\+", " ");
        Optional<CourseEntity> course = courseRepository.findByNameIgnoreCase(replaceName);

        if (course.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new PayloadErrorDTO("Course '" + replaceName + "' does not exists in database!"));
        }
        return ResponseEntity.status(HttpStatus.OK).body(course.stream().map(this::toCourseDTO));
    }


    public ResponseEntity<Object> addNewCourse(@NotNull CourseDTO courseDTO) {
        Optional<CourseEntity> course = courseRepository.findByNameIgnoreCase(courseDTO.getName());

        if (course.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new PayloadErrorDTO("Course '" + courseDTO.getName() + "' is already registered!"));
        }

        if (courseDTO.getName() == null || courseDTO.getName().isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new PayloadErrorDTO("One or more fields are blank or null!"));
        }

        if (courseDTO.getTeacherName() == null || courseDTO.getTeacherName().isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new PayloadErrorDTO("One or more fields are blank or null!"));
        }

        if (courseDTO.getClassNumber() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new PayloadErrorDTO("One or more fields are blank or null!"));
        }

        if (courseDTO.getStartDate() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new PayloadErrorDTO("One or more fields are blank or null!"));
        }

        CourseEntity courseEntity = new CourseEntity(courseDTO);
        courseRepository.save(courseEntity);

        return ResponseEntity.status(HttpStatus.CREATED).body(courseDTO);
    }


    public ResponseEntity<Object> deleteCourseByName(@NotNull String courseName) {
        String replaceName = courseName.replaceAll("\\+", " ");
        Optional<CourseEntity> course = courseRepository.findByNameIgnoreCase(replaceName);

        if (course.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new PayloadErrorDTO("Course '" + replaceName + "' does not exists in database!"));
        }

        courseRepository.deleteById(course.get().getId());

        return ResponseEntity.status(HttpStatus.OK)
                .body(new PayloadErrorDTO("Course '" + course.get().getName() + "' deleted successfully!"));
    }


    public ResponseEntity<Object> updateCourse(String name, String courseName, String teacherName, Integer classNumber) {
        String replaceName = name.replaceAll("\\+", " ");
        Optional<CourseEntity> courseEntity = courseRepository.findByNameIgnoreCase(replaceName);
        Optional<CourseEntity> courseNameParam = courseRepository.findByNameIgnoreCase(courseName);

        if (courseEntity.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new PayloadErrorDTO("Course '" + name + "' does not exists in database!"));
        }

        if (courseNameParam.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new PayloadErrorDTO("Course '" + courseName + "' already exists in database!"));
        }

        if (courseName != null && courseName.length() > 0) {
            courseEntity.get().setName(courseName);
        }

        if (teacherName != null && teacherName.length() > 0) {
            courseEntity.get().setTeacherName(teacherName);
        }

        if (classNumber != null && classNumber > 0) {
            courseEntity.get().setClassNumber(classNumber);
        }

        return ResponseEntity.status(HttpStatus.OK).body(toCourseDTO(courseEntity.get()));
    }

}