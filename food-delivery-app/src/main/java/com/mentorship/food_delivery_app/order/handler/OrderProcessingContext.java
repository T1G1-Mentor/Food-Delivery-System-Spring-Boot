package com.mentorship.food_delivery_app.order.handler;

import com.mentorship.food_delivery_app.cart.entity.Cart;
import com.mentorship.food_delivery_app.cart.entity.CartItem;
import com.mentorship.food_delivery_app.customer.entity.Customer;
import com.mentorship.food_delivery_app.order.dto.request.PlaceOrderRequestDto;
import com.mentorship.food_delivery_app.order.dto.response.OrderResponseDto;
import com.mentorship.food_delivery_app.restaurant.entity.Coupon;
import com.mentorship.food_delivery_app.restaurant.entity.MenuItem;
import com.mentorship.food_delivery_app.restaurant.entity.RestaurantBranch;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.function.Supplier;

@RequiredArgsConstructor
public class OrderProcessingContext {
    private Cart cart;
    private Set<CartItem> cartItems;
    private List<MenuItem> menuItems;
    private Customer customer;
    private Coupon coupon;
    @Getter
    @Setter
    private OrderResponseDto responseDto;
    @Getter
    private final PlaceOrderRequestDto requestDto;
    private final Supplier<Cart> cartSupplier;
    private final Supplier<Set<CartItem>> cartItemsSupplier;
    private final Supplier<Customer> customerSupplier;
    private final Supplier<Coupon> couponSupplier;


    public List<MenuItem> getMenuItems() {
        if (menuItems == null) {
            cartItems = this.getCartItems();
            menuItems = cartItems.stream().map(CartItem::getMenuItem).toList();
        }

        return menuItems;
    }

    public Customer getCustomer() {
        if (customer == null)
            customer = customerSupplier.get();
        return customer;
    }


    public RestaurantBranch getRestaurantBranch() {
        if (cart == null)
            cart = cartSupplier.get();
        return cart.getCurrentRestaurant();
    }

    public Cart getCart() {
        if (cart == null)
            cart = cartSupplier.get();
        return cart;
    }

    public Set<CartItem> getCartItems() {
        if (cartItems == null)
            cartItems = cartItemsSupplier.get();

        return cartItems;
    }


    public UUID getRequestRestaurantBranchId() {
        return requestDto.restaurantBranchId();
    }

    public Coupon getCoupon() {
        if (requestDto.couponId() == null || coupon != null)
            return coupon;

        coupon = couponSupplier.get();
        return coupon;

    }


    public String getCustomerEmail() {
        return this.getCustomer().getUser().getEmail();
    }
}
