package com.example.carea.service;


import com.example.carea.dto.ApiResponse;
import com.example.carea.dto.PhotoDTO;
import com.example.carea.entity.Photo;
import com.example.carea.exception.ApiException;
import com.example.carea.exception.IllegalPhotoTypeException;
import com.example.carea.exception.NotFoundException;
import com.example.carea.repository.PhotoRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Service
@RequiredArgsConstructor
public class PhotoService {

    private final PhotoRepository photoRepo;

    private static final Logger logger = LoggerFactory.getLogger(PhotoService.class);

    public Photo save(String url) {
        Photo photo = new Photo();
        photo.setUrl(url);

        return photoRepo.save(photo);

    }

    public Photo getEmpty() {
        return photoRepo.save(new Photo());
    }


    public ResponseEntity<ApiResponse<List<Photo>>> getAll() {
        ApiResponse<List<Photo>> response = new ApiResponse<>();
        List<Photo> all = photoRepo.findAll();
        response.setData(all);
        response.setMessage("Found "+ all.size()+" photo(s)");

        return ResponseEntity.ok(response);
    }

    public ResponseEntity<ApiResponse<?>> delete(Long id) {
        ApiResponse<String> response = new ApiResponse<>();

        // photo mavjud yoki yo‘qligini tekshirish
        if (photoRepo.existsById(id)) {
            // photo mavjud bo‘lsa, o‘chirib tashlash
            photoRepo.deleteById(id);
            response.setMessage("Deleted");
        } else {
            // photo mavjud bo‘lmasa, "Not found" xabarini qaytarish
            response.setMessage("Photo not found by id: " + id);
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
