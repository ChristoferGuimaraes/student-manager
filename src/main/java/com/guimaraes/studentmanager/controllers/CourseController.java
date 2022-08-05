package com.guimaraes.studentmanager.controllers;

import com.guimaraes.studentmanager.dto.CourseDTO;
import com.guimaraes.studentmanager.services.CourseService;
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

    @GetMapping("/course/{courseName}")
    public ResponseEntity<Object> getCourseByName(@PathVariable("courseName") String courseName) {
        return courseService.findCourseByName(courseName);
    }

    @PostMapping("/course")
    public ResponseEntity<Object> addNewCourse(@RequestBody CourseDTO courseDTO) {
        return courseService.addNewCourse(courseDTO);
    }

    @DeleteMapping("/course/{courseName}")
    public ResponseEntity<Object> deleteCourseByName(@PathVariable("courseName") String courseName) {
        return courseService.deleteCourseByName(courseName);
    }

    @PutMapping("/course/{courseName}")
    public ResponseEntity<Object> updateCourse(
            @PathVariable(name = "courseName", required = false) String name,
            @RequestParam(name ="name", required = false) String courseName,
            @RequestParam(name ="teacher_name", required = false) String teacherName,
            @RequestParam(name ="class_number", required = false) Integer classNumber
    ) {
        return courseService.updateCourse(name, courseName, teacherName, classNumber);
    }
}
