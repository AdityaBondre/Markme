package com.WhoKnows.Markme.controller;

import com.WhoKnows.Markme.model.*;
import com.WhoKnows.Markme.model.DTOs.*;
import com.WhoKnows.Markme.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/professor")
public class ProfessorController {

    @Autowired
    private UserService userService;

    @Autowired
    private AttendanceService attendanceService;

    @Autowired
    private AttendanceSessionService attendanceSessionService;

    @Autowired
    private AttendanceMessageService attendanceMessageService;

    @Autowired
    private NotificationService notificationService;



    // Endpoint to create an attendance session (for professor)
    @PostMapping("/session")
    public ResponseEntity<String> createAttendanceSession(@RequestBody AttendanceSessionRequestDTO attendanceSessionRequestDTO,
                                                          Authentication authentication) {


        // Get the professor's username from the Authentication object
        String professorUsername = authentication.getName();  // Automatically populated by Spring Security

        // Call the service to create an attendance session
        String otp = attendanceSessionService.createAttendanceSession(attendanceSessionRequestDTO, professorUsername);

        return ResponseEntity.ok("Session created successfully. OTP: " + otp);
    }

    // Endpoint to get all sessions created by the authenticated professor
    @GetMapping
    public ResponseEntity<List<AttendanceSession>> getSessions(Authentication authentication) {
        String professorUsername = authentication.getName(); // Get the professor's username from the authentication token
        List<AttendanceSession> sessions = attendanceSessionService.getSessionsByProfessor(professorUsername);
        // Reverse the order of the list
        Collections.reverse(sessions);
        return ResponseEntity.ok(sessions);
    }

    // Endpoint to get the list of students for a specific session
    @GetMapping("/{sessionId}/students")
    public ResponseEntity<List<Attendance>> getStudentsForSession(@PathVariable Long sessionId, Authentication authentication) {
        String professorUsername = authentication.getName(); // Get the professor's username from the authentication token
        List<Attendance> students = attendanceSessionService.getStudentsForSession(sessionId, professorUsername);
        // Reverse the order of the list
        Collections.reverse(students);
        return ResponseEntity.ok(students);
    }

    // Add Student Manually
    @PostMapping("/add-student")
    public ResponseEntity<String> addStudentToSession(@RequestBody AddStudentRequestDTO request, Authentication authentication) {
        String professorUsername = authentication.getName();  // Get the professor's username

        // Call the service to add the student
        String response = attendanceSessionService.addStudentToSession(request, professorUsername);

        return ResponseEntity.ok(response);
    }
    // Update Session
    @PutMapping("/update/{otpCode}")
    public ResponseEntity<String> updateSession(@PathVariable String otpCode,
                                                @RequestBody UpdateAttendanceSessionRequestDTO request,
                                                Authentication authentication) {
        // Get professor's username from JWT
        String professorUsername = authentication.getName();

        // Set the OTP code in the request DTO
        request.setOtpCode(otpCode);

        // Call the service method to update the session
        String response = attendanceSessionService.updateSession(request, professorUsername);

        return ResponseEntity.ok(response);
    }

    //Delete Session
    @DeleteMapping("/delete/{otpCode}")
    public ResponseEntity<String> deleteSession(@PathVariable String otpCode, Authentication authentication) {
        // Get professor's username from JWT
        String professorUsername = authentication.getName();

        // Call the service method to delete the session
        String response = attendanceSessionService.deleteSession(otpCode, professorUsername);

        return ResponseEntity.ok(response);
    }


    // Endpoint to Get All Message For Specific Session
    @GetMapping("/{sessionId}/messages")
    public ResponseEntity<List<AttendanceMessage>> getMessagesForSession(
            @PathVariable Long sessionId) {

        List<AttendanceMessage> messages = attendanceMessageService.getMessagesForSession(sessionId);

        if (messages.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        // Reverse the order of the list
        Collections.reverse(messages);

        return ResponseEntity.ok(messages);
    }


    // Endpoint For Filter Session
    @GetMapping("/filter")
    public List<AttendanceSession> filterSessions(
            @RequestParam(required = false) String sectionName,
            @RequestParam(required = false) String subjectName,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,Authentication authentication) {

        String professorUsername = authentication.getName();
        return attendanceSessionService.filterAttendanceSessions(professorUsername,sectionName, subjectName, startDate, endDate);
    }



    //Endpoint for Add Notification
    @PostMapping("/add/notification")
    public ResponseEntity<Notification> createNotification(@RequestBody NotificationRequestDTO request,Authentication authentication) {
        String username = authentication.getName();

        Notification notification = notificationService.createNotification(
                request.getMessage(),username, request.getTarget(), request.getSection());
        return ResponseEntity.ok(notification);
    }

    //Endpoint for View all notification
    @GetMapping("/all")
    public ResponseEntity<List<Notification>> getNotificationsForAll() {
        return ResponseEntity.ok(notificationService.getNotificationsForAll());
    }

    //Endpoint for view specific sections notifications
    @GetMapping("/section/{section}")
    public ResponseEntity<List<Notification>> getNotificationsForSection(@PathVariable String section) {
        return ResponseEntity.ok(notificationService.getNotificationsForSection(section));
    }

}
