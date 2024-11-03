package com.example.carea.controller;

import com.example.carea.dto.ApiResponse;
import com.example.carea.entity.User;
import com.example.carea.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private  final UserService userService;

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<User>>> getAlle(){
        return userService.getAll();
    }

    @GetMapping("get-by-email")
    public ResponseEntity<ApiResponse<User>> getByEmail(
            @RequestBody String json
    ){
        return userService.getByEmail(json);
    }
}
