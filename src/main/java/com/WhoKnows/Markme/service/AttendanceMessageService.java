package com.WhoKnows.Markme.service;

import com.WhoKnows.Markme.model.AttendanceMessage;
import com.WhoKnows.Markme.model.AttendanceSession;
import com.WhoKnows.Markme.model.DTOs.AttendanceMessageRequestDTO;
import com.WhoKnows.Markme.model.Student;
import com.WhoKnows.Markme.repository.AttendanceMessageRepository;
import com.WhoKnows.Markme.repository.AttendanceSessionRepository;
import com.WhoKnows.Markme.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class AttendanceMessageService {

    @Autowired
    private AttendanceMessageRepository attendanceMessageRepository;

    @Autowired
    private AttendanceSessionRepository attendanceSessionRepository;

    @Autowired
    private StudentRepository studentRepository;



    // Method to send a message
    public String sendMessage(String username, AttendanceMessageRequestDTO attendanceMessageRequestDTO) {

        String otpCode = attendanceMessageRequestDTO.getOtpCode();
        String messageContent = attendanceMessageRequestDTO.getMessageContent();
        Student student = studentRepository.findByUsername(username).orElse(null);


        AttendanceSession session = attendanceSessionRepository.findByOtpCode(otpCode).orElse(null);
        if (session == null) {
            return "Session not found.";
        }

        // Check if the message is being sent within 1 hour of session creation
        LocalDateTime sessionCreateTime = session.getStartTime();
        LocalDateTime currentTime = LocalDateTime.now();

        if (ChronoUnit.HOURS.between(sessionCreateTime, currentTime) > 1) {
            return "You can only send messages within 1 hour of session creation.";
        }

        // Create and save the message
        AttendanceMessage message = new AttendanceMessage();
        message.setUsername(username);
        message.setCollegeId(student.getCollegeId());
        message.setUserId(student.getId());
        message.setSessionId(session.getId());
        message.setSectionName(session.getSectionName());
        message.setSubjectName(session.getSubjectName());
        message.setMessageContent(messageContent);
        message.setTimestamp(LocalDateTime.now());

        attendanceMessageRepository.save(message);

        return "Message sent successfully.";
    }


    // Method for Get Message Of session
    public List<AttendanceMessage> getMessagesForSession(Long sessionId) {
        return attendanceMessageRepository.findAttendanceBySessionId(sessionId);
    }
}
