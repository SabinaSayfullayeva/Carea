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
            // JSON obyektini parsing qilish
            SignInDTO signInDTO = objectMapper.readValue(json, SignInDTO.class);

            // Foydalanuvchini email bo'yicha izlash
            Optional<User> optional = userRepository.findByEmail(signInDTO.getEmail());

            if (optional.isEmpty()) {
                response.setMessage("Email yoki parol noto‘g‘ri");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }

            User user = optional.get();

            // Parolni tekshirish
            if (passwordEncoder.matches(signInDTO.getPassword(), user.getPassword())) {
                Token token = new Token(tokenService.generateToken(signInDTO.getEmail()));
                response.setMessage(String.format("Tizimga kirildi: %s.", user.getUsername()));
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
            // JSON obyektini parsing qilish
            SignUpDTO signUpDTO = objectMapper.readValue(json, SignUpDTO.class);

            // Foydalanuvchini email bo'yicha tekshirish
            Optional<User> optional = userRepository.findByEmail(signUpDTO.getEmail());

            if (optional.isPresent()) {
                response.setMessage("Bu email bilan foydalanuvchi allaqachon ro'yxatdan o'tgan");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            // Yangi foydalanuvchi yaratish
            User newUser = new User();
            newUser.setEmail(signUpDTO.getEmail());
            newUser.setUsername(signUpDTO.getUsername());
            newUser.setRole(userRoleRepository.findById(1L).orElseThrow(() -> new RuntimeException("Role not found")));
            newUser.setPassword(passwordEncoder.encode(signUpDTO.getPassword())); // Parolni shifrlash

            userRepository.save(newUser); // Yangi foydalanuvchini saqlash

            // Yangi foydalanuvchi uchun token yaratish
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

}

