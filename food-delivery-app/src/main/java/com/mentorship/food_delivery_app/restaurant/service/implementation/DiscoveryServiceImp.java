package com.mentorship.food_delivery_app.restaurant.service.implementation;

import com.mentorship.food_delivery_app.restaurant.dto.menuitem.response.SearchMenuItemResponse;
import com.mentorship.food_delivery_app.restaurant.repository.MenuItemRepository;
import com.mentorship.food_delivery_app.restaurant.service.contract.DiscoveryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DiscoveryServiceImp implements DiscoveryService {
    private final MenuItemRepository menuItemRepository;


    @Transactional(readOnly = true)
    @Override
    public List<SearchMenuItemResponse> searchMenuItem(String query) {
        return menuItemRepository.searchMenuItem(query);
    }
}
