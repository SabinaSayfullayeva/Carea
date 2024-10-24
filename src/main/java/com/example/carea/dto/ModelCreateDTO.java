package com.example.carea.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ModelCreateDTO {

    String name;

    Long brandId;

    String status;

    String price;

    String color;

    String rating;

    String description;

    Long companyId;

}
