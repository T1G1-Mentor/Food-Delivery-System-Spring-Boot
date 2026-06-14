package com.mentorship.food_delivery_app.restaurant.service.implementation;

import com.mentorship.food_delivery_app.common.enums.ErrorMessage;
import com.mentorship.food_delivery_app.restaurant.dto.menuitem.request.MenuItemRequestDto;
import com.mentorship.food_delivery_app.restaurant.entity.Coupon;
import com.mentorship.food_delivery_app.restaurant.entity.MenuItem;
import com.mentorship.food_delivery_app.restaurant.entity.RestaurantBranch;
import com.mentorship.food_delivery_app.restaurant.exceptions.CouponNotFoundException;
import com.mentorship.food_delivery_app.restaurant.exceptions.ItemNotFoundException;
import com.mentorship.food_delivery_app.restaurant.exceptions.RestaurantBranchNotFoundException;
import com.mentorship.food_delivery_app.restaurant.repository.CouponRepository;
import com.mentorship.food_delivery_app.restaurant.repository.MenuItemRepository;
import com.mentorship.food_delivery_app.restaurant.repository.RestaurantBranchRepository;
import com.mentorship.food_delivery_app.restaurant.service.contract.RestaurantMenuService;
import com.mentorship.food_delivery_app.restaurant.service.contract.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RestaurantServiceImp implements RestaurantService {
    private final MenuItemRepository menuItemRepository;
    private final CouponRepository couponRepository;
    private final RestaurantBranchRepository restaurantBranchRepository;
    private final RestaurantMenuService restaurantMenuService;

    @Override
    public MenuItem getMenuItemById(UUID menuItemId) {
        return menuItemRepository.findById(menuItemId)
                .orElseThrow(() -> new ItemNotFoundException(ErrorMessage.ITEM_NOT_FOUND.getMessage()));

    }

    @Override
    public Coupon getRestaurantCoupon(UUID couponId) {
        return couponRepository.findByCouponId(couponId)
                .orElseThrow(() -> new CouponNotFoundException(ErrorMessage.COUPON_NOT_FOUND.getMessage()));
    }

    @Override
    public List<MenuItem> getMenuItemsByIds(List<UUID> menuItemIds){
        return menuItemRepository.findMenuItemsByIds(menuItemIds);
    }

    @Override
    public RestaurantBranch getRestaurantBranchById(UUID restaurantBranchId) {

        return restaurantBranchRepository.findByRestaurantBranchId(restaurantBranchId)
                .orElseThrow(()->
                        new RestaurantBranchNotFoundException(ErrorMessage.RESTAURANT_BRANCH_NOT_FOUND.getMessage()));
    }

}
