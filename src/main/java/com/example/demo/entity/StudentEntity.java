package com.example.demo.entity;

import com.example.demo.dto.StudentDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;

import java.time.LocalDate;
import java.time.Period;

import static javax.persistence.GenerationType.SEQUENCE;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity(name = "Student")
@Table(
        name = "student",
        uniqueConstraints = {
                @UniqueConstraint(name = "student_email_unique", columnNames = "email")
        }
)
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
    @Column(name = "id", updatable = false)
    private Long id;


    @Column(
            name = "first_name",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String firstName;


    @Column(
            name = "last_name",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String lastName;


    @Column(
            name = "email",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String email;


    @Transient
    private Integer age;


    @Column(
            name = "birth_date",
            nullable = false,
            updatable = false
    )
    private LocalDate birthDate;


    @Column(name = "created_at",
            updatable = false
    )
    @CreationTimestamp
    private LocalDate createdAt;


    public StudentEntity(StudentDTO studentDTO) {
        id = studentDTO.getId();
        firstName = studentDTO.getFirstName();
        lastName = studentDTO.getLastName();
        age = studentDTO.getAge();
        email = studentDTO.getEmail();
        birthDate = studentDTO.getBirthDate();
        createdAt = studentDTO.getCreatedAt();
    }

}
