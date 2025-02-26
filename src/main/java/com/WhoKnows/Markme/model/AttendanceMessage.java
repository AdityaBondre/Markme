package com.WhoKnows.Markme.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class AttendanceMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long messageId;
    private Long UserId;
    private Long collegeId;
    private String username;
    private Long sessionId;
    private String subjectName;
    private String sectionName;
    private String messageContent;
    private LocalDateTime timestamp;

    // other fields...
}
