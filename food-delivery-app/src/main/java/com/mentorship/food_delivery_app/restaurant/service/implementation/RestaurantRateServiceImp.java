package com.mentorship.food_delivery_app.restaurant.service.implementation;

import com.mentorship.food_delivery_app.common.enums.ErrorMessage;
import com.mentorship.food_delivery_app.customer.entity.Customer;
import com.mentorship.food_delivery_app.customer.service.contract.CustomerService;
import com.mentorship.food_delivery_app.order.enums.OrderStatus;
import com.mentorship.food_delivery_app.order.repository.OrderRepository;
import com.mentorship.food_delivery_app.restaurant.dto.request.RestaurantRatePatchRequestDto;
import com.mentorship.food_delivery_app.restaurant.dto.request.RestaurantRateRequestDto;
import com.mentorship.food_delivery_app.restaurant.dto.response.RestaurantRateResponseDto;
import com.mentorship.food_delivery_app.restaurant.entity.Restaurant;
import com.mentorship.food_delivery_app.restaurant.entity.RestaurantRate;
import com.mentorship.food_delivery_app.restaurant.exceptions.CustomerAlreadyRatedException;
import com.mentorship.food_delivery_app.restaurant.exceptions.CustomerHasNotOrderedException;
import com.mentorship.food_delivery_app.restaurant.exceptions.RestaurantNotFoundException;
import com.mentorship.food_delivery_app.restaurant.exceptions.RestaurantRateNotFoundException;
import com.mentorship.food_delivery_app.restaurant.repository.RestaurantRateRepository;
import com.mentorship.food_delivery_app.restaurant.repository.RestaurantRepository;
import com.mentorship.food_delivery_app.restaurant.service.contract.RestaurantRateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class RestaurantRateServiceImp implements RestaurantRateService {

    private final RestaurantRateRepository restaurantRateRepository;
    private final RestaurantRepository restaurantRepository;
    private final OrderRepository orderRepository;
    private final CustomerService customerService;

    @Override
    public List<RestaurantRateResponseDto> getRestaurantRatings(UUID restaurantId) {
        validateRestaurantExists(restaurantId);
        return restaurantRateRepository.findAllByRestaurantId(restaurantId)
                .stream()
                .map(RestaurantRateResponseDto::from)
                .toList();
    }

    @Transactional
    @Override
    public RestaurantRateResponseDto createRating(UUID restaurantId, RestaurantRateRequestDto request) {
        validateRatingStep(request.rating());

        Restaurant restaurant = getRestaurantById(restaurantId);
        Customer customer = customerService.getLoggedinCustomer();

        boolean hasDeliveredOrder = orderRepository.existsByCustomerIdAndBranchRestaurantIdAndStatus(
                customer.getId(), restaurantId, OrderStatus.DELIVERED);
        if (!hasDeliveredOrder) {
            throw new CustomerHasNotOrderedException(ErrorMessage.CUSTOMER_HAS_NOT_ORDERED.getMessage());
        }

        if (restaurantRateRepository.existsByRestaurantIdAndCustomerId(restaurantId, customer.getId())) {
            throw new CustomerAlreadyRatedException(ErrorMessage.CUSTOMER_ALREADY_RATED.getMessage());
        }

        RestaurantRate rate = RestaurantRate.builder()
                .restaurant(restaurant)
                .customer(customer)
                .title(request.title())
                .rating(request.rating())
                .comment(request.comment())
                .build();

        RestaurantRate saved = restaurantRateRepository.save(rate);
        log.info("Customer {} rated restaurant {} with rating {}", customer.getId(), restaurantId, request.rating());
        return RestaurantRateResponseDto.from(saved);
    }

    @Transactional
    @Override
    public RestaurantRateResponseDto updateRating(UUID restaurantId, UUID rateId, RestaurantRatePatchRequestDto request) {
        if (request.rating() != null) {
            validateRatingStep(request.rating());
        }

        validateRestaurantExists(restaurantId);
        Customer customer = customerService.getLoggedinCustomer();

        RestaurantRate rate = restaurantRateRepository.findByIdAndCustomerId(rateId, customer.getId())
                .orElseThrow(() -> new RestaurantRateNotFoundException(ErrorMessage.RESTAURANT_RATE_NOT_FOUND.getMessage()));

        if (request.title() != null) {
            rate.setTitle(request.title());
        }
        if (request.rating() != null) {
            rate.setRating(request.rating());
        }
        if (request.comment() != null) {
            rate.setComment(request.comment());
        }

        log.info("Customer {} updated rating {} for restaurant {}", customer.getId(), rateId, restaurantId);
        return RestaurantRateResponseDto.from(rate);
    }

    @Transactional
    @Override
    public void deleteRating(UUID restaurantId, UUID rateId) {
        validateRestaurantExists(restaurantId);
        Customer customer = customerService.getLoggedinCustomer();

        RestaurantRate rate = restaurantRateRepository.findByIdAndCustomerId(rateId, customer.getId())
                .orElseThrow(() -> new RestaurantRateNotFoundException(ErrorMessage.RESTAURANT_RATE_NOT_FOUND.getMessage()));

        restaurantRateRepository.delete(rate);
        log.info("Customer {} deleted rating {} for restaurant {}", customer.getId(), rateId, restaurantId);
    }

    private Restaurant getRestaurantById(UUID restaurantId) {
        return restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RestaurantNotFoundException(ErrorMessage.RESTAURANT_NOT_FOUND.getMessage()));
    }

    private void validateRestaurantExists(UUID restaurantId) {
        if (!restaurantRepository.existsById(restaurantId)) {
            throw new RestaurantNotFoundException(ErrorMessage.RESTAURANT_NOT_FOUND.getMessage());
        }
    }

    private void validateRatingStep(double rating) {
        // Rating must be a multiple of 0.5 (0.0, 0.5, 1.0, 1.5, ... 5.0)
        if (rating * 2 != Math.floor(rating * 2)) {
            throw new com.mentorship.food_delivery_app.common.exceptions.BadRequestException(
                    ErrorMessage.INVALID_RATING_VALUE.getMessage());
        }
    }
}
