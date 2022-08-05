package com.guimaraes.studentmanager.entities;

import com.guimaraes.studentmanager.dto.StudentDTO;
import lombok.*;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static javax.persistence.GenerationType.SEQUENCE;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table
@Entity(name = "Student")
public class StudentEntity {

    @Id
    @SequenceGenerator(
            name = "student_sequence",
            sequenceName = "student_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = SEQUENCE,
            generator = "student_sequence"
    )
    @Column
    private Long id;

    @Column
    private String firstName;

    @Column
    private String lastName;

    @Column
    private String email;

    @Transient
    private Integer age;

    @Column
    private LocalDate birthDate;

    @Column
    private LocalDate createdAt;

    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(name = "student_course",
    joinColumns = @JoinColumn(name = "student_id"),
    inverseJoinColumns = @JoinColumn(name= "course_id"))
    private List<CourseEntity> courses;

    public StudentEntity(@NotNull StudentDTO studentDTO) {
        id = studentDTO.getId();
        firstName = studentDTO.getFirstName();
        lastName = studentDTO.getLastName();
        age = studentDTO.getAge();
        email = studentDTO.getEmail();
        birthDate = studentDTO.getBirthDate();
        createdAt = studentDTO.getCreatedAt();
        courses = studentDTO.getCourses().stream().map(CourseEntity::new).collect(Collectors.toList());
    }

    @PrePersist
    private void prePersist() {
        createdAt = LocalDate.now();
    }

}
