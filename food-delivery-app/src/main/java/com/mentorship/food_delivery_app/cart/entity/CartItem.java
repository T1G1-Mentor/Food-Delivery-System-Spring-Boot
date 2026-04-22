package com.mentorship.food_delivery_app.cart.entity;

import com.mentorship.food_delivery_app.restaurant.entity.MenuItem;
import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "cart_item")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_item_id")
    private Long id;

    @Positive(message = "Quantity must be greater than zero")
    @Column(name = "cart_item_quantity", nullable = false)
    private Integer quantity;

    @Column(name = "cart_item_note")
    private String note;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_item_cart_id")
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_item_id")
    private MenuItem menuItem;

    public BigDecimal getTotalPrice() {
        return this.menuItem.getPrice().multiply(BigDecimal.valueOf(this.quantity));
    }
}
