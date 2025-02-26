package com.WhoKnows.Markme.model.DTOs;

import lombok.Data;

@Data
public class AddStudentRequestDTO {
    private String otpCode;
    private String studentUsername;

    // Getters and Setters


    public String getOtpCode() {
        return otpCode;
    }

    public void setOtpCode(String otpCode) {
        this.otpCode = otpCode;
    }

    public String getStudentUsername() {
        return studentUsername;
    }

    public void setStudentUsername(String studentUsername) {
        this.studentUsername = studentUsername;
    }
}
