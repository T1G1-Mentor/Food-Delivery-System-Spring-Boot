package com.mentorship.food_delivery_app.order.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "order_tracking")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderTracking {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "order_tracking_id")
    private UUID id;

    @Column(name = "order_tracking_description", length = 50, nullable = false)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_tracking_status_id")
    private OrderStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_tracking_order_id", nullable = false)
    private Order order;

    @CreationTimestamp
    @Column(name = "order_tracking_created_at", updatable = false)
    private Instant createdAt;
}
