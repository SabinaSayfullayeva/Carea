package com.example.carea.controller;

import com.example.carea.dto.ApiResponse;
import com.example.carea.dto.PhotoDTO;
import com.example.carea.entity.Photo;
import com.example.carea.repository.PhotoRepository;
import com.example.carea.service.PhotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/photo")
public class PhotoController
{

    private final PhotoService photoService;


    @GetMapping("/all")
        public ResponseEntity<ApiResponse<List<Photo>>> allPhoto()
    {
        return photoService.getAll();
    }

    @DeleteMapping("/{id}")
     public ResponseEntity<ApiResponse<?>> deletePhoto(
            @PathVariable  Long id)
    {
        return photoService.delete(id);
    }
}

