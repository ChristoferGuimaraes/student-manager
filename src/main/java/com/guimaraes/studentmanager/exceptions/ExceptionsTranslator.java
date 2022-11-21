package com.guimaraes.studentmanager.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.stream.Collectors;

@ControllerAdvice
public class ExceptionsTranslator {
    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<StudentProblem> validationException(ConstraintViolationException exception) {
        String message = exception.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList()).toString();


        StudentProblem problem = StudentProblem.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message(message.replaceAll("", ""))
                .title("ConstraintViolationException")
                .build();
        return new ResponseEntity<>(problem, HttpStatus.BAD_REQUEST);
    }
}
