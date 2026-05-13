package com.mentorship.food_delivery_app.restaurant.service.implementation;

import com.mentorship.food_delivery_app.common.enums.ErrorMessage;
import com.mentorship.food_delivery_app.common.exceptions.ResourceNotFoundException;
import com.mentorship.food_delivery_app.restaurant.entity.Coupon;
import com.mentorship.food_delivery_app.restaurant.entity.MenuItem;
import com.mentorship.food_delivery_app.restaurant.entity.RestaurantBranch;
import com.mentorship.food_delivery_app.restaurant.repository.CouponRepository;
import com.mentorship.food_delivery_app.restaurant.repository.MenuItemRepository;
import com.mentorship.food_delivery_app.restaurant.service.contract.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RestaurantServiceImp implements RestaurantService {
    private final MenuItemRepository menuItemRepository;
    private final CouponRepository couponRepository;

    @Override
    public MenuItem getMenuItemById(UUID menuItemId) {
        return menuItemRepository.findById(menuItemId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessage.ITEM_NOT_FOUND.getMessage()));
    }

    @Override
    public Coupon getRestaurantCoupon(UUID couponId, RestaurantBranch branch) {
        return couponRepository.findByIdAndRestaurant(couponId, branch.getRestaurant())
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessage.COUPON_NOT_FOUND.getMessage()));
    }
}
