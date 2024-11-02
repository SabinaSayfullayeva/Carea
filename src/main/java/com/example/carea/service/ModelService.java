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
            // JSON ma'lumotini parse qilish
            ModelCreateDTO modelCreateDTO = objectMapper.readValue(json, ModelCreateDTO.class);

            // Model yaratish
            Model model = new Model();
            model.setName(modelCreateDTO.getName());
            model.setStatus(modelCreateDTO.getStatus());
            model.setPrice(modelCreateDTO.getPrice());
            model.setColor(modelCreateDTO.getColor());
            model.setRating(modelCreateDTO.getRating());
            model.setDescription(modelCreateDTO.getDescription());
            model.setPhoto(photoService.save(modelCreateDTO.getPhotoUrl()));

            // Brend va kompaniya tekshirish
            model.setBrand(validateBrand(modelCreateDTO.getBrandId()));
            model.setCompany(validateCompany(modelCreateDTO.getCompanyId()));

            // Modelni saqlash
            Model savedModel = modelRepository.save(model);
            response.setData(new ModelDTO(savedModel));
            response.setMessage("Model successfully added");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (JsonProcessingException e) {
            response.setMessage("Invalid JSON format: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);

        } catch (NotFoundException | NullPointerException e) {
            response.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);

        } catch (Exception e) {
            response.setMessage("An error occurred: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // Yordamchi metodlar
    private Brand validateBrand(Long brandId) {
        if (brandId == null) {
            throw new NullPointerException("Brand is null");
        }
        return brandRepository.findById(brandId)
                .orElseThrow(() -> new NotFoundException("Brand not found with id: " + brandId));
    }

    private Company validateCompany(Long companyId) {
        if (companyId == null) {
            throw new NullPointerException("Company is null");
        }
        return companyRepository.findById(companyId)
                .orElseThrow(() -> new NotFoundException("Company not found with id: " + companyId));
    }


    public ResponseEntity<ApiResponse<List<ModelDTO>>> getAll(String name, Long brandId) {
        ApiResponse<List<ModelDTO>> response = new ApiResponse<>();

        try {
            // Filtrlar bo'yicha modelni qidirish
            Specification<Model> specification = ModelSpecification.byFilters(name, brandId);
            List<Model> all = modelRepository.findAll(specification);
            List<ModelDTO> dtos = new ArrayList<>();

            for (Model model : all) {
                dtos.add(new ModelDTO(model));
            }

            // Javobni sozlash
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
