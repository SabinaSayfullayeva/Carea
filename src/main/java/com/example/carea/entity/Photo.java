package com.example.carea.entity;

import com.example.carea.dto.PhotoDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)

@Entity
public class Photo {
    private static final Logger LOGGER = LoggerFactory.getLogger(Photo.class);
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @JsonIgnore
    String name;

    @JsonIgnore
    String path;

    @JsonProperty(value = "url")
    @Column(unique = true)
    String url;

    @JsonIgnore
    String type;

    public Photo(PhotoDTO dto)
    {
        if (dto == null)
            return;

        this.id = dto.getId();
        this.url = dto.getUrl();
    }

    @PreRemove
    private void deleteFile()
    {
        if (this.path != null)
        {
            try
            {
                Files.delete(Paths.get(path));
            } catch (IOException e)
            {
                LOGGER.error("Error deleting file: ", e);
            }
        }
    }
}
