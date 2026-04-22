package com.mentorship.food_delivery_app.restaurant.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "restaurant")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "restaurant_id")
    private UUID id;

    @Column(name = "restaurant_name", nullable = false, length = 100)
    private String name;

    @Column(name = "restaurant_description", nullable = false)
    private String description;

    @OneToMany(mappedBy = "restaurant", orphanRemoval = true)
    private Set<RestaurantRate> ratings;


    @OneToMany(mappedBy = "restaurant",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH}, orphanRemoval = true)
    private Set<Coupon> coupons;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "restaurant_category",
            joinColumns = @JoinColumn(name = "restaurant_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<Category> categories;

    @OneToMany(mappedBy = "restaurant",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, orphanRemoval = true)
    private Set<RestaurantBranch> branches;
}
