package com.WhoKnows.Markme.model.DTOs;

import lombok.Data;

@Data
public class ErrorResponse {
    private String message;
    // Constructor that accepts a message string
    public ErrorResponse(String message) {
        this.message = message;
    }
}
