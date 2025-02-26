package com.WhoKnows.Markme.controller;

import com.WhoKnows.Markme.model.Attendance;
import com.WhoKnows.Markme.model.DTOs.AttendanceMessageRequestDTO;
import com.WhoKnows.Markme.model.DTOs.CheckAttendanceRequestDTO;
import com.WhoKnows.Markme.model.DTOs.MarkAttendanceRequestDTO;
import com.WhoKnows.Markme.model.Notification;
import com.WhoKnows.Markme.model.Student;
import com.WhoKnows.Markme.repository.StudentRepository;
import com.WhoKnows.Markme.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import org.springframework.web.bind.annotation.*;

import java.util.*;


@RestController
@RequestMapping("/api/student")
public class StudentController {

    @Autowired
    private UserService userService;

    @Autowired
    private AttendanceService attendanceService;

    @Autowired
    private AttendanceMessageService attendanceMessageService;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private AttendanceSessionService attendanceSessionService;

    @Autowired
    private NotificationService notificationService;



    // Endpoint to mark attendance (for student)
    @PostMapping("/mark")
    public ResponseEntity<String> markAttendance(@RequestBody MarkAttendanceRequestDTO markAttendanceRequestDTO,
                                                 Authentication authentication) {



        // Get the student details from the Authentication object
        String studentUsername = authentication.getName(); // Automatically populated by Spring Security

        // Call the service to mark attendance
        String response = attendanceService.markAttendance(markAttendanceRequestDTO, studentUsername);

        return ResponseEntity.ok(response);
    }

    // Check own attendance is marked or not
    @GetMapping("/check")
    public ResponseEntity<String> checkAttendance(@RequestBody CheckAttendanceRequestDTO request,
                                                  Authentication authentication) {
        // Get the student username from the authentication object
        String studentUsername = authentication.getName();

        // Call the service to check attendance
        boolean isMarked = attendanceService.isAttendanceMarked(request.getOtpCode(),request.getSubjectName(), studentUsername);

        if (isMarked) {
            return ResponseEntity.ok("Your attendance has been marked for " + request.getSubjectName() + " in section " + request.getOtpCode());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Your attendance has not been marked.");
        }
    }

    // Endpoint For Add message On session
    @PostMapping("/message")
    public ResponseEntity<String> sendMessage(
            @RequestBody AttendanceMessageRequestDTO attendanceMessageRequestDTO,
            Authentication authentication) {

        // Get the student username from the authentication object
        String username = authentication.getName();

        String response = attendanceMessageService.sendMessage(username, attendanceMessageRequestDTO);

        if (response.equals("Message sent successfully.")) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.badRequest().body(response);
    }


    //Endpoint for History of student attendance
    @GetMapping("/history")
    public ResponseEntity<List<Attendance>> getAttendanceHistory(Authentication authentication) {
        String username = authentication.getName();
        List<Attendance> attendanceList = attendanceService.getUserAttendanceHistory(username);
        // Reverse the order of the list
        Collections.reverse(attendanceList);
        return ResponseEntity.ok(attendanceList);
    }

    //Endpoint for Dashboard Analysis
    @GetMapping("/summary")
    public ResponseEntity<Map<String, Object>> getSessionSummary(Authentication authentication) {
        String username = authentication.getName();

        // Fetch student entity by username
        Optional<Student> student = studentRepository.findByUsername(username);
        if (student.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of());
        }

        // Extract sectionName and studentId from the student entity
        String sectionName = student.get().getSectionName();
        Long studentId = student.get().getId();

        // Fetch total, attended, and missed sessions using the sectionName and studentId
        long totalSessions = attendanceSessionService.getTotalSessionsForSectionInLast30Days(sectionName);
        long attendedSessions = attendanceSessionService.getAttendedSessionsForStudentInSectionLast30Days(studentId, sectionName);
        long missedSessions = Math.max(0, totalSessions - attendedSessions);
        // Calculate attendance percentage
        double attendancePercentage = (totalSessions > 0) ? ((double) attendedSessions / totalSessions) * 100 : 0.0;


        // Create response map
        Map<String, Object> response = new HashMap<>();
        response.put("totalSessions", totalSessions);
        response.put("attendedSessions", attendedSessions);
        response.put("missedSessions", missedSessions);
        response.put("attendancePercentage", attendancePercentage);

        return ResponseEntity.ok(response);
    }

    //Endpoint for view Notifications
    @GetMapping("/notifications")
    public ResponseEntity<List<Notification>> getNotifications(Authentication authentication) {
        String username = authentication.getName();
        Optional<Student> student = studentRepository.findByUsername(username);
        String sectionName = student.get().getSectionName();

        List<Notification> notifications = notificationService.getNotificationsForAllAndUserSection(sectionName);
        return ResponseEntity.ok(notifications);
    }




}

