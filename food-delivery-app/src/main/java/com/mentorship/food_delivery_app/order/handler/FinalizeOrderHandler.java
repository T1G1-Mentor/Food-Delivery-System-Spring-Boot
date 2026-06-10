package com.mentorship.food_delivery_app.order.handler;

import com.mentorship.food_delivery_app.cart.entity.Cart;
import com.mentorship.food_delivery_app.cart.entity.CartItem;
import com.mentorship.food_delivery_app.common.enums.ErrorMessage;
import com.mentorship.food_delivery_app.common.service.contract.EmailService;
import com.mentorship.food_delivery_app.customer.entity.Customer;
import com.mentorship.food_delivery_app.customer.exceptions.AddressNotFoundException;
import com.mentorship.food_delivery_app.order.dto.OrderPricing;
import com.mentorship.food_delivery_app.order.dto.request.DeliveryAddressDto;
import com.mentorship.food_delivery_app.order.dto.response.OrderResponseDto;
import com.mentorship.food_delivery_app.order.entity.DeliveryAddress;
import com.mentorship.food_delivery_app.order.entity.Order;
import com.mentorship.food_delivery_app.order.entity.OrderItem;
import com.mentorship.food_delivery_app.order.entity.OrderTracking;
import com.mentorship.food_delivery_app.order.enums.OrderStatus;
import com.mentorship.food_delivery_app.order.mapper.OrderMapper;
import com.mentorship.food_delivery_app.order.repository.OrderRepository;
import com.mentorship.food_delivery_app.restaurant.entity.Coupon;
import com.mentorship.food_delivery_app.restaurant.entity.RestaurantBranch;
import lombok.RequiredArgsConstructor;

import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class FinalizeOrderHandler extends OrderHandler {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;


    @Override
    public OrderResponseDto handle(OrderProcessingContext context) {

        Order savedOrder = this.createAndPersistOrder(context);
        context.setResponseDto(orderMapper.toResponse(savedOrder));

        return this.handleNext(context);
    }


    public Order createAndPersistOrder(OrderProcessingContext context) {
        DeliveryAddressDto deliveryAddressDto = context.getRequestDto().deliveryAddress();
        Customer localCustomer = context.getCustomer();
        Cart localCart = context.getCart();
        RestaurantBranch restaurantBranch = context.getRestaurantBranch();
        Coupon coupon = context.getCoupon();

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
                .note(context.getRequestDto().orderNotes())
                .build();

        order.setItems(buildOrderItems(context.getCartItems(), order));

        Order savedOrder = orderRepository.save(order);

        OrderTracking.createNewOrderTracking(OrderStatus.PENDING, OrderStatus.PENDING.getDescription(), savedOrder);
        return savedOrder;
    }

    private DeliveryAddress resolveDeliveryAddress(Customer customer, DeliveryAddressDto deliveryAddressDto) {
        if (deliveryAddressDto == null) {
            if (customer.getDefaultAddress() == null)
                throw new AddressNotFoundException(ErrorMessage.NO_DEFAULT_ADDRESS_EXISTS.getMessage());
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
