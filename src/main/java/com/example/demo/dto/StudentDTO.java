package com.example.demo.dto;

import com.sun.istack.NotNull;
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

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    private Integer age;

    @NotNull
    private String email;

    @NotNull
    private LocalDate birthDate;

    private LocalDate createdAt;

    private List<CourseDTO> courses;

    public Integer getAge() {
        return Period.between(this.birthDate, LocalDate.now()).getYears();
    }

}
