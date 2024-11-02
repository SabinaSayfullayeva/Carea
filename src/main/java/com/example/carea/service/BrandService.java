package com.example.carea.service;

import com.example.carea.dto.ApiResponse;
import com.example.carea.dto.BrandCreateDTO;
import com.example.carea.dto.BrandDTO;
import com.example.carea.entity.Brand;
import com.example.carea.exception.NotFoundException;
import com.example.carea.repository.BrandRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    private final ObjectMapper objectMapper;

    public ResponseEntity<ApiResponse<BrandDTO>> create(String json) {
        ApiResponse<BrandDTO> response = new ApiResponse<>();

        try {
            // JSON obyektini parsing qilish
            BrandCreateDTO brandCreateDTO = objectMapper.readValue(json, BrandCreateDTO.class);

            // Yangi brand yaratish
            Brand brand = new Brand();
            brand.setName(brandCreateDTO.getName());

            // Ikonani saqlash va belgilash
            brand.setIcon(photoService.save(brandCreateDTO.getPhotoUrl()));

            // Brandni saqlash
            Brand savedBrand = brandRepository.save(brand);

            // Javob qaytarish uchun DTO yaratish
            response.setData(new BrandDTO(savedBrand));
            response.setMessage("Brend muvaffaqiyatli qo'shildi");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (JsonProcessingException e) {
            response.setMessage("Kiritilgan JSON formatida xato: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);

        } catch (Exception e) {
            response.setMessage("Xatolik yuz berdi: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }


    public ResponseEntity<ApiResponse<BrandDTO>> getById(Long id) {
        ApiResponse<BrandDTO> response = new ApiResponse<>();

        try {
            // Brandni repository'dan qidirish
            Optional<Brand> byId = brandRepository.findById(id);

            // Agar topilmasa, xatolik qaytarish
            if (byId.isEmpty()) {
                response.setMessage("Ushbu ID: " + id + " bilan brend topilmadi");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            // Agar topilsa, muvaffaqiyatli javob
            response.setData(new BrandDTO(byId.get()));
            response.setMessage("Brend topildi");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.setMessage("Xatolik yuz berdi: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }



    public ResponseEntity<ApiResponse<List<BrandDTO>>> getAll() {
        ApiResponse<List<BrandDTO>> response = new ApiResponse<>();

        try {
            // Barcha brendlarni olish
            List<Brand> all = brandRepository.findAll();
            List<BrandDTO> dtos = new ArrayList<>();

            for (Brand brand : all) {
                dtos.add(new BrandDTO(brand));
            }

            // Javobni sozlash
            response.setData(dtos);
            response.setMessage(dtos.size() + " ta brend topildi");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.setMessage("Xatolik yuz berdi: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }


    public ResponseEntity<ApiResponse<?>> delete(Long id) {
        ApiResponse<?> response = new ApiResponse<>();

        try {
            // Brendni repository'dan ID bo'yicha qidirish
            Optional<Brand> byId = brandRepository.findById(id);

            if (byId.isEmpty()) {
                response.setMessage("ID " + id + " bo'yicha brend topilmadi");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            } else {
                // Agar brend topilsa, o'chirish
                brandRepository.deleteById(id);
                response.setMessage("Muvaffaqiyatli o'chirildi");
                return ResponseEntity.ok(response);
            }

        } catch (Exception e) {
            response.setMessage("Xatolik yuz berdi: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

}
