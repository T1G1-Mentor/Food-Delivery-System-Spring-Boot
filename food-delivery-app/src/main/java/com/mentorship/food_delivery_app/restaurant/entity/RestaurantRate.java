package com.mentorship.food_delivery_app.restaurant.entity;

import com.mentorship.food_delivery_app.customer.entity.Customer;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "restaurant_rate")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class RestaurantRate {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "restaurant_rate_id")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_rate_restaurant_id", nullable = false)
    private Restaurant restaurant;

    @ManyToOne()
    @JoinColumn(name = "restaurant_rate_customer_id", nullable = false)
    private Customer customerId;

    @Column(name = "restaurant_rate_rating")
    private Integer rating;

    @Column(name = "restaurant_rate_comment", nullable = false, length = 500)
    private String comment;

    @CreatedDate
    @Column(name = "restaurant_rate_created_at", insertable = false, updatable = false)
    private Instant createdAt;


}
