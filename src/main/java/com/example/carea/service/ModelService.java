package com.example.carea.service;

import com.example.carea.dto.*;
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


    public ResponseEntity<ApiResponse<ModelDTO>> create(String json, MultipartFile photo) {
        ApiResponse<ModelDTO> response = new ApiResponse<>();
        try {
            // JSON formatdagi ma'lumotlarni parse qilish
            ModelCreateDTO modelCreateDTO = objectMapper.readValue(json, ModelCreateDTO.class);
            Model model = new Model();
            model.setName(modelCreateDTO.getName());
            model.setStatus(modelCreateDTO.getStatus());
            model.setPrice(modelCreateDTO.getPrice());
            model.setColor(modelCreateDTO.getColor());
            model.setRating(modelCreateDTO.getRating());
            model.setDescription(modelCreateDTO.getDescription());

            // Foto faylni saqlash
            model.setPhoto(photoService.save(photo));

            // Brand tekshirish va sozlash
            if (modelCreateDTO.getBrandId() == null) {
                throw new NullPointerException("Brand is null");
            } else if (brandRepository.findById(modelCreateDTO.getBrandId()).isEmpty()) {
                throw new NotFoundException("Brand not found with id: " + modelCreateDTO.getBrandId());
            } else {
                model.setBrand(brandRepository.findById(modelCreateDTO.getBrandId()).get());
            }

            // Company tekshirish va sozlash
            if (modelCreateDTO.getCompanyId() == null) {
                throw new NullPointerException("Company is null");
            } else if (companyRepository.findById(modelCreateDTO.getCompanyId()).isEmpty()) {
                throw new NotFoundException("Company not found with id: " + modelCreateDTO.getCompanyId());
            } else {
                model.setCompany(companyRepository.findById(modelCreateDTO.getCompanyId()).get());
            }

            // Modelni saqlash
            Model savedModel = modelRepository.save(model);
            response.setData(new ModelDTO(savedModel));
            response.setMessage("Model successfully added");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (JsonProcessingException e) {
            // JSON xatolarini ushlash
            response.setMessage("Invalid JSON format: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);

        } catch (NotFoundException e) {
            // Brand yoki Company topilmagan holatlarda
            response.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);

        } catch (NullPointerException e) {
            // Null qiymatlar bilan bog'liq xatolarni ushlash
            response.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);

        } catch (Exception e) {
            // Boshqa xatolarni ushlash
            response.setMessage("An error occurred: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    public ResponseEntity<ApiResponse<List<ModelDTO>>> getAll(String name,Long brandId){
        Specification<Model> specification = ModelSpecification.byFilters(
                name,brandId);
        ApiResponse<List<ModelDTO>> response = new ApiResponse<>();
        List<Model> all = modelRepository.findAll(specification);
        List<ModelDTO> dtos = new ArrayList<>();

        for (Model model : all) {
            dtos.add(new ModelDTO(model));
        }

        response.setData(dtos);
        response.setMessage("Found " + dtos.size() + " model");

        return ResponseEntity.ok(response);
    }


    public ResponseEntity<ApiResponse<ModelDTO>> getById(Long id) {
        ApiResponse<ModelDTO> response = new ApiResponse<>();


        Optional<Model> byId = modelRepository.findById(id);


        if (byId.isEmpty()) {

            response.setMessage("Model not found with id: " + id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        response.setData(new ModelDTO(byId.get()));
        response.setMessage("Model found");

        return ResponseEntity.ok(response);
    }

    public ResponseEntity<ApiResponse<?>> delete(Long id) {
        ApiResponse<?> response = new ApiResponse<>();

        // Model mavjudligini tekshirish
        Optional<Model> modelOptional = modelRepository.findById(id);
        if (modelOptional.isEmpty()) {
            throw new NotFoundException("Model not found with id: " + id);
        } else {
            // Modelni o'chirish
            modelRepository.deleteById(id);
            response.setMessage("Model successfully deleted");
        }

        return ResponseEntity.ok(response);
    }

}
