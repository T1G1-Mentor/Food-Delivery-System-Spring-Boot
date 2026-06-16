package com.mentorship.food_delivery_app.restaurant.entity;

import com.mentorship.food_delivery_app.common.audit.Auditable;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "restaurant_menu")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamicUpdate
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

    public static RestaurantMenu createMenu(String menuName) {
        return RestaurantMenu.
                builder()
                .name(menuName)
                .isEnabled(true).build();
    }

    public void applyModifications(String menuName) {
        if (menuName != null && !menuName.equals(this.name))
            this.name = menuName;
    }
}
