package com.example.carea.dto;

import com.example.carea.entity.*;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.MDC;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ModelDTO {

    Long id;

    PhotoDTO photo;

    String name;

    BrandDTO brand;

    String status;

    String price;

    String color;

    String rating;

    String description;

    CompanyDTO company;

    public ModelDTO(Model model){
        this.id= model.getId();
        this.photo=new PhotoDTO(model.getPhoto());
        this.name=model.getName();
        this.brand=new BrandDTO(model.getBrand());
        this.status=model.getStatus();
        this.price=model.getPrice();
        this.color=model.getColor();
        this.rating=model.getRating();
        this.description=model.getDescription();
        this.company=new CompanyDTO(model.getCompany());
    }
}
