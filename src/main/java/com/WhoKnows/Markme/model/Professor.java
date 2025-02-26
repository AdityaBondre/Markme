package com.WhoKnows.Markme.model;

import jakarta.persistence.Entity;
import lombok.Data;

@Data
@Entity
public class Professor extends User{
    private String firstName;
    private String lastName;
    private String phoneNo;
}
