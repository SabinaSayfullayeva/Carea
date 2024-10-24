package com.example.carea.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor

@Entity
public class Model {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @OneToOne
    Photo photo;

    String name;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    Brand brand;

    String status;

    String price;

    String color;

    String rating;

    String description;

    @ManyToOne
    Company company;


}
