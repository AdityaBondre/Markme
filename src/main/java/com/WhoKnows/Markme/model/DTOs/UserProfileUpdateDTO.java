package com.WhoKnows.Markme.model.DTOs;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class UserProfileUpdateDTO {

    private Long collegeId;
    private String username;
    private String firstName;
    private String lastName;
    private String phoneNo;
    private String sectionName; // only for Student
    private String email;
}
