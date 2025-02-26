package com.WhoKnows.Markme.security;

import com.WhoKnows.Markme.model.Professor;
import com.WhoKnows.Markme.model.Student;
import com.WhoKnows.Markme.repository.ProfessorRepository;
import com.WhoKnows.Markme.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;


@Service
public class CustomUserDetailsService implements UserDetailsService {



    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ProfessorRepository professorRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Attempt to find the user in the Student repository
        Optional<Student> studentOpt = studentRepository.findByUsername(username);
        if (studentOpt.isPresent()) {
            Student student = studentOpt.get();
            Collection<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority("student")); // Add the appropriate role
            return new org.springframework.security.core.userdetails.User(student.getUsername(), student.getPassword(), authorities);
        }

        // Attempt to find the user in the Professor repository
        Optional<Professor> professorOpt = professorRepository.findByUsername(username);
        if (professorOpt.isPresent()) {
            Professor professor = professorOpt.get();
            Collection<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority("professor")); // Add the appropriate role
            return new org.springframework.security.core.userdetails.User(professor.getUsername(), professor.getPassword(), authorities);
        }

        // If no user is found in any repository, throw an exception
        throw new UsernameNotFoundException("User not found");
    }

}
