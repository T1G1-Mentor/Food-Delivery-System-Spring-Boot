package com.mentorship.food_delivery_app.order.entity;

import com.mentorship.food_delivery_app.restaurant.entity.MenuItem;
import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "order_item")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "order_item_id")
    private UUID id;

    @Positive(message = "Unit price must be greater than zero")
    @Column(name = "order_item_unit_price", precision = 9, scale = 2, nullable = false)
    private BigDecimal unitPrice;

    @Positive(message = "Quantity must be greater than zero")
    @Column(name = "order_item_quantity", nullable = false)
    private Integer quantity;

    @Positive(message = "Subtotal must be greater than zero")
    @Column(name = "order_item_subtotal", precision = 10, scale = 2)
    private BigDecimal subtotal;

    @Column(name = "order_item_note")
    private String note;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_item_order_id", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_item_menu_item_id", nullable = false)
    private MenuItem menuItem;
}
