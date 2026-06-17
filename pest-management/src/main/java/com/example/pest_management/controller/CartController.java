package com.example.pest_management.controller;

import com.example.pest_management.entity.CartItem;
import com.example.pest_management.entity.User;
import com.example.pest_management.repository.UserRepository;
import com.example.pest_management.service.CartService;
import com.example.pest_management.service.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;

@Controller
public class CartController {
    private final CartService cartService;
    private final OrderService orderService;
    private final UserRepository userRepository;

    public CartController(CartService cartService, OrderService orderService, UserRepository userRepository) {
        this.cartService = cartService;
        this.orderService = orderService;
        this.userRepository = userRepository;
    }

    private User getAuthenticatedUser(Principal principal) {
        return userRepository.findByUsername(principal.getName()).orElse(null);
    }

    // HIỂN THỊ GIỎ HÀNG
    @GetMapping("/cart")
    public String viewCart(Principal principal, Model model) {
        User user = getAuthenticatedUser(principal);
        model.addAttribute("currentUser", user);

        List<CartItem> items = cartService.getCartEntries(user);
        double total = items.stream().mapToDouble(i -> i.getProduct().getPrice() * i.getQuantity()).sum();

        model.addAttribute("cartItems", items);
        model.addAttribute("totalAmount", total);
        return "user/cart";
    }

    // THÊM SẢN PHẨM VÀO GIỎ HÀNG
    @PostMapping("/cart/add")
    public String addToCart(@RequestParam("productId") Long productId, @RequestParam(value = "quantity", defaultValue = "1") int quantity, Principal principal) {
        User user = getAuthenticatedUser(principal);
        cartService.addProductToCart(user, productId, quantity);
        return "redirect:/cart";
    }

    // CẬP NHẬT TĂNG GIẢM SỐ LƯỢNG (+/-)
    @PostMapping("/cart/update")
    public String updateCartQuantity(@RequestParam("itemId") Long itemId, @RequestParam("action") String action) {
        cartService.updateQuantity(itemId, action);
        return "redirect:/cart";
    }

    // XÓA SẢN PHẨM KHỎI GIỎ HÀNG
    @PostMapping("/cart/remove/{id}")
    public String removeFromCart(@PathVariable("id") Long itemId) {
        cartService.removeCartItem(itemId);
        return "redirect:/cart";
    }

    // XỬ LÝ ĐẶT HÀNG (CHECKOUT)
    @PostMapping("/checkout")
    public String processCheckout(@RequestParam("address") String address,
                                  @RequestParam("paymentMethod") String paymentMethod,
                                  Principal principal) {
        User user = getAuthenticatedUser(principal);
        try {
            orderService.checkout(user, address, paymentMethod);
            // Trả về đường dẫn chuyển hướng sang trang lịch sử đơn hàng
            return "redirect:/user/orders";
        } catch (Exception e) {
            return "redirect:/cart?error=true";
        }
    }
}