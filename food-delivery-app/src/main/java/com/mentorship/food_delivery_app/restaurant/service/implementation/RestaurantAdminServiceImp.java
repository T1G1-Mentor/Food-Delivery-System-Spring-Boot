package com.mentorship.food_delivery_app.restaurant.service.implementation;

import com.mentorship.food_delivery_app.common.enums.ErrorMessage;
import com.mentorship.food_delivery_app.restaurant.dto.restaurant.request.CreateRestaurantDto;
import com.mentorship.food_delivery_app.restaurant.dto.restaurant.request.UpdateRestaurantDto;
import com.mentorship.food_delivery_app.restaurant.dto.restaurant.response.RestaurantDto;
import com.mentorship.food_delivery_app.restaurant.dto.restaurant.response.TopRestaurantDto;
import com.mentorship.food_delivery_app.restaurant.dto.restaurantbranch.request.CreateBranchDto;
import com.mentorship.food_delivery_app.restaurant.dto.restaurantbranch.request.UpdateBranchDto;
import com.mentorship.food_delivery_app.restaurant.dto.restaurantbranch.response.RestaurantBranchDto;
import com.mentorship.food_delivery_app.restaurant.entity.Category;
import com.mentorship.food_delivery_app.restaurant.entity.Restaurant;
import com.mentorship.food_delivery_app.restaurant.entity.RestaurantBranch;
import com.mentorship.food_delivery_app.restaurant.exceptions.RestaurantBranchNotFoundException;
import com.mentorship.food_delivery_app.restaurant.exceptions.RestaurantNotFoundException;
import com.mentorship.food_delivery_app.restaurant.repository.CategoryRepository;
import com.mentorship.food_delivery_app.restaurant.repository.RestaurantBranchRepository;
import com.mentorship.food_delivery_app.restaurant.repository.RestaurantRepository;
import com.mentorship.food_delivery_app.restaurant.service.contract.RestaurantAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.domain.PageRequest;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RestaurantAdminServiceImp implements RestaurantAdminService {

    private final RestaurantRepository restaurantRepository;
    private final RestaurantBranchRepository restaurantBranchRepository;
    private final CategoryRepository categoryRepository;

    @Transactional
    @Override
    public void createRestaurant(CreateRestaurantDto dto) {
        Set<Category> categories = resolveCategories(dto.categoryIds());
        Restaurant restaurant = Restaurant.builder()
                .name(dto.name())
                .description(dto.description())
                .categories(categories)
                .build();
        restaurantRepository.save(restaurant);
    }

    @Transactional
    @Override
    public void updateRestaurant(UUID restaurantId, UpdateRestaurantDto dto) {
        Restaurant restaurant = getRestaurant(restaurantId);
        restaurant.applyModifications(dto.name(), dto.description());
        if (dto.categoryIds() != null)
            restaurant.setCategories(resolveCategories(dto.categoryIds()));
    }

    @Transactional
    @Override
    public void deleteRestaurant(UUID restaurantId) {
        Restaurant restaurant = getRestaurant(restaurantId);
        restaurantRepository.delete(restaurant);
    }

    @Transactional(readOnly = true)
    @Override
    public List<RestaurantDto> searchRestaurants(String name, String categoryName) {
        return restaurantRepository.searchRestaurants(name, categoryName)
                .stream()
                .map(RestaurantDto::from)
                .toList();
    }

    @Transactional
    @Override
    public void createBranch(UUID restaurantId, CreateBranchDto dto) {
        Restaurant restaurant = getRestaurant(restaurantId);
        RestaurantBranch branch = RestaurantBranch.builder()
                .restaurant(restaurant)
                .city(dto.city())
                .deliveryFee(dto.deliveryFee())
                .minOrder(dto.minOrder())
                .openTime(dto.openTime())
                .closeTime(dto.closeTime())
                .phoneNumber(dto.phoneNumber())
                .estimatedDeliveryTime(dto.estimatedDeliveryTime())
                .isEnabled(true)
                .build();
        restaurantBranchRepository.save(branch);
    }

    @Transactional
    @Override
    public void updateBranch(UUID restaurantId, UUID branchId, UpdateBranchDto dto) {
        RestaurantBranch branch = getBranchByIdAndRestaurantId(branchId, restaurantId);
        branch.applyModifications(
                dto.deliveryFee(), dto.minOrder(), dto.city(),
                dto.openTime(), dto.closeTime(), dto.phoneNumber(),
                dto.estimatedDeliveryTime()
        );
    }

    @Transactional
    @Override
    public void deleteBranch(UUID restaurantId, UUID branchId) {
        RestaurantBranch branch = getBranchByIdAndRestaurantId(branchId, restaurantId);
        restaurantBranchRepository.delete(branch);
    }

    @Transactional(readOnly = true)
    @Override
    public List<RestaurantBranchDto> getBranchesByRestaurantId(UUID restaurantId) {
        validateRestaurantExists(restaurantId);
        return restaurantBranchRepository.findAllByRestaurantId(restaurantId)
                .stream()
                .map(RestaurantBranchDto::from)
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public List<TopRestaurantDto> getTopRestaurants() {
        return restaurantRepository.findTopByAverageRating(PageRequest.of(0, 10));
    }

    private Restaurant getRestaurant(UUID restaurantId) {
        return restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RestaurantNotFoundException(ErrorMessage.RESTAURANT_NOT_FOUND.getMessage()));
    }

    private void validateRestaurantExists(UUID restaurantId) {
        if (!restaurantRepository.existsById(restaurantId))
            throw new RestaurantNotFoundException(ErrorMessage.RESTAURANT_NOT_FOUND.getMessage());
    }

    private RestaurantBranch getBranchByIdAndRestaurantId(UUID branchId, UUID restaurantId) {
        return restaurantBranchRepository.findByBranchIdAndRestaurantId(branchId, restaurantId)
                .orElseThrow(() -> new RestaurantBranchNotFoundException(ErrorMessage.RESTAURANT_BRANCH_NOT_FOUND.getMessage()));
    }

    private Set<Category> resolveCategories(Set<Integer> categoryIds) {
        if (categoryIds == null || categoryIds.isEmpty())
            return new HashSet<>();
        return new HashSet<>(categoryRepository.findAllById(categoryIds));
    }
}
