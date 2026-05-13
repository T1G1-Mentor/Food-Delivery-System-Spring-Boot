package com.mentorship.food_delivery_app.cart.entity;

import com.mentorship.food_delivery_app.cart.exceptions.CartLockedException;
import com.mentorship.food_delivery_app.customer.entity.Customer;
import com.mentorship.food_delivery_app.restaurant.entity.RestaurantBranch;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

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

    @Column(name = "is_locked")
    private boolean isLocked;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_customer_id", nullable = false, updatable = false)
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_current_rest_id")
    private RestaurantBranch currentRestaurant;

    @OneToMany(mappedBy = "cart", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH}, orphanRemoval = true)
    private Set<CartItem> cartItems;

    public BigDecimal calculateTotal() {
        return this.getCartItems()
                .stream()
                .map(CartItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public Optional<CartItem> searchExistingItem(UUID menuItemId) {
        return this.
                cartItems
                .stream()
                .filter(item -> item.getMenuItem().getId().equals(menuItemId))
                .findFirst();
    }

    public void removeCartItem(CartItem item) {
        this.cartItems.remove(item);
    }

    public Set<CartItem> getUnavailableItems() {
        return this.cartItems
                .stream()
                .filter(item -> !item.isAvailable())
                .collect(Collectors.toSet());
    }

    public void lock() {
        if (this.isLocked) throw new CartLockedException();
        this.isLocked = true;
    }

    public void unlock() {
        this.isLocked = false;
    }
}
