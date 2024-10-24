package com.example.carea.service;

import com.example.carea.dto.ApiResponse;
import com.example.carea.dto.BrandDTO;
import com.example.carea.entity.Brand;
import com.example.carea.exception.NotFoundException;
import com.example.carea.repository.BrandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BrandService {

    private final BrandRepository brandRepository;
    private final PhotoService photoService;

    public ResponseEntity<ApiResponse<BrandDTO>> create(String name, MultipartFile icon) {
        ApiResponse<BrandDTO> response = new ApiResponse<>();
        Brand brand = new Brand();
        brand.setName(name);
        brand.setIcon(photoService.save(icon));
        Brand save = brandRepository.save(brand);
        response.setData(new BrandDTO(save));
        response.setMessage("Brand successfully added");

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    public ResponseEntity<ApiResponse<BrandDTO>> getById(Long id) {
        ApiResponse<BrandDTO> response = new ApiResponse<>();

        // Brandni repository'dan qidirish
        Optional<Brand> byId = brandRepository.findById(id);

        // Agar topilmasa, xatolik qaytarish
        if (byId.isEmpty()) {
            response.setMessage("Brand not found with id: " + id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        // Agar topilsa, muvaffaqiyatli javob
        response.setData(new BrandDTO(byId.get()));
        response.setMessage("Brand found");

        return ResponseEntity.ok(response);
    }


    public ResponseEntity<ApiResponse<List<BrandDTO>>> getAll() {
        ApiResponse<List<BrandDTO>> response = new ApiResponse<>();
        List<Brand> all = brandRepository.findAll();
        List<BrandDTO> dtos = new ArrayList<>();

        for (Brand brand : all) {
            dtos.add(new BrandDTO(brand));
        }

        response.setData(dtos);
        response.setMessage("Found " + dtos.size() + " brand");

        return ResponseEntity.ok(response);
    }

    public ResponseEntity<ApiResponse<?>> delete(Long id) {
        ApiResponse<?> response = new ApiResponse<>();
        Optional<Brand> byId = brandRepository.findById(id);
        if (byId.isEmpty()) {
            throw new NotFoundException("Brand not found with id: " + id);
        }else {
            brandRepository.deleteById(id);
            response.setMessage("Successfully deleted");
        }
        return ResponseEntity.ok(response);
    }
}
