package com.example.demo.dto;

import com.example.demo.entities.CourseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseDTO {
    private String name;
    private String teacherName;
    private Integer classNumber;
    private LocalDate startDate;

    public CourseDTO(CourseEntity courseEntity) {
        name = courseEntity.getName();
        teacherName = courseEntity.getTeacherName();
        classNumber = courseEntity.getClassNumber();
        startDate = courseEntity.getStartDate();
    }

}
