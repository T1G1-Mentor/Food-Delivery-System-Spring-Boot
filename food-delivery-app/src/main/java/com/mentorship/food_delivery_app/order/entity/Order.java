package com.mentorship.food_delivery_app.order.entity;

import com.mentorship.food_delivery_app.customer.entity.Customer;
import com.mentorship.food_delivery_app.customer.entity.CustomerAddress;
import com.mentorship.food_delivery_app.order.enums.OrderStatus;
import com.mentorship.food_delivery_app.restaurant.entity.Coupon;
import com.mentorship.food_delivery_app.restaurant.entity.RestaurantBranch;
import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;

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
@Builder
@DynamicUpdate
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "order_id")
    private UUID orderId;

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

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status", nullable = false)
    private OrderStatus status;

    @Column(name = "order_note")
    private String note;


    @OneToMany(mappedBy = "order", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private Set<OrderItem> items;

    @OneToMany(mappedBy = "order",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private Set<OrderTracking> trackingHistory;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "city",        column = @Column(name = "order_delivery_city",         length = 20)),
            @AttributeOverride(name = "street",      column = @Column(name = "order_delivery_street",       length = 20)),
            @AttributeOverride(name = "building",    column = @Column(name = "order_delivery_building",     length = 20)),
            @AttributeOverride(name = "apartment",   column = @Column(name = "order_delivery_apartment",    length = 20)),
            @AttributeOverride(name = "phoneNumber", column = @Column(name = "order_delivery_phone_number", length = 15)),
            @AttributeOverride(name = "note",        column = @Column(name = "order_delivery_note",         length = 500))
    })
    private DeliveryAddress deliveryAddress;

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
        if(this.items==null)
            this.items=new HashSet<>();

        this.items.add(item);
        item.setOrder(this);
    }

    public void addTrackingEvent(OrderTracking tracking) {
        if (this.trackingHistory==null)
            this.trackingHistory=new HashSet<>();

        this.trackingHistory.add(tracking);
        tracking.setOrder(this);
    }

    public String getDeliveryCity() {
        return this.deliveryAddress.getCity();
    }

    public String getDeliveryStreet() {
        return this.deliveryAddress.getStreet();
    }

    public String getDeliveryBuilding() {
        return this.deliveryAddress.getBuilding();
    }

    public String getDeliveryApartment() {
        return this.deliveryAddress.getApartment();
    }

    public BigDecimal getCouponAmount() {
        return this.coupon != null ? this.coupon.getAmount() : null;
    }

    public String getCustomerFullName() {
        return this.customer.getFullName();
    }

    public String getCustomerEmail(){
        return this.customer.getUser().getEmail();
    }

    public boolean isCancelled(){
        return this.status.equals(OrderStatus.CANCELLED);
    }

    public String getRestaurantBranchName(){
        return this.branch.getRestaurantName();
    }
}
