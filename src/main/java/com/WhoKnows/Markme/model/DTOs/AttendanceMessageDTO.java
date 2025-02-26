package com.WhoKnows.Markme.model.DTOs;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AttendanceMessageDTO {

    private String username;
    private String collageId;
    private Long sessionId;
    private String section;
    private String subject;
    private String messageContent;
    private LocalDateTime timestamp;
}
