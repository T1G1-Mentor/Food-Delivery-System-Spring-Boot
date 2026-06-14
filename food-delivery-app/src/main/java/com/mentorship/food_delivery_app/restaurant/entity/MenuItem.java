package com.mentorship.food_delivery_app.restaurant.entity;

import com.mentorship.food_delivery_app.common.audit.Auditable;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "menu_item")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamicUpdate
public class MenuItem extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "menu_item_id")
    private UUID menuItemId;

    @Column(name = "menu_item_description", length = 255)
    private String description;

    @Column(name = "menu_item_name", nullable = false, length = 50)
    private String name;

    @Column(name = "menu_item_price", nullable = false, precision = 9, scale = 2)
    private BigDecimal price;

    @Column(name = "is_available")
    private boolean isAvailable;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_menu_id", nullable = false)
    private RestaurantMenu menu;

    public RestaurantBranch getRestaurantBranch() {
        return this.menu.getRestaurantBranch();
    }

    public void applyModifications(String name, String description, BigDecimal price) {
        if (name != null && !this.name.equals(name))
            this.name = name;
        if (description != null && !this.description.equals(description))
            this.description = description;
        if (price != null && !this.price.equals(price))
            this.price = price;

    }
}
