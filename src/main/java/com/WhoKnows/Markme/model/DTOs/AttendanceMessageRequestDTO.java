package com.WhoKnows.Markme.model.DTOs;


import lombok.Data;

@Data
public class AttendanceMessageRequestDTO {
    private String messageContent;
    private String otpCode;
}
