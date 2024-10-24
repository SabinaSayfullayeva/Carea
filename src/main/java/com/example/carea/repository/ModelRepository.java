package com.example.carea.repository;

import com.example.carea.entity.Model;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ModelRepository  extends JpaRepository<Model,Long>, JpaSpecificationExecutor<Model> {
}
