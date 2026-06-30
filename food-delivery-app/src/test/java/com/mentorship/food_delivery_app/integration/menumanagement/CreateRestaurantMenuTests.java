package com.mentorship.food_delivery_app.integration.menumanagement;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mentorship.food_delivery_app.common.enums.ErrorMessage;
import com.mentorship.food_delivery_app.integration.BaseIntegrationTest;
import com.mentorship.food_delivery_app.integration.security.WithMockPrincipal;
import com.mentorship.food_delivery_app.restaurant.dto.restaurantmenu.request.CreateMenuDto;
import com.mentorship.food_delivery_app.restaurant.dto.restaurantmenu.response.RestaurantMenuDto;
import com.mentorship.food_delivery_app.restaurant.entity.Restaurant;
import com.mentorship.food_delivery_app.restaurant.entity.RestaurantBranch;
import com.mentorship.food_delivery_app.restaurant.repository.RestaurantBranchRepository;
import com.mentorship.food_delivery_app.restaurant.repository.RestaurantMenuRepository;
import com.mentorship.food_delivery_app.restaurant.repository.RestaurantRepository;
import com.mentorship.food_delivery_app.user.entity.enums.RoleName;
import com.mentorship.food_delivery_app.user.entity.enums.UserType;
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
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CreateRestaurantMenuTests extends BaseIntegrationTest {

    private final String url = "/api/v1/admin/restaurants/branchs/{branchId}/restaurant-menus";
    private RestaurantBranch testBranch;


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
    }

    @Test
    @WithMockPrincipal(roles = RoleName.ROLE_ADMIN, userType = UserType.USER_ADMIN)
    @DisplayName("""
            GIVEN an authenticated Admin AND a valid menu payload
            WHEN a POST request is made to the branch menu endpoint
            THEN the response status should be 201 Created AND the menu should be saved in the database
            """)
    void createMenu_Success() throws Exception {
        CreateMenuDto payload = createMenuDto("Burgers");

        mockMvc.perform(post(url, testBranch.getRestaurantBranchId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(payload))
        ).andExpect(status().isCreated());
        List<RestaurantMenuDto> menus =
                restaurantMenuRepository.findAllByBranchId(testBranch.getRestaurantBranchId());

        assertThat(menus)
                .hasSize(1)
                .isNotNull()
                .isNotEmpty();

        assertThat(menus.getFirst().restaurantMenuName()).isEqualTo(payload.restaurantMenuName());

    }

    @Test
    @WithMockPrincipal(roles = RoleName.ROLE_CUSTOMER)
    @DisplayName("""
            GIVEN an authenticated Customer (without Admin privileges) AND a valid menu payload
            WHEN a POST request is made to the branch menu endpoint
            THEN the response status should be 403 Forbidden
            """)
    void createMenu_Forbidden_WhenUserIsNotAdmin() throws Exception {
        CreateMenuDto payload = createMenuDto("Burgers");

        mockMvc.perform(post(url, testBranch.getRestaurantBranchId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isForbidden());

    }

    @Test
    @DisplayName("""
            GIVEN: an unauthenticated user
            WHEN: a POST request is made to the branch menu endpoint
            THEN: the response status should be 401 Unauthorized
            """)
    void createMenu_Unauthorized_WhenNoUserIsLoggedIn() throws Exception {

        mockMvc.perform(post(url, testBranch.getRestaurantBranchId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Mockito.any(CreateMenuDto.class))))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message",
                        CoreMatchers.containsStringIgnoringCase("Unauthorized")));
    }

    @Test
    @WithMockPrincipal(roles = RoleName.ROLE_ADMIN)
    @DisplayName("""
            GIVEN: an authenticated Admin
            AND: a branch ID that does not exist in the database
            WHEN: a POST request is made to the branch menu endpoint
            THEN: the response status should be 404 Not Found
            AND: a resource not found message is returned
            """)
    void createMenu_NotFound_WhenBranchDoesNotExist() throws Exception {
        CreateMenuDto payload = createMenuDto("Burgers");

        mockMvc.perform(post(url, UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message",
                        CoreMatchers.is(ErrorMessage.RESTAURANT_BRANCH_NOT_FOUND.getMessage())))
                .andDo(print());

    }

    @Test
    @WithMockPrincipal(roles = RoleName.ROLE_ADMIN)
    @DisplayName("""
            GIVEN: an authenticated Admin
            AND: an invalid menu payload
            WHEN: a POST request is made to the branch menu endpoint
            THEN: the response status should be 400 Bad Request
            AND: validation error details are returned
            """)
    void createMenu_BadRequest_WhenPayloadIsInvalid() throws Exception {
        CreateMenuDto payload = createMenuDto(" ");

        mockMvc.perform(post(url, testBranch.getRestaurantBranchId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message",
                        CoreMatchers.containsStringIgnoringCase("validation failed")));

    }

    @Test
    @WithMockPrincipal(roles = RoleName.ROLE_ADMIN)
    @DisplayName("""
            GIVEN: an authenticated Admin
            AND: an disabled restaurant branch
            WHEN: a POST request is made to the branch menu endpoint
            THEN: the response status should be 400 Bad Request
            AND: error details are returned
            """)
    void createMenu_BadRequest_WhenRestaurantDisabled() throws Exception {
        CreateMenuDto payload = createMenuDto("Burgers");

        testBranch.setEnabled(false);
        restaurantBranchRepository.save(testBranch);

        mockMvc.perform(post(url, testBranch.getRestaurantBranchId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message",
                        CoreMatchers.is(ErrorMessage.RESTAURANT_BRANCH_DISABLED.getMessage())));

    }

    private CreateMenuDto createMenuDto(String restaurantMenuName) {
        return new CreateMenuDto(restaurantMenuName);
    }
}
