package com.example.carea.dto;

import com.example.carea.entity.Brand;
import com.example.carea.entity.Photo;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BrandDTO {

    Long id;

    PhotoDTO icon;

    String name;

    public BrandDTO(Brand entity){
        this.id=entity.getId();
        this.icon=new PhotoDTO(entity.getIcon());
        this.name=entity.getName();
    }
}
