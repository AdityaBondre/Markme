package com.WhoKnows.Markme.model.DTOs;

import lombok.Data;

@Data
public class NotificationRequestDTO {
    private String message;
    private String target; // 'ALL', 'SECTION', 'STUDENT'
    private String section; // Optional, used if target is 'SECTION'

}
