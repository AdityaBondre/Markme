package com.WhoKnows.Markme.model;

import jakarta.persistence.*;

import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String message;

    private Long professorId;

    private String professorUsername;

    private String target; // Can be 'ALL', 'SECTION', or 'STUDENT'

    private String section; // Null if not targeted at a section



    private LocalDateTime createdAt;

    public Notification() {
        this.createdAt = LocalDateTime.now();
    }

    public Notification(String message, Long professorId, String target, String section,String professorUsername) {
        this.professorUsername=professorUsername;
        this.message = message;
        this.professorId = professorId;
        this.target = target;
        this.section = section;

        this.createdAt = LocalDateTime.now();
    }
}
