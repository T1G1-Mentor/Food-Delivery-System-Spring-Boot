package com.mentorship.food_delivery_app.restaurant.entity;

import com.mentorship.food_delivery_app.common.audit.Auditable;
import com.mentorship.food_delivery_app.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "restaurant_branch")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamicUpdate
@SQLDelete(sql = "UPDATE restaurant_branch SET is_deleted = true WHERE branch_id = ?")
@SQLRestriction("is_deleted = false")
public class RestaurantBranch extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "branch_id")
    private UUID restaurantBranchId;


    @Column(name = "branch_delivery_fee", precision = 6, scale = 2)
    private BigDecimal deliveryFee;

    @Column(name = "branch_min_order", precision = 6, scale = 2)
    private BigDecimal minOrder;

    @Column(name = "branch_city", nullable = false, length = 20)
    private String city;

    @Column(name = "branch_open_time", nullable = false)
    private LocalTime openTime;

    @Column(name = "branch_close_time", nullable = false)
    private LocalTime closeTime;

    @Column(name = "branch_phone_number", nullable = false, length = 15)
    private String phoneNumber;

    @Column(name = "branch_estimated_delivery_time")
    private Integer estimatedDeliveryTime;

    @Column(name = "is_enabled")
    private boolean isEnabled;

    @Column(name = "is_deleted")
    private boolean isDeleted;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id")
    private User admin;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_rest_id", nullable = false)
    private Restaurant restaurant;


    @OneToMany(mappedBy = "restaurantBranch",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, orphanRemoval = true)
    private Set<RestaurantMenu> menus;

    public boolean isOpen() {
        LocalTime currentTime = LocalTime.now();
        if (openTime.isBefore(closeTime)) {
            return !currentTime.isBefore(openTime)
                    && currentTime.isBefore(closeTime);
        }
        return !currentTime.isBefore(openTime)
                || currentTime.isBefore(closeTime);
    }

    public String getRestaurantName(){
        return this.restaurant.getName();
    }

    public void applyModifications(BigDecimal deliveryFee, BigDecimal minOrder, String city,
                                   LocalTime openTime, LocalTime closeTime, String phoneNumber,
                                   Integer estimatedDeliveryTime) {
        if (city != null && !city.equals(this.city)) this.city = city;
        if (deliveryFee != null) this.deliveryFee = deliveryFee;
        if (minOrder != null) this.minOrder = minOrder;
        if (openTime != null) this.openTime = openTime;
        if (closeTime != null) this.closeTime = closeTime;
        if (phoneNumber != null && !phoneNumber.equals(this.phoneNumber)) this.phoneNumber = phoneNumber;
        if (estimatedDeliveryTime != null) this.estimatedDeliveryTime = estimatedDeliveryTime;
    }
}
