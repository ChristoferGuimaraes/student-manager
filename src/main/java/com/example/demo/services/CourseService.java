package com.example.demo.services;

import com.example.demo.dto.CourseDTO;
import com.example.demo.entities.CourseEntity;
import com.example.demo.repositories.CourseRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final ModelMapper modelMapper;

    public CourseService(CourseRepository courseRepository, ModelMapper modelMapper) {
        this.courseRepository = courseRepository;
        this.modelMapper = modelMapper;
    }

    private CourseDTO toCourseDTO(CourseEntity courseEntity) {
        return modelMapper.map(courseEntity, CourseDTO.class);
    }

    public ResponseEntity<Object> getAllCourses(PageRequest pageRequest) {
        Page<Object> page = courseRepository.findAll(pageRequest).map(this::toCourseDTO);

        return ResponseEntity.status(HttpStatus.OK).body(page);
    }
}
