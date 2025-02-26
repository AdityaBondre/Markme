package com.WhoKnows.Markme.repository;

import com.WhoKnows.Markme.model.AttendanceMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface AttendanceMessageRepository extends JpaRepository<AttendanceMessage,Long> {

    List<AttendanceMessage> findAttendanceBySessionId(Long sessionId);
}
