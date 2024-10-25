package com.example.carea.controller;


import com.example.carea.dto.ApiResponse;
import com.example.carea.dto.Token;
import com.example.carea.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;

@RequiredArgsConstructor
@Controller
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    @PostMapping(value = "/login", consumes = {
            MediaType.MULTIPART_FORM_DATA_VALUE
    })
    public ResponseEntity<ApiResponse<Token>> login(
            @RequestPart("email") String email,
            @RequestPart("password") String password) {
        return userService.login(email, password);
    }

    // Sign-up method
    @PostMapping(value = "/signup", consumes = {
            MediaType.MULTIPART_FORM_DATA_VALUE
    })
    public ResponseEntity<ApiResponse<Token>> signUp(
            @RequestPart("email") String email,
            @RequestPart("password") String password) {
        return userService.signUp(email, password);
    }
}

