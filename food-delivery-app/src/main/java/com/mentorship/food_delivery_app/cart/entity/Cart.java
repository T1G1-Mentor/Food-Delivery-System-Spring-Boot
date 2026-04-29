package com.mentorship.food_delivery_app.cart.entity;

import com.mentorship.food_delivery_app.customer.entity.Customer;
import com.mentorship.food_delivery_app.restaurant.entity.RestaurantBranch;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcType;
import org.hibernate.boot.model.source.spi.JdbcDataType;

import java.math.BigDecimal;
import java.sql.JDBCType;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "cart")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "cart_id")
    private UUID id;

    @Column(name = "is_locked" )
    private boolean isLocked;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_customer_id", nullable = false,updatable = false)
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_current_rest_id")
    private RestaurantBranch currentRestaurant;

    @OneToMany(mappedBy = "cart", cascade = {CascadeType.PERSIST,CascadeType.MERGE,CascadeType.REFRESH}, orphanRemoval = true)
    private Set<CartItem> cartItems;

    public BigDecimal calculateTotal() {
        return this.getCartItems()
                .stream()
                .map(CartItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
