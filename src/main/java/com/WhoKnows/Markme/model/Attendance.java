package com.WhoKnows.Markme.model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Attendance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "session_id", nullable = false)
    @JsonIgnore
    private AttendanceSession session;

    private int studentId;  // Reference to a student entity in a real-world scenario
    private String studentName;
    private String studentEmail;
    private LocalDateTime timestamp;
    private String professorName;
    private String subjectName;
    private String sectionName;

    // Getters and Setters
}