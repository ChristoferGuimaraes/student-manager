package com.guimaraes.studentmanager.repositories;

import com.guimaraes.studentmanager.entities.CourseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<CourseEntity, Integer> {
    Optional<CourseEntity> findByNameIgnoreCase(String name);

}
