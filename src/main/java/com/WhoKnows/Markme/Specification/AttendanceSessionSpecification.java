package com.WhoKnows.Markme.Specification;

import com.WhoKnows.Markme.model.AttendanceSession;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class AttendanceSessionSpecification {

    public static Specification<AttendanceSession> filterSessions(
            String professorUsername, String sectionName, String subjectName, LocalDateTime startDate, LocalDateTime endDate) {

        return (root, query, criteriaBuilder) -> {
            Specification<AttendanceSession> spec = Specification.where(null);

            // Filter by professor username
            if (professorUsername != null && !professorUsername.isEmpty()) {
                spec = spec.and((root1, query1, cb) -> cb.equal(root1.get("professorName"), professorUsername));
            }

            if (sectionName != null && !sectionName.isEmpty()) {
                spec = spec.and((root1, query1, cb) -> cb.equal(root1.get("sectionName"), sectionName));
            }

            if (subjectName != null && !subjectName.isEmpty()) {
                spec = spec.and((root1, query1, cb) -> cb.equal(root1.get("subjectName"), subjectName));
            }

            if (startDate != null && endDate != null) {
                spec = spec.and((root1, query1, cb) ->
                        cb.between(root1.get("startTime"), startDate, endDate));
            }

            return spec.toPredicate(root, query, criteriaBuilder);
        };
    }

}
