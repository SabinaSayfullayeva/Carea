package com.example.carea.service;

import com.example.carea.dto.*;
import com.example.carea.entity.Brand;
import com.example.carea.entity.Company;
import com.example.carea.entity.Model;
import com.example.carea.exception.NotFoundException;
import com.example.carea.repository.BrandRepository;
import com.example.carea.repository.CompanyRepository;
import com.example.carea.repository.ModelRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ModelService {

    private final ModelRepository modelRepository;
    private final PhotoService photoService;
    private final BrandRepository brandRepository;
    private final CompanyRepository companyRepository;
    private final ObjectMapper objectMapper;


    public ResponseEntity<ApiResponse<ModelDTO>> create(String json) {
        ApiResponse<ModelDTO> response = new ApiResponse<>();

        try {
            // JSON ni ModelCreateDTO ga o‘girish
            ModelCreateDTO modelCreateDTO = objectMapper.readValue(json, ModelCreateDTO.class);

            // Tekshiruv: ModelCreateDTO ning asosiy maydonlari null emasligini tekshirish
            if (modelCreateDTO.getName() == null || modelCreateDTO.getStatus() == null || modelCreateDTO.getPrice() == null) {
                response.setMessage("Name, status yoki price bo'sh bo'lmasligi kerak");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            // Yangi Model obyektini yaratish
            Model model = new Model();
            model.setName(modelCreateDTO.getName());
            model.setStatus(modelCreateDTO.getStatus());
            model.setPrice(modelCreateDTO.getPrice());
            model.setColor(modelCreateDTO.getColor());
            model.setRating(modelCreateDTO.getRating());
            model.setDescription(modelCreateDTO.getDescription());

            // Photo URL null emasligini tekshirish va foto saqlash
            if (modelCreateDTO.getPhotoUrl() != null) {
                model.setPhoto(photoService.save(modelCreateDTO.getPhotoUrl()));
            } else {
                response.setMessage("Photo URL bo'sh bo'lishi mumkin emas");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            // Brand va Company null emasligini tekshirish va ularni tekshirish
            model.setBrand(validateBrand(modelCreateDTO.getBrandId()));
            model.setCompany(validateCompany(modelCreateDTO.getCompanyId()));

            // Modelni saqlash
            Model savedModel = modelRepository.save(model);
            response.setData(new ModelDTO(savedModel));
            response.setMessage("Model muvaffaqiyatli qo'shildi");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (JsonProcessingException e) {
            response.setMessage("JSON formati noto'g'ri: " + e.getOriginalMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);

        } catch (NotFoundException | NullPointerException e) {
            response.setMessage("Xatolik: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);

        } catch (Exception e) {
            response.setMessage("Xato yuz berdi: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // Yordamchi metodlar
    private Brand validateBrand(Long brandId) {
        if (brandId == null) {
            throw new NullPointerException("Brand ID bo'sh bo'lmasligi kerak");
        }
        return brandRepository.findById(brandId)
                .orElseThrow(() -> new NotFoundException("Brand topilmadi, ID: " + brandId));
    }

    private Company validateCompany(Long companyId) {
        if (companyId == null) {
            throw new NullPointerException("Company ID bo'sh bo'lmasligi kerak");
        }
        return companyRepository.findById(companyId)
                .orElseThrow(() -> new NotFoundException("Company topilmadi, ID: " + companyId));
    }


    public ResponseEntity<ApiResponse<List<ModelDTO>>> getAll(String json) {
        ApiResponse<List<ModelDTO>> response = new ApiResponse<>();
        List<ModelDTO> dtos = new ArrayList<>();

        try {
            ModelSearchDTO modelSearchDTO = objectMapper.readValue(json, ModelSearchDTO.class);

            // Ixtiyoriy qiymatlar uchun tekshirish
            String name = Optional.ofNullable(modelSearchDTO.getName()).orElse("");
            Long brandId = modelSearchDTO.getBrandId();

            // Filtr bo'yicha qidiruv yoki barcha elementlarni olish
            if (!name.isBlank() || brandId != null) {
                Specification<Model> specification = ModelSpecification.byFilters(name, brandId);
                List<Model> all = modelRepository.findAll(specification);

                for (Model model : all) {
                    dtos.add(new ModelDTO(model));
                }
            } else {
                List<Model> all = modelRepository.findAll();

                for (Model model : all) {
                    dtos.add(new ModelDTO(model));
                }
            }

            response.setData(dtos);
            response.setMessage("Found " + dtos.size() + " models");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.setMessage("An error occurred: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }




    public ResponseEntity<ApiResponse<ModelDTO>> getById(Long id) {
        ApiResponse<ModelDTO> response = new ApiResponse<>();

        try {
            // Modelni ID bo'yicha qidirish
            Optional<Model> byId = modelRepository.findById(id);

            if (byId.isEmpty()) {
                response.setMessage("Model not found with id: " + id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            // Model topilgan bo'lsa, DTO yaratish
            response.setData(new ModelDTO(byId.get()));
            response.setMessage("Model found");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.setMessage("An error occurred: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }


    public ResponseEntity<ApiResponse<?>> delete(Long id) {
        ApiResponse<?> response = new ApiResponse<>();

        try {
            // Model mavjudligini tekshirish
            Optional<Model> modelOptional = modelRepository.findById(id);

            if (modelOptional.isEmpty()) {
                response.setMessage("Model not found with id: " + id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            // Modelni o'chirish
            modelRepository.deleteById(id);
            response.setMessage("Model successfully deleted");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.setMessage("An error occurred: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }


}
