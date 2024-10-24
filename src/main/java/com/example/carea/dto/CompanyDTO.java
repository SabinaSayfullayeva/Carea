package com.example.carea.dto;

import com.example.carea.entity.Company;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CompanyDTO {

    Long id;

    String name;

    String description;

    public CompanyDTO(Company company){
        this.id= company.getId();
        this.name=company.getName();
        this.description=company.getDescription();
    }
}
