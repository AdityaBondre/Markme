package com.WhoKnows.Markme.repository;
import com.WhoKnows.Markme.model.Attendance;
import com.WhoKnows.Markme.model.AttendanceSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    List<Attendance> findBySessionId(Long sessionId);

    boolean existsBySessionAndStudentId(AttendanceSession session, Long id);

    boolean existsBySessionAndStudentEmail(AttendanceSession session, String studentEmail);

    boolean existsByStudentIdAndSession(Long studentId, AttendanceSession session);

    List<Attendance> findByStudentId(Long studentId);
    @Query("SELECT COUNT(a) FROM Attendance a WHERE a.studentId = :studentId AND a.session.sectionName = :sectionName AND a.session.startTime > :startTime")
    long countAttendedSessionsForStudentInSectionLast30Days(
            @Param("studentId") Long studentId,
            @Param("sectionName") String sectionName,
            @Param("startTime") LocalDateTime startTime
    );

}
