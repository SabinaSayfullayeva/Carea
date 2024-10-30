package com.example.carea.service;


import com.example.carea.dto.ApiResponse;
import com.example.carea.dto.SignInDTO;
import com.example.carea.dto.SignUpDTO;
import com.example.carea.dto.Token;
import com.example.carea.entity.Role;
import com.example.carea.entity.User;
import com.example.carea.entity.UserRole;
import com.example.carea.repository.UserRepository;
import com.example.carea.repository.UserRoleRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    private final ObjectMapper objectMapper;
    private final UserRoleRepository userRoleRepository;

    public ResponseEntity<ApiResponse<Token>> login(String json) {
        ApiResponse<Token> response = new ApiResponse<>();
        try {
            SignInDTO signInDTO = objectMapper.readValue(json, SignInDTO.class);
            Optional<User> optional = userRepository.findByEmail(signInDTO.getEmail());
            if (optional.isEmpty()) {
                response.setMessage("Email or password is incorrect");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
            User user = optional.get();
            if (passwordEncoder.matches(signInDTO.getPassword(), user.getPassword())) {
                Token token = new Token(tokenService.generateToken(signInDTO.getEmail()));
                response.setMessage(String.format(
                        "Successfully logged in as: %s.", user.getUsername()));
                response.setData(token);
                return ResponseEntity.status(HttpStatus.OK).body(response);
            } else {
                response.setMessage("Email or password is incorrect");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }

    // Sign-up method
    public ResponseEntity<ApiResponse<Token>> signUp(String json) {
        ApiResponse<Token> response = new ApiResponse<>();
        try {
            SignUpDTO signUpDTO = objectMapper.readValue(json, SignUpDTO.class);
            Optional<User> optional = userRepository.findByEmail(signUpDTO.getEmail());
            if (optional.isPresent()) {
                response.setMessage("User already exists with this email");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            // Create a new user
            User newUser = new User();
            newUser.setEmail(signUpDTO.getEmail());
            newUser.setUsername(signUpDTO.getUsername());
            newUser.setRole(userRoleRepository.findById(1L).get());
            newUser.setPassword(passwordEncoder.encode(signUpDTO.getPassword())); // Encode the password
            newUser.setEmail(signUpDTO.getEmail()); // You can set the username differently if needed

            userRepository.save(newUser); // Save the new user

            // Generate token for the new user
            Token token = new Token(tokenService.generateToken(signUpDTO.getEmail()));
            response.setMessage("Successfully signed up.");
            response.setData(token);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }





        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}

