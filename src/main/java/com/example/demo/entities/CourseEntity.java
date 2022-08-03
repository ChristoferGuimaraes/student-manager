package com.example.demo.entities;

import com.example.demo.dto.CourseDTO;
import lombok.*;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table
@Entity(name = "Course")
public class CourseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer id;

    @Column
    private String name;

    @Column
    private String teacherName;

    @Column
    private Integer classNumber;

    @Column
    private LocalDate startDate;

    @ManyToMany(mappedBy = "courses")
    private Set<StudentEntity> studentEntityList;

    public CourseEntity(@NotNull CourseDTO courseDTO) {
        name = courseDTO.getName();
        teacherName = courseDTO.getTeacherName();
        classNumber = courseDTO.getClassNumber();
        startDate = courseDTO.getStartDate();
    }

}
