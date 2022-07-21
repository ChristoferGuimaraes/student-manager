package com.example.demo.dto;

import com.example.demo.entities.StudentEntity;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentDTO {

    private Long id;

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    private Integer age;

    private String email;

    @NotNull
    private LocalDate birthDate;

    private LocalDate createdAt;

    private List<CourseDTO> courses;


    public StudentDTO(Long id, String firstName, String lastName, Integer age, String email, LocalDate birthDate, LocalDate createdAt) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.email = email;
        this.birthDate = birthDate;
        this.createdAt = createdAt;
    }

    public StudentDTO(StudentEntity studentEntity) {
        id = studentEntity.getId();
        firstName = studentEntity.getFirstName();
        lastName = studentEntity.getLastName();
        age = studentEntity.getAge();
        email = studentEntity.getEmail();
        birthDate = studentEntity.getBirthDate();
        createdAt = studentEntity.getCreatedAt();
        courses = studentEntity.getCourses().stream().map(CourseDTO::new).collect(Collectors.toList());
    }

    public Integer getAge() {
        return Period.between(this.birthDate, LocalDate.now()).getYears();
    }

}
