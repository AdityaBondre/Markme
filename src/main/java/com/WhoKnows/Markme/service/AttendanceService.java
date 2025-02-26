package com.WhoKnows.Markme.service;

import com.WhoKnows.Markme.model.Attendance;

import com.WhoKnows.Markme.model.AttendanceSession;
import com.WhoKnows.Markme.model.DTOs.*;
import com.WhoKnows.Markme.model.Student;
import com.WhoKnows.Markme.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Service
public class AttendanceService {

    // Automatically inject the repositories
    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private AttendanceSessionRepository attendanceSessionRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private AttendanceMessageRepository attendanceMessageRepository;

    @Autowired
    private EmailService emailService;

    // Method For Student To Mark Attendance
    public String markAttendance(MarkAttendanceRequestDTO request, String studentUsername) {
        Optional<Student> student = studentRepository.findByUsername(studentUsername);
        if (student.isEmpty() || !student.get().getRole().equals("STUDENT")) {
            return "Invalid student details";
        }

        // Fetch the session based on OTP
        Optional<AttendanceSession> session = attendanceSessionRepository.findByOtpCode(request.getOtpCode());
        if (session.isEmpty()) {
            return "Invalid OTP";
        }

        // Validate session details (subject and section)
        if (!session.get().getSubjectName().equals(request.getSubjectName()) ||
                !session.get().getSectionName().equals(student.get().getSectionName())) {
            return "Invalid session details";
        }

        // Check if the OTP has expired
        if (session.get().getExpirationTime().isBefore(LocalDateTime.now())) {
            return "OTP expired";
        }

        // Check if the student limit is reached
        if (session.get().getStudentsMarked() >= session.get().getStudentLimit()) {
            return "Student limit reached";
        }

        boolean alreadyMarked = attendanceRepository.existsByStudentIdAndSession(
                student.get().getId(), session.get());
        if (alreadyMarked) {
            return "You have already marked attendance for this session.";
        }

        // Geofencing Check: Verify if the student is inside the allowed area
        boolean isInsideGeofence = isWithinGeofence(
                session.get().getLatitude(),
                session.get().getLongitude(),
                session.get().getRadius(),
                request.getLatitude(),
                request.getLongitude()
        );

        if (!isInsideGeofence) {
            return "You are outside the allowed area!";
        }

        // Mark attendance
        Attendance attendance = new Attendance();
        attendance.setStudentId(Math.toIntExact(student.get().getId()));
        attendance.setStudentName(student.get().getUsername());
        attendance.setStudentEmail(student.get().getEmail());
        attendance.setProfessorName(session.get().getProfessorName());
        attendance.setSectionName(student.get().getSectionName());
        attendance.setSubjectName(session.get().getSubjectName());
        attendance.setSession(session.get());
        attendance.setTimestamp(LocalDateTime.now());

        // Save attendance record
        attendanceRepository.save(attendance);

        // Update the attendance session
        session.get().setStudentsMarked(session.get().getStudentsMarked() + 1);
        attendanceSessionRepository.save(session.get());

        // Send email to the student
        emailService.emailForSuccessfullyMarkedAttendance(student.get().getEmail(), session.get().getSubjectName(), session.get().getSectionName());

        return "Attendance marked successfully";
    }

    // Sub Method Of Marking Attendance for Location Check
    private boolean isWithinGeofence(double officeLat, double officeLon, double radius, double userLat, double userLon) {
        final int EARTH_RADIUS = 6371000; // Radius of Earth in meters
        double dLat = Math.toRadians(userLat - officeLat);
        double dLon = Math.toRadians(userLon - officeLon);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(officeLat)) * Math.cos(Math.toRadians(userLat)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = EARTH_RADIUS * c;

        return distance <= radius;
    }

    //  Method Of Marking Attendance for Check Is Attendance Marked
    public boolean isAttendanceMarked(String otpCode,String subjectName, String studentUsername) {
        // Find the student's record
        Optional<Student> student = studentRepository.findByUsername(studentUsername);
        if (student.isEmpty() || !student.get().getRole().equals("STUDENT")) {
            return false;
        }
         String sectionName = student.get().getSectionName();
        // Fetch the attendance session based on subject and section
        List<AttendanceSession> sessions = attendanceSessionRepository.findByOtpCodeAndSubjectNameAndSectionName(otpCode,subjectName, sectionName);
        if (sessions.isEmpty()) {
            return false;
        }

        // Check if the student has marked attendance for any session
        for (AttendanceSession session : sessions) {
            boolean isMarked = attendanceRepository.existsBySessionAndStudentId(session, student.get().getId());
            if (isMarked) {
                return true;
            }
        }

        return false;
    }

    //Method for history of student mark attendance
    public List<Attendance> getUserAttendanceHistory(String username) {
        Optional<Student> student = studentRepository.findByUsername(username);
        Long userId = student.get().getId();
        return attendanceRepository.findByStudentId(userId);
    }

}
