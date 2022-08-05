package com.guimaraes.studentmanager.dto;


public class PayloadErrorDTO {
    private final String message;

    public PayloadErrorDTO(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
