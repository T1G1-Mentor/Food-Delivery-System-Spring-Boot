package com.mentorship.food_delivery_app.restaurant.service.contract;

import com.mentorship.food_delivery_app.restaurant.dto.restaurant.request.CreateRestaurantDto;
import com.mentorship.food_delivery_app.restaurant.dto.restaurant.request.UpdateRestaurantDto;
import com.mentorship.food_delivery_app.restaurant.dto.restaurantbranch.request.CreateBranchDto;
import com.mentorship.food_delivery_app.restaurant.dto.restaurantbranch.request.UpdateBranchDto;
import com.mentorship.food_delivery_app.restaurant.dto.restaurantbranch.response.RestaurantBranchDto;

import java.util.List;
import java.util.UUID;

public interface RestaurantAdminService {

    void createRestaurant(CreateRestaurantDto dto);

    void updateRestaurant(UUID restaurantId, UpdateRestaurantDto dto);

    void deleteRestaurant(UUID restaurantId);


    void createBranch(UUID restaurantId, CreateBranchDto dto);

    void updateBranch(UUID restaurantId, UUID branchId, UpdateBranchDto dto);

    void deleteBranch(UUID restaurantId, UUID branchId);

    List<RestaurantBranchDto> getBranchesByRestaurantId(UUID restaurantId);

}
