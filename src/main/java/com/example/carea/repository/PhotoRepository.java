package com.example.carea.repository;

import com.example.carea.dto.PhotoDTO;
import com.example.carea.entity.Photo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface PhotoRepository extends JpaRepository<Photo,Long> {

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM photo WHERE id= :id", nativeQuery = true)
    void delete(Long id);

    Optional<Photo> findByUrl(String url);

    @Modifying
    @Query(value = "UPDATE photo SET url= :url WHERE id= :id", nativeQuery = true)
    void setUrl(String url, Long id);
}
