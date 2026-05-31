package com.mentorship.food_delivery_app.order.service.implementation;

import com.mentorship.food_delivery_app.order.dto.response.OrderTrackingDto;
import com.mentorship.food_delivery_app.order.repository.OrderTrackingRepository;
import com.mentorship.food_delivery_app.order.service.contract.OrderTrackingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderTrackingServiceImp implements OrderTrackingService {
    private final OrderTrackingRepository orderTrackingRepository;

    @Override
    public List<OrderTrackingDto> getTrackingHistory(UUID customerId, UUID orderId) {
        return orderTrackingRepository.findAllByCustomerIdAndOrderId(customerId, orderId);
    }

    @Override
    public List<OrderTrackingDto> getTrackingHistoryByOrderId(UUID orderId) {
        return orderTrackingRepository.findAllByOrderId(orderId);
    }
}
