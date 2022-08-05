package com.guimaraes.studentmanager.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CourseDTO {
    private String name;
    private String teacherName;
    private Integer classNumber;
    private LocalDate startDate;
}
