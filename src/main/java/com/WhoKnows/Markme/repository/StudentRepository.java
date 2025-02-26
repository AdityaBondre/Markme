package com.WhoKnows.Markme.repository;
import com.WhoKnows.Markme.model.Student;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student,Long> {

    Optional<Student> findByUsername(String username);



    boolean existsByEmail(String email);
}
