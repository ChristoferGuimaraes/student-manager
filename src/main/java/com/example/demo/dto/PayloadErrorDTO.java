package com.example.demo.dto;


public class PayloadErrorDTO {
    private final String message;

    public PayloadErrorDTO(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
