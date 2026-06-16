package com.mentorship.food_delivery_app.restaurant.service.implementation;

import com.mentorship.food_delivery_app.common.enums.ErrorMessage;
import com.mentorship.food_delivery_app.restaurant.dto.menuitem.request.MenuItemRequestDto;
import com.mentorship.food_delivery_app.restaurant.dto.menuitem.request.UpdateMenuItemRequestDto;
import com.mentorship.food_delivery_app.restaurant.dto.menuitem.response.MenuItemDto;
import com.mentorship.food_delivery_app.restaurant.dto.restaurantmenu.request.CreateMenuDto;
import com.mentorship.food_delivery_app.restaurant.dto.restaurantmenu.request.UpdateMenuDto;
import com.mentorship.food_delivery_app.restaurant.entity.Coupon;
import com.mentorship.food_delivery_app.restaurant.entity.MenuItem;
import com.mentorship.food_delivery_app.restaurant.entity.RestaurantBranch;
import com.mentorship.food_delivery_app.restaurant.exceptions.CouponNotFoundException;
import com.mentorship.food_delivery_app.restaurant.exceptions.DisabledRestaurantBranchException;
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
    public List<MenuItem> getMenuItemsByIds(List<UUID> menuItemIds) {
        return menuItemRepository.findMenuItemsByIds(menuItemIds);
    }

    @Override
    public RestaurantBranch getRestaurantBranchById(UUID restaurantBranchId) {

        return restaurantBranchRepository.findByRestaurantBranchId(restaurantBranchId)
                .orElseThrow(() ->
                        new RestaurantBranchNotFoundException(ErrorMessage.RESTAURANT_BRANCH_NOT_FOUND.getMessage()));
    }

    @Transactional
    @Override
    public void createMenuItem(MenuItemRequestDto menuItemRequestDto, UUID restaurantMenuId, UUID branchId) {
        validateRestaurant(branchId);
        restaurantMenuService.createMenuItem(menuItemRequestDto,
                restaurantMenuId,
                branchId);
    }

    @Transactional
    @Override
    public void updateMenuItem(UpdateMenuItemRequestDto menuItemRequestDto, UUID restaurantMenuId, UUID branchId) {
        validateRestaurant(branchId);
        restaurantMenuService.updateMenuItem(menuItemRequestDto,
                restaurantMenuId,
                branchId);
    }

    @Transactional
    @Override
    public void deleteMenuItem(UUID menuItemId, UUID restaurantMenuId, UUID branchId) {
        validateRestaurant(branchId);
        restaurantMenuService.deleteMenuItem(menuItemId,
                restaurantMenuId,
                branchId);
    }

    @Transactional(readOnly = true)
    @Override
    public List<MenuItemDto> getAllMenuItemsByMenuId(UUID restaurantMenuId, UUID branchId) {
        validateRestaurant(branchId);

        return restaurantMenuService.getAllMenuItemsByMenuId(restaurantMenuId,
                branchId);
    }

    @Transactional
    @Override
    public void createRestaurantMenu(CreateMenuDto createMenuDto, UUID branchId){
        RestaurantBranch branch = getAndValidateRestaurantBranch(branchId);

        restaurantMenuService.createRestaurantMenu(createMenuDto,
                branch);
    }

    @Transactional
    @Override
    public void updateRestaurantMenu(UpdateMenuDto updateMenuDto, UUID menuId, UUID branchId) {
        validateRestaurant(branchId);

        restaurantMenuService.updateRestaurantMenu(updateMenuDto,
                menuId,
                branchId);
    }

    private void validateRestaurant(UUID branchId) {
        Boolean isEnabled = restaurantBranchRepository.isEnabledById(branchId);

        if (isEnabled == null)
            throw new RestaurantBranchNotFoundException(ErrorMessage.RESTAURANT_BRANCH_NOT_FOUND.getMessage());
        if (!isEnabled)
            throw new DisabledRestaurantBranchException(ErrorMessage.RESTAURANT_BRANCH_DISABLED.getMessage());
    }

    private RestaurantBranch getAndValidateRestaurantBranch(UUID branchId){
        RestaurantBranch branch = getRestaurantBranchById(branchId);

        if (!branch.isEnabled())
            throw new DisabledRestaurantBranchException(ErrorMessage.RESTAURANT_BRANCH_DISABLED.getMessage());

        return branch;
    }
}
