package com.example.demo.dto;

import com.example.demo.entity.StudentEntity;

import java.time.LocalDate;


public class StudentDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private Integer age;
    private String email;
    private LocalDate birthDate;
    private LocalDate createdAt;

    public StudentDTO() {

    }

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
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }
}
