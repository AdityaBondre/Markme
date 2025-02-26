package com.WhoKnows.Markme.service;

import com.WhoKnows.Markme.model.DTOs.UserDto;
import com.WhoKnows.Markme.model.DTOs.UserProfileUpdateDTO;
import com.WhoKnows.Markme.model.Professor;
import com.WhoKnows.Markme.model.Student;
import com.WhoKnows.Markme.model.User;
import com.WhoKnows.Markme.repository.ProfessorRepository;
import com.WhoKnows.Markme.repository.StudentRepository;
import com.WhoKnows.Markme.security.CustomUserDetailsService;
import com.WhoKnows.Markme.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ProfessorRepository professorRepository;


    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private  EmailService emailService;


    // Service method to handle login
    public String login(User user) {
        try {
            // Load user details using the custom user details service
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(user.getUsername());

            // Verify the password
            if (passwordEncoder.matches(user.getPassword(), userDetails.getPassword())) {
                // Retrieve the role directly from UserDetails
                String role = userDetails.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .findFirst()
                        .orElse(""); // Default to an empty string if no role is found

                // Generate a token including the username and role
                return jwtUtil.generateToken(userDetails.getUsername(), role);  // Return the token directly
            } else {
                // If credentials are incorrect, return an error message
                throw new IllegalArgumentException("Invalid login credentials");
            }
        } catch (UsernameNotFoundException | IllegalArgumentException e) {
            // Handle the case where the user is not found or password mismatch
            throw new IllegalArgumentException("Invalid login credentials");
        }
    }



    //register method
    public ResponseEntity<String> register(UserDto user) {
        try {
            // Check if the role is valid and create the appropriate user instance
            if (user.getRole().equals(User.ROLE_STUDENT)) {
                Student student = new Student();
                student.setCollegeId(user.getCollegeId());
                student.setUsername(user.getUsername());
                student.setSectionName(user.getSectionName());
                student.setEmail(user.getEmail());
                student.setPassword(user.getPassword()); // we'll encrypt it later
                student.setRole(User.ROLE_STUDENT);

                // Encrypt the password before saving
                String encryptedPassword = passwordEncoder.encode(student.getPassword());
                student.setPassword(encryptedPassword);

                // Save the student
                studentRepository.save(student);

                // Send registration confirmation email for student
                emailService.sendRegistrationEmail(student.getEmail(), student.getUsername());

            } else if (user.getRole().equals(User.ROLE_PROFESSOR)) {
                Professor professor = new Professor();
                professor.setUsername(user.getUsername());
                professor.setCollegeId(user.getCollegeId());
                professor.setEmail(user.getEmail());
                professor.setPassword(user.getPassword()); // we'll encrypt it later
                professor.setRole(User.ROLE_PROFESSOR);

                // Encrypt the password before saving
                String encryptedPassword = passwordEncoder.encode(professor.getPassword());
                professor.setPassword(encryptedPassword);

                // Save the professor
                professorRepository.save(professor);

                // Send registration confirmation email for professor
                emailService.sendRegistrationEmail(professor.getEmail(), professor.getUsername());

            } else {
                return ResponseEntity.badRequest().body("Invalid role specified.");
            }

            return ResponseEntity.ok("User registered successfully.");

        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.badRequest().body("Username or Email already exists.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while processing the registration.");
        }
    }

    //update student method
    public ResponseEntity<Object> updateStudentProfile(Long studentId, UserProfileUpdateDTO dto) {
        Student student = studentRepository.findById(studentId).orElseThrow(() -> new RuntimeException("Student not found"));
        student.setFirstName(dto.getFirstName());
        student.setLastName(dto.getLastName());
        student.setPhoneNo(dto.getPhoneNo());
        student.setSectionName(dto.getSectionName());
        student.setEmail(dto.getEmail());
         studentRepository.save(student);
        return ResponseEntity.ok(student);
    }

    //update professor method
    public ResponseEntity<Object> updateProfessorProfile(Long professorId, UserProfileUpdateDTO dto) {
        Professor professor = professorRepository.findById(professorId).orElseThrow(() -> new RuntimeException("Professor not found"));
        professor.setFirstName(dto.getFirstName());
        professor.setLastName(dto.getLastName());
        professor.setPhoneNo(dto.getPhoneNo());
        professor.setEmail(dto.getEmail());
        professorRepository.save(professor);
        return ResponseEntity.ok(professor);
    }

}
