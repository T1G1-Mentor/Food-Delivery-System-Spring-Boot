package com.mentorship.food_delivery_app.order.repository;

import com.mentorship.food_delivery_app.order.entity.Order;
import com.mentorship.food_delivery_app.order.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {

    @Query("""
                SELECT o FROM Order o
                JOIN FETCH o.trackingHistory th
                JOIN FETCH o.customer c
                JOIN FETCH c.user
                WHERE o.orderId= :orderId AND o.branch.admin.id= :userId
            """)
    Optional<Order> fetchOrderWithTrackingAndRestaurantBranchAndCustomer(UUID orderId, UUID userId);

    @Query(
            value = """
                    SELECT o FROM Order o
                    JOIN FETCH o.customer c
                    JOIN FETCH c.user
                    JOIN FETCH o.branch b
                    JOIN FETCH b.restaurant
                    WHERE o.branch.admin.id = :adminId
                    """,
            countQuery = "SELECT COUNT(o) FROM Order o WHERE o.branch.admin.id = :adminId"
    )
    Page<Order> findOrdersByAdmin(@Param("adminId") UUID adminId, Pageable pageable);

    @Query(
            value = """
                    SELECT o FROM Order o
                    JOIN FETCH o.customer c
                    JOIN FETCH c.user
                    JOIN FETCH o.branch b
                    JOIN FETCH b.restaurant
                    WHERE o.branch.admin.id = :adminId AND o.status = :status
                    """,
            countQuery = "SELECT COUNT(o) FROM Order o WHERE o.branch.admin.id = :adminId AND o.status = :status"
    )
    Page<Order> findOrdersByAdminAndStatus(@Param("adminId") UUID adminId, @Param("status") OrderStatus status, Pageable pageable);

    @Query("""
                SELECT o FROM Order o
                JOIN FETCH o.items i
                JOIN FETCH i.menuItem
                JOIN FETCH o.trackingHistory
                JOIN FETCH o.address
                JOIN FETCH o.customer c
                JOIN FETCH c.user
                JOIN FETCH o.branch b
                JOIN FETCH b.restaurant
                LEFT JOIN FETCH o.coupon
                WHERE o.orderId = :orderId AND o.branch.admin.id = :adminId
            """)
    Optional<Order> fetchOrderDetailsForAdmin(@Param("orderId") UUID orderId, @Param("adminId") UUID adminId);

    @Query("""
                SELECT o FROM Order o
                JOIN FETCH o.customer c
                JOIN FETCH c.user
                JOIN FETCH o.branch b
                JOIN FETCH b.restaurant
                WHERE o.orderId = :orderId AND o.branch.admin.id = :adminId
            """)
    Optional<Order> fetchOrderSummaryForAdmin(@Param("orderId") UUID orderId, @Param("adminId") UUID adminId);

    @Query(
            value = """
                    SELECT o FROM Order o
                    JOIN FETCH o.branch b
                    JOIN FETCH b.restaurant
                    WHERE o.customer.user.id = :userId
                    """,
            countQuery = "SELECT COUNT(o) FROM Order o WHERE o.customer.user.id = :userId"
    )
    Page<Order> findOrdersByUserId(@Param("userId") UUID userId, Pageable pageable);

    @Query(
            value = """
                    SELECT o FROM Order o
                    JOIN FETCH o.branch b
                    JOIN FETCH b.restaurant
                    WHERE o.customer.user.id = :userId AND o.status = :status
                    """,
            countQuery = "SELECT COUNT(o) FROM Order o WHERE o.customer.user.id = :userId AND o.status = :status"
    )
    Page<Order> findOrdersByUserIdAndStatus(@Param("userId") UUID userId, @Param("status") OrderStatus status, Pageable pageable);
}
