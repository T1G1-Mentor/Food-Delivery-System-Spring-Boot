package com.mentorship.food_delivery_app.restaurant.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "restaurant")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamicUpdate
@SQLDelete(sql = "UPDATE restaurant SET is_deleted = true WHERE restaurant_id = ?")
@SQLRestriction("is_deleted = false")
public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "restaurant_id")
    private UUID restaurantId;

    @Column(name = "restaurant_name", nullable = false, length = 100)
    private String name;

    @Column(name = "restaurant_description", nullable = false)
    private String description;

    @Column(name = "is_deleted")
    private boolean isDeleted;

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

    public void applyModifications(String name, String description) {
        if (name != null && !name.equals(this.name))
            this.name = name;
        if (description != null && !description.equals(this.description))
            this.description = description;
    }
}
