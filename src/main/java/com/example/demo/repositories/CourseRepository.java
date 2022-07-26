package com.example.demo.repositories;

import com.example.demo.entities.CourseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CourseRepository extends JpaRepository<CourseEntity, Integer> {
    Optional<CourseEntity> findByName(String name);

}
