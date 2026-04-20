package com.mentorship.food_delivery_app.restaurant.entity;

import com.mentorship.food_delivery_app.common.audit.Auditable;
import com.mentorship.food_delivery_app.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Table(name = "restaurant_branch")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantBranch extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "branch_id")
    private UUID id;


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


    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id")
    private User admin;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_rest_id", nullable = false)
    private Restaurant restaurant;
}
