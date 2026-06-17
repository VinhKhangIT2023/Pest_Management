package com.example.pest_management.repository;

import com.example.pest_management.entity.Order;
import com.example.pest_management.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserOrderByOrderDateDesc(User user);

    List<Order> findAllByOrderByOrderDateDesc();

    // BỔ SUNG: Đếm số lượng đơn hàng có thay đổi mà User chưa xem
    int countByUserAndIsViewedFalse(User user);

    // [TỐI ƯU HÓA] Cập nhật toàn bộ đơn hàng thành "Đã xem" bằng 1 câu lệnh SQL duy nhất
    @Modifying
    @Transactional
    @Query("UPDATE Order o SET o.isViewed = true WHERE o.user = :user AND o.isViewed = false")
    void markAllOrdersAsViewed(@Param("user") User user);
}