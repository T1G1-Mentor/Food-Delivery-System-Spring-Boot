package com.mentorship.food_delivery_app.order.entity;

import com.mentorship.food_delivery_app.customer.entity.Customer;
import com.mentorship.food_delivery_app.customer.entity.CustomerAddress;
import com.mentorship.food_delivery_app.restaurant.entity.Coupon;
import com.mentorship.food_delivery_app.restaurant.entity.RestaurantBranch;
import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "order_id")
    private UUID id;

    @PositiveOrZero(message = "Order discount cannot be negative")
    @Column(name = "order_discount_value", precision = 6, scale = 2)
    private BigDecimal discountValue = BigDecimal.ZERO;

    @Positive(message = "Subtotal must be greater than zero")
    @Column(name = "order_subtotal", precision = 7, scale = 2)
    private BigDecimal subtotal;

    @PositiveOrZero(message = "Fee cannot be negative")
    @Column(name = "order_fee", precision = 6, scale = 2)
    private BigDecimal fee = BigDecimal.ZERO;

    @PositiveOrZero(message = "Total cannot be negative")
    @Column(name = "order_total", precision = 10, scale = 2, nullable = false)
    private BigDecimal total;

    @CreationTimestamp
    @Column(name = "order_date", updatable = false)
    private Instant orderDate;

    @Column(name = "order_note")
    private String note;


    @OneToMany(mappedBy = "order", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private Set<OrderItem> items;

    @OneToMany(mappedBy = "order",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private Set<OrderTracking> trackingHistory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_address_id", nullable = false)
    private CustomerAddress address;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_customer_id", nullable = false)
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_restaurant_branch_id", nullable = false)
    private RestaurantBranch branch;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_coupon_id")
    private Coupon coupon;

    public void addOrderItem(OrderItem item) {
        items.add(item);
        item.setOrder(this);
    }

    public void addTrackingEvent(OrderTracking tracking) {
        trackingHistory.add(tracking);
        tracking.setOrder(this);
    }
}
