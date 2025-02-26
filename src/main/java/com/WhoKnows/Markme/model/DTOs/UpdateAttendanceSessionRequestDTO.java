package com.WhoKnows.Markme.model.DTOs;

import lombok.Data;

@Data
public class UpdateAttendanceSessionRequestDTO {
    private String otpCode;
    private String subjectName;
    private String sectionName;
    private int studentLimit;

    // Getters and Setters

    public String getOtpCode() {
        return otpCode;
    }

    public void setOtpCode(String otpCode) {
        this.otpCode = otpCode;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public int getStudentLimit() {
        return studentLimit;
    }

    public void setStudentLimit(int studentLimit) {
        this.studentLimit = studentLimit;
    }
}
