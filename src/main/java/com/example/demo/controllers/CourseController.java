package com.example.demo.controllers;

import com.example.demo.services.CourseService;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping("/courses")
    public ResponseEntity<Object> getAllCourses(
            @RequestParam(value="page", defaultValue = "0" )Integer page,
            @RequestParam(value="size", defaultValue = "10") Integer size){
        PageRequest pageRequest = PageRequest.of(page, size);
        return courseService.getAllCourses(pageRequest);
    }

    @PostMapping("/course")


}
