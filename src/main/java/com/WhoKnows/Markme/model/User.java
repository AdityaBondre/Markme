package com.WhoKnows.Markme.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@MappedSuperclass
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long collegeId;
    @Column(unique = true)
    private String username;
    private String password;

    @Column(unique = true)
    private String email;
    private String role;

    public static final String ROLE_STUDENT = "STUDENT";
    public static final String ROLE_PROFESSOR = "PROFESSOR";
}
