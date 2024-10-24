package com.example.carea.service;

import com.example.carea.dto.ApiResponse;
import com.example.carea.dto.BrandDTO;
import com.example.carea.dto.CompanyCreateDTO;
import com.example.carea.dto.CompanyDTO;
import com.example.carea.entity.Brand;
import com.example.carea.entity.Company;
import com.example.carea.exception.NotFoundException;
import com.example.carea.repository.CompanyRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final ObjectMapper objectMapper;


    public ResponseEntity<ApiResponse<CompanyDTO>> create(String json) {
        ApiResponse<CompanyDTO> response = new ApiResponse<>();
        try {
            // JSON ma'lumotini parse qilish
            CompanyCreateDTO companyCreateDTO = objectMapper.readValue(json, CompanyCreateDTO.class);

            // Yangi company yaratish va saqlash
            Company company = new Company();
            company.setName(companyCreateDTO.getName());
            company.setDescription(companyCreateDTO.getDescription());
            Company save = companyRepository.save(company);

            // Javobni sozlash
            response.setData(new CompanyDTO(save));
            response.setMessage("Brand successfully added");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (JsonProcessingException e) {
            // JSON parse qilishda xatolik bo'lsa
            response.setMessage("Invalid JSON format: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);

        } catch (Exception e) {
            // Boshqa xatolarni ushlash
            response.setMessage("An error occurred: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    public ResponseEntity<ApiResponse<List<CompanyDTO>>> getAll(){
        ApiResponse<List<CompanyDTO>> response = new ApiResponse<>();
        List<Company> all = companyRepository.findAll();
        List<CompanyDTO> dtos = new ArrayList<>();

        for (Company company : all) {
            dtos.add(new CompanyDTO(company));
        }

        response.setData(dtos);
        response.setMessage("Found " + dtos.size() + " company");

        return ResponseEntity.ok(response);
    }


    public ResponseEntity<ApiResponse<CompanyDTO>> getById(Long id) {
        ApiResponse<CompanyDTO> response = new ApiResponse<>();


        Optional<Company> byId = companyRepository.findById(id);


        if (byId.isEmpty()) {

            response.setMessage("Company not found with id: " + id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }


        response.setData(new CompanyDTO(byId.get()));
        response.setMessage("Company found");

        return ResponseEntity.ok(response);
    }

    public ResponseEntity<ApiResponse<?>> delete(Long id) {
        ApiResponse<?> response = new ApiResponse<>();
        Optional<Company> byId = companyRepository.findById(id);
        if (byId.isEmpty()) {
            throw new NotFoundException("Company not found with id: " + id);
        }else {
            companyRepository.deleteById(id);
            response.setMessage("Successfully deleted");
        }
        return ResponseEntity.ok(response);
    }

}
