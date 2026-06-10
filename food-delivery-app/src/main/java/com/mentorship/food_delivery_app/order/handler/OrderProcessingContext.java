package com.mentorship.food_delivery_app.order.handler;

import com.mentorship.food_delivery_app.cart.entity.Cart;
import com.mentorship.food_delivery_app.cart.entity.CartItem;
import com.mentorship.food_delivery_app.customer.entity.Customer;
import com.mentorship.food_delivery_app.order.dto.OrderPricing;
import com.mentorship.food_delivery_app.order.dto.request.DeliveryAddressDto;
import com.mentorship.food_delivery_app.order.dto.request.PlaceOrderRequestDto;
import com.mentorship.food_delivery_app.order.dto.response.OrderResponseDto;
import com.mentorship.food_delivery_app.order.entity.DeliveryAddress;
import com.mentorship.food_delivery_app.order.entity.Order;
import com.mentorship.food_delivery_app.order.entity.OrderItem;
import com.mentorship.food_delivery_app.order.entity.OrderTracking;
import com.mentorship.food_delivery_app.order.enums.OrderStatus;
import com.mentorship.food_delivery_app.order.mapper.OrderMapper;
import com.mentorship.food_delivery_app.order.repository.OrderRepository;
import com.mentorship.food_delivery_app.restaurant.entity.Coupon;
import com.mentorship.food_delivery_app.restaurant.entity.MenuItem;
import com.mentorship.food_delivery_app.restaurant.entity.RestaurantBranch;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class OrderProcessingContext {
    private Cart cart;
    private Set<CartItem> cartItems;
    private List<MenuItem> menuItems;
    private Customer customer;
    private Coupon coupon;
    private Order savedOrder;
    private final Supplier<Cart> cartSupplier;
    private final Supplier<Set<CartItem>> cartItemsSupplier;
    private final Runnable cartLocker;
    private final Supplier<Customer> customerSupplier;
    private final Supplier<Coupon> couponSupplier;
    private final OrderRepository orderRepository;
    @Getter
    private final PlaceOrderRequestDto requestDto;
    private final OrderMapper orderMapper;


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

    public void lockCart() {
        cartLocker.run();
    }

    public OrderResponseDto getOrderResponse() {
        return orderMapper.toResponse(savedOrder);
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

    public Order createAndPersistOrder() {
        DeliveryAddressDto deliveryAddressDto = this.requestDto.deliveryAddress();
        Customer localCustomer = this.getCustomer();
        Cart localCart = this.getCart();
        RestaurantBranch restaurantBranch = this.getRestaurantBranch();
        coupon = this.getCoupon();

        DeliveryAddress deliveryAddressToPersist = resolveDeliveryAddress(localCustomer, deliveryAddressDto);

        OrderPricing pricing = OrderPricing.calculate(localCart, coupon);
        Order order = Order.builder()
                .customer(localCustomer)
                .deliveryAddress(deliveryAddressToPersist)
                .branch(restaurantBranch)
                .subtotal(pricing.subtotal())
                .total(pricing.total())
                .fee(pricing.deliveryFee())
                .discountValue(pricing.discount())
                .status(OrderStatus.PENDING)
                .note(requestDto.orderNotes())
                .build();

        savedOrder = orderRepository.save(order);
        savedOrder.setItems(buildOrderItems(cartItems, savedOrder));

        OrderTracking.createNewOrderTracking(OrderStatus.PENDING, OrderStatus.PENDING.getDescription(), savedOrder);
        return savedOrder;
    }

    private DeliveryAddress resolveDeliveryAddress(Customer customer, DeliveryAddressDto deliveryAddressDto) {
        if (deliveryAddressDto == null) {
            return DeliveryAddress.from(customer.getDefaultAddress());
        }
        return DeliveryAddress.from(deliveryAddressDto);
    }

    private Set<OrderItem> buildOrderItems(Set<CartItem> cartItems, Order order) {
        return cartItems.stream()
                .map(cartItem -> OrderItem.builder()
                        .menuItem(cartItem.getMenuItem())
                        .quantity(cartItem.getQuantity())
                        .unitPrice(cartItem.getMenuItem().getPrice())
                        .subtotal(cartItem.getTotalPrice())
                        .order(order)
                        .build()
                )
                .collect(Collectors.toSet());
    }
}
