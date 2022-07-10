package com.example.demo.configuration;


import com.example.demo.entity.StudentEntity;
import com.example.demo.repository.StudentRepository;
import org.hibernate.Session;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class StudentConfig {

    StudentRepository studentRepository;

    @Bean
    CommandLineRunner commandLineRunner(StudentRepository repository) {
        return args -> {
            StudentEntity christofer = new StudentEntity(
                    "Christofer",
                    "Guimarães",
                    "christofer.guimaraes@projuris.com.br",
                    28);


            StudentEntity maria = new StudentEntity(
                    "Maria",
                    "Guimarães",
                    "mariaguimaraes@gmail.com.br",
                    38);


            repository.saveAll(
                    List.of(christofer, maria)
            );
        };



    }
}
