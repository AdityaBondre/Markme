package com.WhoKnows.Markme.model.DTOs;

import lombok.Data;

@Data
public class CheckAttendanceRequestDTO {
    private String subjectName;
    private String otpCode;


}
