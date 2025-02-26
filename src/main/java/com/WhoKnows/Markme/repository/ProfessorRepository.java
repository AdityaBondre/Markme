package com.WhoKnows.Markme.repository;

import com.WhoKnows.Markme.model.Professor;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface ProfessorRepository extends JpaRepository<Professor,Long> {
    Optional<Professor> findByUsername(String username);

    boolean existsByEmail(String email);
}
