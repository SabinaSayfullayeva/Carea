package com.example.carea.controller;


import com.example.carea.dto.ApiResponse;
import com.example.carea.dto.Token;
import com.example.carea.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;

@RequiredArgsConstructor
@Controller
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    @PostMapping(value = "/login")
    public ResponseEntity<ApiResponse<Token>> login(
            @RequestBody String json) {
        return userService.login(json);
    }

    // Sign-up method
    @PostMapping(value = "/signup")
    public ResponseEntity<ApiResponse<Token>> signUp(
            @RequestBody String json) {
        return userService.signUp(json);
    }
}

