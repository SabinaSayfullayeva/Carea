package com.example.carea.controller;

import com.example.carea.dto.ApiResponse;
import com.example.carea.dto.BrandDTO;
import com.example.carea.dto.CompanyDTO;
import com.example.carea.dto.ModelDTO;
import com.example.carea.service.BrandService;
import com.example.carea.service.CompanyService;
import com.example.carea.service.ModelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/model")
public class ModelController {


    private final ModelService modelService;


    @PostMapping("/add")
    public ResponseEntity<ApiResponse<ModelDTO>> create(
            @RequestParam(value = "json") String json,
            @RequestParam(value = "photo") MultipartFile file) {
        return modelService.create(json, file);
    }


    @GetMapping("/get/{id}")
    public ResponseEntity<ApiResponse<ModelDTO>> getById(
            @PathVariable Long id) {
        return modelService.getById(id);
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<ModelDTO>>> getAll(
            @RequestParam(required = false,value = "name") String name,
            @RequestParam(required = false,value = "brandId") Long brandId
    ) {
        return modelService.getAll(name,brandId);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<?>> delete(@PathVariable Long id) {
        return modelService.delete(id);
    }
}
