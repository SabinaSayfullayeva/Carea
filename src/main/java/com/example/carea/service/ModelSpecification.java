package com.example.carea.service;

import com.example.carea.entity.Brand;
import com.example.carea.entity.Model;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

public class ModelSpecification {

    public static Specification<Model> byFilters(String name, Long brandId) {
        return (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction(); // Default conjunction (true)

            // brandId bo'yicha qidirish
            if (brandId != null) {
                Join<Model, Brand> modelBrandJoin = root.join("brand");
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(modelBrandJoin.get("id"), brandId));
            }

            // name bo'yicha qidirish (ignore case)
            if (name != null && !name.isEmpty()) {
                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
            }

            return predicate;
        };
    }
}

