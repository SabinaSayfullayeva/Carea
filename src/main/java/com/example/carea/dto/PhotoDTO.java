package com.example.carea.dto;

import com.example.carea.entity.Photo;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PhotoDTO
{
    Long id;

    String url;

    public PhotoDTO(Photo entity)
    {
        if (entity == null)
            return;
        this.id = entity.getId();
        this.url = entity.getUrl();
    }
}
