package com.WhoKnows.Markme.model.DTOs;

import lombok.Data;

@Data
public class UserDto {
    private Long collegeId;
    private String username;
    private String password;
    private String email;
    private String role;
    private String sectionName; // This will be set only for students

    public static final String ROLE_STUDENT = "STUDENT";
    public static final String ROLE_PROFESSOR = "PROFESSOR";
}
