package com.example.carea.controller;

import com.example.carea.dto.ApiResponse;
import com.example.carea.dto.BrandDTO;
import com.example.carea.dto.CompanyDTO;
import com.example.carea.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/company")
public class CompanyController {

    @PostMapping("/add")
    public ResponseEntity<ApiResponse<CompanyDTO>> create(
            @RequestBody String json){
        return companyService.create(json);
    }
    private final CompanyService companyService;

    @GetMapping("/get/{id}")
    public ResponseEntity<ApiResponse<CompanyDTO>> getById(
            @PathVariable Long id) {
       return companyService.getById(id);
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<CompanyDTO>>> getAll() {
        return companyService.getAll();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<?>> delete(@PathVariable Long id){
        return companyService.delete(id);
    }
}
