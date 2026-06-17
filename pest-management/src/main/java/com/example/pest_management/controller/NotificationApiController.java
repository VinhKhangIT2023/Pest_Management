package com.example.pest_management.controller;

import com.example.pest_management.entity.User;
import com.example.pest_management.repository.OrderRepository;
import com.example.pest_management.repository.UserRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Optional;

@RestController
public class NotificationApiController {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    public NotificationApiController(OrderRepository orderRepository, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
    }

    // API này trả về con số nguyên (JSON) để hiển thị lên cái chấm đỏ
    @GetMapping("/api/orders/unread-count")
    public int getUnreadOrderCount(Principal principal) {
        if (principal == null) return 0; // Nếu chưa đăng nhập thì trả về 0

        Optional<User> userOpt = userRepository.findByUsername(principal.getName());
        if (userOpt.isPresent()) {
            return orderRepository.countByUserAndIsViewedFalse(userOpt.get());
        }
        return 0;
    }
}