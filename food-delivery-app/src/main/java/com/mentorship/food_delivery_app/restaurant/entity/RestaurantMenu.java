package com.mentorship.food_delivery_app.restaurant.entity;

import com.mentorship.food_delivery_app.common.audit.Auditable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "restaurant_menu")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantMenu extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "restaurant_menu_id")
    private UUID restaurantMenuId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_menu_rest_branch_id", nullable = false)
    private RestaurantBranch restaurantBranch;

    @Column(name = "restaurant_menu_name", nullable = false, length = 30)
    private String name;

    @OneToMany(mappedBy = "menu",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, orphanRemoval = true)
    private Set<MenuItem> items;

    @Column(name = "is_enabled")
    private boolean isEnabled;

    public static RestaurantMenu createMenu( String menuName) {
        return RestaurantMenu.
                builder()
                .name(menuName)
                .isEnabled(true).build();
    }
}
