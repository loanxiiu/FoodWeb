package com.example.food_web2.repository;

import com.example.food_web2.dto.response.BrandStatisticsResponse;
import com.example.food_web2.dto.response.CustomerProductStatisticsResponse;
import com.example.food_web2.dto.response.CustomerStatisticsResponse;
import com.example.food_web2.dto.response.ProductStatisticsResponse;
import com.example.food_web2.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer> {
    List<OrderDetail> findByOrderId(int orderId);

    @Query("""
        SELECT new com.example.food_web2.dto.response.ProductStatisticsResponse(
            od.food.name,
            SUM(od.quantity)
        )
        FROM OrderDetail od
        WHERE od.order.orderDate BETWEEN :startDate AND :endDate
        AND od.order.status = 'Giao hàng thành công'
        GROUP BY od.food.name
    """)
    List<ProductStatisticsResponse> findProductStatisticsByMonth(LocalDate startDate, LocalDate endDate);

    @Query("""
        SELECT new com.example.food_web2.dto.response.CustomerStatisticsResponse(
            o.customer.user.name,
            SUM(o.totalPrice)
        )
        FROM Order o
        WHERE o.orderDate BETWEEN :startDate AND :endDate
        AND o.status = 'Giao hàng thành công'
        GROUP BY o.customer.user.name
    """)
    List<CustomerStatisticsResponse> findCustomerStatisticsByMonth(LocalDate startDate, LocalDate endDate);

    @Query("""
    SELECT o.customer.id, od.quantity
    FROM Order o
    JOIN o.orderDetails od
    WHERE o.orderDate BETWEEN :startDate AND :endDate
    AND o.status = 'Giao hàng thành công'
""")
    List<Object[]> findCustomerOrderDetailsByMonth(LocalDate startDate, LocalDate endDate);


    @Query("""
        SELECT new com.example.food_web2.dto.response.BrandStatisticsResponse(
            f.brand.name,
            SUM(od.quantity)
        )
        FROM OrderDetail od
        JOIN od.food f
        WHERE od.order.orderDate BETWEEN :startDate AND :endDate
        AND od.order.status = 'Giao hàng thành công'
        GROUP BY f.brand.name
    """)
    List<BrandStatisticsResponse> findBrandStatisticsByMonth(LocalDate startDate, LocalDate endDate);

}
