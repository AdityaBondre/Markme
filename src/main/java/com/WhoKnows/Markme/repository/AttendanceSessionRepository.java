package com.WhoKnows.Markme.repository;
import com.WhoKnows.Markme.model.AttendanceSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AttendanceSessionRepository extends JpaRepository<AttendanceSession, Long>, JpaSpecificationExecutor<AttendanceSession> {

    Optional<AttendanceSession> findByOtpCode(String otpCode);

    List<AttendanceSession> findByProfessorName(String professorUsername);

    List<AttendanceSession> findByOtpCodeAndSubjectNameAndSectionName(String otpCode, String subjectName, String sectionName);
    @Query("SELECT COUNT(s) FROM AttendanceSession s WHERE s.sectionName = :sectionName AND s.startTime > :startTime")
    long countTotalSessionsBySectionInLast30Days(@Param("sectionName") String sectionName, @Param("startTime") LocalDateTime startTime);
}
