package com.mentorship.food_delivery_app.restaurant.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "category")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Integer id;

    @Column(name = "category_name", nullable = false, length = 20)
    private String name;

    @ManyToMany(mappedBy = "categories") //lazy by default
    private Set<Restaurant> restaurants;
}
