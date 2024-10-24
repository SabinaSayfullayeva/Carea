package com.example.carea.controller;

import com.example.carea.dto.ApiResponse;
import com.example.carea.dto.BrandDTO;
import com.example.carea.service.BrandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/brand")
public class BrandController {
    private final BrandService brandService;

    @PostMapping("/add")
    public ResponseEntity<ApiResponse<BrandDTO>> create(
            @RequestParam(value = "name") String name,
            @RequestParam(value = "icon") MultipartFile file){
         return brandService.create(name,file);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<ApiResponse<BrandDTO>> getById(
            @PathVariable Long id){
        return brandService.getById(id);
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<BrandDTO>>> getAll(){
       return brandService.getAll();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<?>> delete(@PathVariable Long id){
        return brandService.delete(id);
    }
}
