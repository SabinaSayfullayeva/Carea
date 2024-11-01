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
@RequestMapping("/photo")
public class PhotoController
{

    private final PhotoService photoService;


    @PostMapping()
    public ResponseEntity<ApiResponse<List<Photo>>> upload(
            @RequestPart(name = "photo", required = false) List<MultipartFile> photo)
    {
        return photoService.upload(photo);
    }

    @GetMapping("/{name}")
    public ResponseEntity<byte[]> getPhoto(@PathVariable(name = "name") String name)
    {
        return photoService.findByName(name);
    }


    @PutMapping(value = "/{id}", consumes = {"multipart/form-data"})
    public ResponseEntity<ApiResponse<PhotoDTO>> updatePhoto(
            @PathVariable(name = "id")
            Long id,


            @RequestPart(name = "new-photo", required = false) MultipartFile newPhoto)
    {
        return photoService.update(id, newPhoto);
    }

    @DeleteMapping("/{id}")
        public ResponseEntity<ApiResponse<?>> deletePhoto(
            @PathVariable
             Long id)
    {
        return photoService.delete(id);
    }
}

