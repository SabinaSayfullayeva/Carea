package com.example.carea.service;


import com.example.carea.dto.*;
import com.example.carea.entity.User;
import com.example.carea.repository.UserRepository;
import com.example.carea.repository.UserRoleRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
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
                response.setMessage("Email yoki parol noto‘g‘ri");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }

            User user = optional.get();


            if (passwordEncoder.matches(signInDTO.getPassword(), user.getPassword())) {
                Token token = new Token(tokenService.generateToken(signInDTO.getEmail()));
                response.setMessage(String.format("Tizimga kirildi: %s.", user.getEmail()));
                response.setData(token);
                return ResponseEntity.status(HttpStatus.OK).body(response);
            } else {
                response.setMessage("Email yoki parol noto‘g‘ri");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }

        } catch (JsonProcessingException e) {
            response.setMessage("Kiritilgan JSON formatida xato: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);

        } catch (Exception e) {
            response.setMessage("Xatolik yuz berdi: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }


    // Sign-up method
    public ResponseEntity<ApiResponse<Token>> signUp(String json) {
        ApiResponse<Token> response = new ApiResponse<>();
        try {

            SignUpDTO signUpDTO = objectMapper.readValue(json, SignUpDTO.class);


            Optional<User> optional = userRepository.findByEmail(signUpDTO.getEmail());

            if (optional.isPresent()) {
                response.setMessage("Bu email bilan foydalanuvchi allaqachon ro'yxatdan o'tgan");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }


            User newUser = new User();
            newUser.setEmail(signUpDTO.getEmail());
            newUser.setUsername(signUpDTO.getUsername());
            newUser.setRole(userRoleRepository.findById(1L).orElseThrow(() -> new RuntimeException("Role not found")));
            newUser.setPassword(passwordEncoder.encode(signUpDTO.getPassword())); // Parolni shifrlash

            userRepository.save(newUser);
            Token token = new Token(tokenService.generateToken(signUpDTO.getEmail()));
            response.setMessage("Muvaffaqiyatli ro'yxatdan o'tildi.");
            response.setData(token);
            return ResponseEntity.status(HttpStatus.OK).body(response);

        } catch (JsonProcessingException e) {
            response.setMessage("Kiritilgan JSON formatida xato: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);

        } catch (Exception e) {
            response.setMessage("Xatolik yuz berdi: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }


    public ResponseEntity<ApiResponse<List<User>>> getAll(){
        ApiResponse<List<User>> response=new ApiResponse<>();
        List<User> all = userRepository.findAll();
        response.setData(all);
        response.setMessage("Found "+ all.size()+" user(s)");

        return ResponseEntity.ok(response);
    }


    public ResponseEntity<ApiResponse<User>> getByEmail(String json)
    {
        ApiResponse<User> response=new ApiResponse<>();
        try {
            UserSearchDTO userSearchDTO = objectMapper.readValue(json, UserSearchDTO.class);
            Optional<User> byEmail = userRepository.findByEmail(userSearchDTO.getEmail());
            if (byEmail.isEmpty()){
                response.setMessage("User not found with email : "+userSearchDTO.getEmail());
            }else {
                response.setData(byEmail.get());
                response.setMessage("User found");
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok(response);
    }
}

