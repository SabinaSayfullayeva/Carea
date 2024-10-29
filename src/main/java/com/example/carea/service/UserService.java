package com.example.carea.service;


import com.example.carea.dto.ApiResponse;
import com.example.carea.dto.Token;
import com.example.carea.entity.Role;
import com.example.carea.entity.User;
import com.example.carea.entity.UserRole;
import com.example.carea.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenService tokenService;

    public ResponseEntity<ApiResponse<Token>> login(String email, String password) {
        ApiResponse<Token> response = new ApiResponse<>();
        Optional<User> optional = userRepository.findByEmail(email);
        if (optional.isEmpty()) {
            response.setMessage("Email or password is incorrect");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
        User user = optional.get();
        if (passwordEncoder.matches(password, user.getPassword())) {
            Token token = new Token(tokenService.generateToken(email));
            response.setMessage(String.format(
                    "Successfully logged in as: %s.", user.getUsername()));
            response.setData(token);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } else {
            response.setMessage("Email or password is incorrect");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

    // Sign-up method
    public ResponseEntity<ApiResponse<Token>> signUp(String email, String password) {
        ApiResponse<Token> response = new ApiResponse<>();
        Optional<User> optional = userRepository.findByEmail(email);
        if (optional.isPresent()) {
            response.setMessage("User already exists with this email");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        // Create a new user
        User newUser = new User();
        newUser.setEmail(email);
        newUser.setRole(new UserRole(Role.USER));
        newUser.setPassword(passwordEncoder.encode(password)); // Encode the password
        newUser.setEmail(email); // You can set the username differently if needed

        userRepository.save(newUser); // Save the new user

        // Generate token for the new user
        Token token = new Token(tokenService.generateToken(email));
        response.setMessage("Successfully signed up.");
        response.setData(token);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}

