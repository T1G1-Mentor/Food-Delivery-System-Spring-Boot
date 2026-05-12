package com.mentorship.food_delivery_app.restaurant.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "coupon")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Coupon {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "coupon_id")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_restaurant_id", nullable = false)
    private Restaurant restaurant;

    @Column(name = "coupon_amount", precision = 6, scale = 2)
    private BigDecimal amount;

    @Column(name = "coupon_available_from", nullable = false)
    private Instant availableFrom;

    @Column(name = "coupon_available_to", nullable = false)
    private Instant availableTo;

    @Column(name = "coupon_is_active", nullable = false, columnDefinition = "BIT(1)")
    private boolean isActive;

    @CreatedDate
    @Column(name = "coupon_created_at", insertable = false, updatable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(name = "coupon_last_modified")
    private Instant lastModified;

    public boolean isValid() {
        Instant now = Instant.now();
        return this.isActive() && !now.isBefore(this.getAvailableFrom()) && !now.isAfter(this.getAvailableTo());
    }
}
