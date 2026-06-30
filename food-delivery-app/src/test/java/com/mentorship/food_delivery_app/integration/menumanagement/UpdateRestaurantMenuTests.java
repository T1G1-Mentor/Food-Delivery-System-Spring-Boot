package com.mentorship.food_delivery_app.integration.menumanagement;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mentorship.food_delivery_app.common.enums.ErrorMessage;
import com.mentorship.food_delivery_app.integration.BaseIntegrationTest;
import com.mentorship.food_delivery_app.integration.security.WithMockPrincipal;
import com.mentorship.food_delivery_app.restaurant.dto.restaurantmenu.request.UpdateMenuDto;
import com.mentorship.food_delivery_app.restaurant.entity.Restaurant;
import com.mentorship.food_delivery_app.restaurant.entity.RestaurantBranch;
import com.mentorship.food_delivery_app.restaurant.entity.RestaurantMenu;
import com.mentorship.food_delivery_app.restaurant.repository.RestaurantBranchRepository;
import com.mentorship.food_delivery_app.restaurant.repository.RestaurantMenuRepository;
import com.mentorship.food_delivery_app.restaurant.repository.RestaurantRepository;
import com.mentorship.food_delivery_app.user.entity.enums.RoleName;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UpdateRestaurantMenuTests extends BaseIntegrationTest {

    private final String url = "/api/v1/admin/restaurants/branchs/{branchId}/restaurant-menus/{menuId}";
    private RestaurantBranch testBranch;
    private RestaurantMenu testMenu;


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private RestaurantBranchRepository restaurantBranchRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RestaurantMenuRepository restaurantMenuRepository;

    @BeforeEach
    void setUp() {
        Restaurant restaurant = com.mentorship.food_delivery_app.restaurant.entity.Restaurant.builder()
                .name("Buffalo Burger")
                .description("The best burger in the city")
                .build();

        Restaurant testRestaurant = restaurantRepository.save(restaurant);

        RestaurantBranch branch = RestaurantBranch.builder()
                .deliveryFee(BigDecimal.valueOf(10))
                .minOrder(BigDecimal.valueOf(1))
                .city("Cairo")
                .openTime(LocalTime.of(9, 0))
                .closeTime(LocalTime.of(22, 0))
                .phoneNumber("232323")
                .estimatedDeliveryTime(20)
                .isEnabled(true)
                .restaurant(testRestaurant)
                .build();

        branch.setCreatedBy(UUID.randomUUID());
        branch.setCreatedAt(Instant.now());

        this.testBranch = restaurantBranchRepository.save(branch);
        RestaurantMenu menu = RestaurantMenu.createMenu("Meet");
        menu.setCreatedBy(UUID.randomUUID());
        menu.setCreatedAt(Instant.now());
        menu.setRestaurantBranch(testBranch);
        testMenu = restaurantMenuRepository.save(menu);
    }

    @Test
    @WithMockPrincipal(roles = RoleName.ROLE_ADMIN)
    @DisplayName("""
            GIVEN: an authenticated Admin
            AND: an enabled restaurant branch with an existing menu
            AND: a valid update menu payload
            WHEN: a PUT request is made to the branch menu update endpoint
            THEN: the response status should be 204 No Content
            AND: the menu details should be updated in the database
            """)
    void updateMenu_Success() throws Exception {
        UpdateMenuDto payload = createUpdateMenuDto("Beef");

        mockMvc.perform(put(url, testBranch.getRestaurantBranchId(), testMenu.getRestaurantMenuId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isNoContent());

        Optional<RestaurantMenu> menu = restaurantMenuRepository.
                findByIdAndBranchId(testMenu.getRestaurantMenuId(), testBranch.getRestaurantBranchId());

        assertThat(menu).isNotEmpty();
        assertThat(menu.get().getName()).isEqualTo(payload.restaurantMenuName());
    }

    @Test
    @WithMockPrincipal(roles = RoleName.ROLE_ADMIN)
    @DisplayName("""
            GIVEN: an authenticated Admin
            AND: an enabled restaurant branch with an existing menu
            AND: a null update menu payload
            WHEN: a PUT request is made to the branch menu update endpoint
            THEN: the response status should be 204 No Content
            AND: the menu details should not be updated in the database
            """)
    void updateMenu_Nothing_WhenTheNameIsTheSame() throws Exception {
        UpdateMenuDto payload = createUpdateMenuDto(null);

        mockMvc.perform(put(url, testBranch.getRestaurantBranchId(), testMenu.getRestaurantMenuId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isNoContent());

        Optional<RestaurantMenu> menu = restaurantMenuRepository.
                findByIdAndBranchId(testMenu.getRestaurantMenuId(), testBranch.getRestaurantBranchId());

        assertThat(menu)
                .isNotEmpty();

        assertThat(menu.get().getName())
                .isNotNull()
                .isNotEmpty()
                .isEqualTo(testMenu.getName());
    }

    @Test
    @WithMockPrincipal(roles = RoleName.ROLE_ADMIN)
    @DisplayName("""
            GIVEN: an authenticated Admin
            AND: a branch ID that does not exist in the database
            WHEN: a PUT request is made to the branch menu update endpoint
            THEN: the response status should be 404 Not Found
            AND: a error message is returned
            """)
    void updateMenu_NotFound_WhenBranchDoesNotExist() throws Exception {
        UpdateMenuDto payload = createUpdateMenuDto("Beef");

        mockMvc.perform(put(url, UUID.randomUUID(), testMenu.getRestaurantMenuId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message",
                        CoreMatchers.is(ErrorMessage.RESTAURANT_BRANCH_NOT_FOUND.getMessage())));

    }

    @Test
    @WithMockPrincipal(roles = RoleName.ROLE_ADMIN)
    @DisplayName("""
            GIVEN: an authenticated Admin
            AND: a disabled restaurant branch
            WHEN: a PUT request is made to the branch menu update endpoint
            THEN: the response status should be 400 Bad Request
            AND: a error message is returned
            """)
    void updateMenu_BadRequest_WhenRestaurantDisabled() throws Exception {
        UpdateMenuDto payload = createUpdateMenuDto("Beef");

        testBranch.setEnabled(false);
        restaurantBranchRepository.save(testBranch);

        mockMvc.perform(put(url, testBranch.getRestaurantBranchId(), testMenu.getRestaurantMenuId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message",
                        CoreMatchers.is(ErrorMessage.RESTAURANT_BRANCH_DISABLED.getMessage())));

    }

    @Test
    @WithMockPrincipal(roles = RoleName.ROLE_ADMIN)
    @DisplayName("""
            GIVEN: an authenticated Admin
            AND: a menu ID that does not belong to the branch (or does not exist)
            WHEN: a PUT request is made to the branch menu update endpoint
            THEN: the response status should be 404 Not Found
            AND: a error message is returned
            """)
    void updateMenu_NotFound_WhenMenuDoesNotExistForBranch() throws Exception {
        UpdateMenuDto payload = createUpdateMenuDto("Beef");


        mockMvc.perform(put(url, testBranch.getRestaurantBranchId(), UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message",
                        CoreMatchers.is(ErrorMessage.RESTAURANT_MENU_NOT_FOUND.getMessage())))
                .andDo(print());
    }


    @Test
    @DisplayName("""
            GIVEN: an unauthenticated user
            WHEN: a PUT request is made to the branch menu update endpoint
            THEN: the response status should be 401 Unauthorized
            """)
    void updateMenu_Unauthorized_WhenNoUserIsLoggedIn() throws Exception {

        mockMvc.perform(put(url, testBranch.getRestaurantBranchId(), testMenu.getRestaurantMenuId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Mockito.any(UpdateMenuDto.class))))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message",
                        CoreMatchers.containsStringIgnoringCase("Unauthorized")));
    }

    @Test
    @WithMockPrincipal(roles = RoleName.ROLE_CUSTOMER)
    @DisplayName("""
            GIVEN: an authenticated user without Admin privileges
            WHEN: a PUT request is made to the branch menu update endpoint
            THEN: the response status should be 403 Forbidden
            """)
    void updateMenu_Forbidden_WhenUserIsNotAdmin() throws Exception {
        UpdateMenuDto payload = createUpdateMenuDto("Beef");

        mockMvc.perform(put(url, testBranch.getRestaurantBranchId(), testMenu.getRestaurantMenuId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isForbidden());
    }

    private UpdateMenuDto createUpdateMenuDto(String menuName) {
        return new UpdateMenuDto(menuName);
    }
}
