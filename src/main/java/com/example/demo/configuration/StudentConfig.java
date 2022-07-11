package com.example.demo.configuration;


import com.example.demo.entity.StudentEntity;
import com.example.demo.repository.StudentRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.util.List;

@Configuration
public class StudentConfig {

    @Bean
    CommandLineRunner commandLineRunner(StudentRepository repository) {
        return args -> {
            StudentEntity christofer = new StudentEntity(
                    "Christofer",
                    "Guimarães",
                    "christofer.guimaraes@projuris.com.br",
                    28,
                    LocalDate.of(1993, 11, 22));


            StudentEntity maria = new StudentEntity(
                    "Maria",
                    "Guimarães",
                    "maria_guimaraes@gmail.com.br",
                    38,
                    LocalDate.of(1983, 2, 14));


            repository.saveAll(
                    List.of(christofer, maria)
            );
        };



    }
}
