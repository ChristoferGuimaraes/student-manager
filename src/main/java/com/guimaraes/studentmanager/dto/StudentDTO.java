package com.guimaraes.studentmanager.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentDTO {

    private Long id;

    private String firstName;

    private String lastName;

    private Integer age;

    private String email;

    private LocalDate birthDate;

    private LocalDate createdAt;

    private List<CourseDTO> courses;

    public Integer getAge() {
        return Period.between(this.birthDate, LocalDate.now()).getYears();
    }

}
