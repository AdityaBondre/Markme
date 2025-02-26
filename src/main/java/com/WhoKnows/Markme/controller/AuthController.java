package com.WhoKnows.Markme.controller;

import com.WhoKnows.Markme.model.DTOs.ErrorResponse;
import com.WhoKnows.Markme.model.DTOs.UserDto;
import com.WhoKnows.Markme.model.DTOs.UserProfileUpdateDTO;
import com.WhoKnows.Markme.model.Professor;
import com.WhoKnows.Markme.model.Student;
import com.WhoKnows.Markme.model.User;
import com.WhoKnows.Markme.repository.ProfessorRepository;
import com.WhoKnows.Markme.repository.StudentRepository;
import com.WhoKnows.Markme.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")

public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ProfessorRepository professorRepository;


    //Endpoint for User Login
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody User user) {
        try {
            // Call the login service method
            String token = userService.login(user);  // This will return the token (String)
            Map<String, String> response = new HashMap<>();
            response.put("token", token);  // Wrap the token in a map or JSON structure
            return ResponseEntity.ok(response);  // Return the response with the token
        } catch (Exception e) {
            return ResponseEntity.status(401).body(Collections.singletonMap("message", "Invalid login credentials"));
        }
    }



    // Register endpoint
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserDto user) {
        return userService.register(user);
    }

    // Endpoint for updating student profile
    @PutMapping("/profile/{id}")
    public ResponseEntity<Object> updateProfile(@PathVariable Long id, @RequestBody UserProfileUpdateDTO dto, Authentication authentication) {
        String username = authentication.getName();
        String role = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst() // Assuming one role, adjust if there are multiple roles
                .orElseThrow(() -> new IllegalArgumentException("Role not found"));

        if ("student".equalsIgnoreCase(role)) { // Use .equalsIgnoreCase() for case-insensitive comparison
            return userService.updateStudentProfile(id, dto);
        } else if ("professor".equalsIgnoreCase(role)) {
            return userService.updateProfessorProfile(id, dto);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ErrorResponse("Access denied for this role"));
        }
    }

    //Endpoint for Update profile
    @GetMapping("/profile/details")
    public ResponseEntity<Object> getUserDetails(Authentication authentication) {
        String username = authentication.getName();
        String role = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst() // Assuming one role, adjust if there are multiple roles
                .orElseThrow(() -> new IllegalArgumentException("Role not found"));

        switch (role) {
            case "student":
                // Fetch student details directly using student repository
                Optional<Student> student = studentRepository.findByUsername(username);
                return student.map(s -> ResponseEntity.ok((Object) s))  // Cast to Object to match ResponseEntity<Object>
                        .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("Student not found")));

            case "professor":
                // Fetch professor details directly using professor repository
                Optional<Professor> professor = professorRepository.findByUsername(username);
                return professor.map(p -> ResponseEntity.ok((Object) p))  // Cast to Object to match ResponseEntity<Object>
                        .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("Professor not found")));

            default:
                // Handle unsupported roles or errors
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorResponse("Access denied for this role"));
        }
    }

}
