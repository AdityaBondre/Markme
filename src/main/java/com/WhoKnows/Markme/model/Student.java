package com.WhoKnows.Markme.model;

import jakarta.persistence.Entity;
import lombok.Data;

@Data
@Entity
public class Student extends User{
    private String firstName;
    private String lastName;
    private String SectionName;
    private String phoneNo;
}
