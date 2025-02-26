package com.WhoKnows.Markme.model;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
public class AttendanceSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String otpCode;
    private String professorName;
    private String subjectName;
    private String sectionName;
    private int studentLimit;
    private int studentsMarked = 0;
    private LocalDateTime startTime;
    private LocalDateTime expirationTime;
    private double latitude;   // Professor-defined latitude
    private double longitude;  // Professor-defined longitude
    private double radius;     // Allowed radius in meters

    @OneToMany(mappedBy = "session")
    private List<Attendance> attendances;

    // Getters and Setters
}