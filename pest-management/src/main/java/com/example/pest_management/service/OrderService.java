package com.example.pest_management.service;

import com.example.pest_management.entity.CartItem;
import com.example.pest_management.entity.Order;
import com.example.pest_management.entity.OrderDetail;
import com.example.pest_management.entity.User;
import com.example.pest_management.repository.CartItemRepository;
import com.example.pest_management.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class OrderService {
    private final OrderRepository orderRepository;
    private final CartItemRepository cartItemRepository;

    public OrderService(OrderRepository orderRepository, CartItemRepository cartItemRepository) {
        this.orderRepository = orderRepository;
        this.cartItemRepository = cartItemRepository;
    }

    public Order checkout(User user, String shippingAddress, String paymentMethod) throws Exception {
        List<CartItem> cartItems = cartItemRepository.findByUser(user);
        if (cartItems.isEmpty()) throw new Exception("Giỏ hàng trống!");

        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setShippingAddress(shippingAddress);
        order.setPaymentMethod(paymentMethod);
        order.setStatus("PENDING"); // Mặc định: Đang xử lý

        double totalAmount = 0;
        List<OrderDetail> details = new ArrayList<>();
        for (CartItem item : cartItems) {
            OrderDetail detail = new OrderDetail();
            detail.setOrder(order);
            detail.setProduct(item.getProduct());
            detail.setQuantity(item.getQuantity());
            detail.setPriceAtPurchase(item.getProduct().getPrice());
            totalAmount += item.getProduct().getPrice() * item.getQuantity();
            details.add(detail);
        }

        order.setOrderDetails(details);
        order.setTotalAmount(totalAmount);
        Order savedOrder = orderRepository.save(order);
        cartItemRepository.deleteByUser(user);
        return savedOrder;
    }

    public List<Order> getUserOrderHistory(User user) {
        return orderRepository.findByUserOrderByOrderDateDesc(user);
    }

    // HÀM MỚI: Lấy tất cả đơn hàng cho Admin
    public List<Order> getAllOrdersForAdmin() {
        return orderRepository.findAllByOrderByOrderDateDesc();
    }

    // HÀM MỚI: Cập nhật trạng thái đơn hàng
    public void updateOrderStatus(Long orderId, String status) {
        Order order = orderRepository.findById(orderId).orElseThrow();
        order.setStatus(status);
        orderRepository.save(order);
    }
}