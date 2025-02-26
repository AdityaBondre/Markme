package com.WhoKnows.Markme.service;

import com.WhoKnows.Markme.Specification.AttendanceSessionSpecification;
import com.WhoKnows.Markme.model.*;
import com.WhoKnows.Markme.model.DTOs.AddStudentRequestDTO;
import com.WhoKnows.Markme.model.DTOs.AttendanceSessionRequestDTO;
import com.WhoKnows.Markme.model.DTOs.UpdateAttendanceSessionRequestDTO;
import com.WhoKnows.Markme.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class AttendanceSessionService {

    @Autowired
    private AttendanceSessionRepository attendanceSessionRepository;

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ProfessorRepository professorRepository;


    @Autowired
    private EmailService emailService;

    //Create Attendace Method
    public String createAttendanceSession(AttendanceSessionRequestDTO request, String professorUsername) {
        Optional<Professor> professor = professorRepository.findByUsername(professorUsername);
        if (professor.isEmpty() || !professor.get().getRole().equals("PROFESSOR")) {
            return "Invalid professor details";
        }

        // Generate OTP
        String otpCode = generateOtp();

        // Create an AttendanceSession
        AttendanceSession session = new AttendanceSession();
        session.setOtpCode(otpCode);
        session.setProfessorName(professor.get().getUsername());
        session.setSubjectName(request.getSubjectName());
        session.setSectionName(request.getSectionName());
        session.setStudentLimit(request.getStudentLimit());
        session.setStartTime(LocalDateTime.now());
        session.setExpirationTime(LocalDateTime.now().plusMinutes(request.getDuration()));

        // Save geofence details
        session.setLatitude(request.getLatitude());
        session.setLongitude(request.getLongitude());
        session.setRadius(request.getRadius());

        attendanceSessionRepository.save(session);

        return otpCode;
    }

    // Method to generate OTP (example)
    private String generateOtp() {
        return String.valueOf((int) (Math.random() * 1000000)); // 6-digit OTP
    }

    // Method to get all sessions created by the authenticated professor
    public List<AttendanceSession> getSessionsByProfessor(String professorUsername) {
        return attendanceSessionRepository.findByProfessorName(professorUsername);
    }

    // Method to get the list of students who marked attendance for a specific session
    public List<Attendance> getStudentsForSession(Long sessionId, String professorUsername) {
        // Ensure the session exists and belongs to the professor
        Optional<AttendanceSession> sessionOpt = attendanceSessionRepository.findById(sessionId);
        if (sessionOpt.isEmpty()) {
            throw new RuntimeException("Session not found.");
        }

        AttendanceSession session = sessionOpt.get();
        if (!session.getProfessorName().equals(professorUsername)) {
            throw new RuntimeException("You are not authorized to view this session.");
        }

        // Return the list of students who marked attendance
        return attendanceRepository.findBySessionId(sessionId);
    }

    //Mark Students Attendance Manually By Professor Method
    public String addStudentToSession(AddStudentRequestDTO request, String professorUsername) {
        // Fetch the session based on OTP
        Optional<AttendanceSession> sessionOpt = attendanceSessionRepository.findByOtpCode(request.getOtpCode());
        if (sessionOpt.isEmpty()) {
            return "Invalid OTP";
        }

        AttendanceSession session = sessionOpt.get();

        // Verify that the session belongs to the professor
        if (!session.getProfessorName().equals(professorUsername)) {
            return "You are not authorized to modify this session";
        }

        // Check if the student limit is reached
        if (session.getStudentsMarked() >= session.getStudentLimit()) {
            return "Student limit reached";
        }

        // Fetch the student details
        Optional<Student> studentOpt = studentRepository.findByUsername(request.getStudentUsername());
        if (studentOpt.isEmpty() || !studentOpt.get().getRole().equals("STUDENT")) {
            return "Invalid student details";
        }

        Student student = studentOpt.get();

        // Check if the student has already been marked
        boolean alreadyMarked = attendanceRepository.existsBySessionAndStudentEmail(session, student.getEmail());
        if (alreadyMarked) {
            return "Student has already been marked";
        }

        // Mark attendance
        Attendance attendance = new Attendance();
        attendance.setStudentId(Math.toIntExact(student.getId()));
        attendance.setStudentName(student.getUsername());
        attendance.setStudentEmail(student.getEmail());
        attendance.setSession(session);
        attendance.setTimestamp(LocalDateTime.now());

        // Save attendance record
        attendanceRepository.save(attendance);

        // Update the attendance session
        session.setStudentsMarked(session.getStudentsMarked() + 1);
        attendanceSessionRepository.save(session);

        // Send email to the student
        emailService.emailForSuccessfullyMarkedAttendance(student.getEmail(), session.getSubjectName(), session.getSectionName());

        return "Student added successfully to the session";
    }


    //Update Session Method
    public String updateSession(UpdateAttendanceSessionRequestDTO request, String professorUsername) {
        Optional<AttendanceSession> sessionOptional = attendanceSessionRepository.findByOtpCode(request.getOtpCode());

        if (sessionOptional.isEmpty()) {
            return "Session not found!";
        }

        AttendanceSession session = sessionOptional.get();

        // Check if the session belongs to the authenticated professor
        if (!session.getProfessorName().equals(professorUsername)) {
            return "You are not authorized to update this session!";
        }

        // Update session details
        session.setSubjectName(request.getSubjectName());
        session.setSectionName(request.getSectionName());
        session.setStudentLimit(request.getStudentLimit());

        // Save the updated session
        attendanceSessionRepository.save(session);

        return "Attendance session updated successfully!";
    }


    //Delete Session Method
    public String deleteSession(String otpCode, String professorUsername) {
        Optional<AttendanceSession> sessionOptional = attendanceSessionRepository.findByOtpCode(otpCode);

        if (sessionOptional.isEmpty()) {
            return "Session not found!";
        }

        AttendanceSession session = sessionOptional.get();

        // Check if the session belongs to the authenticated professor
        if (!session.getProfessorName().equals(professorUsername)) {
            return "You are not authorized to delete this session!";
        }

        // Delete all associated attendance records
        attendanceRepository.deleteAll(session.getAttendances());

        // Delete the session
        attendanceSessionRepository.delete(session);

        return "Attendance session deleted successfully!";
    }




    public List<AttendanceSession> filterAttendanceSessions(String professorUsername, String sectionName, String subjectName, LocalDateTime startDate, LocalDateTime endDate) {
        Specification<AttendanceSession> spec = AttendanceSessionSpecification.filterSessions(professorUsername, sectionName, subjectName, startDate, endDate);
        List<AttendanceSession> sessions = attendanceSessionRepository.findAll(spec);

        // Reverse the order of the list
        Collections.reverse(sessions);

        return sessions;
    }

    // Fetch total sessions for the section in the last 30 days
    public long getTotalSessionsForSectionInLast30Days(String sectionName) {
        LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);
        return attendanceSessionRepository.countTotalSessionsBySectionInLast30Days(sectionName, thirtyDaysAgo);
    }

    // Fetch attended sessions for the student in the specific section in the last 30 days
    public long getAttendedSessionsForStudentInSectionLast30Days(Long studentId, String sectionName) {
        LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);
        return attendanceRepository.countAttendedSessionsForStudentInSectionLast30Days(studentId, sectionName, thirtyDaysAgo);
    }

    // Fetch missed sessions (total - attended) ensuring non-negative values
    public long getMissedSessionsForStudentInLast30Days(Long studentId, String sectionName) {
        long totalSessions = getTotalSessionsForSectionInLast30Days(sectionName);
        long attendedSessions = getAttendedSessionsForStudentInSectionLast30Days(studentId, sectionName);
        return Math.max(0, totalSessions - attendedSessions);
    }

}
