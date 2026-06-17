package com.example.pest_management.controller;

import com.example.pest_management.entity.Order;
import com.example.pest_management.entity.User;
import com.example.pest_management.repository.OrderRepository;
import com.example.pest_management.repository.UserRepository;
import com.example.pest_management.service.OrderService;
import com.example.pest_management.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/user")
public class ProfileController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final OrderService orderService;
    private final OrderRepository orderRepository;

    public ProfileController(UserService userService, UserRepository userRepository, OrderService orderService, OrderRepository orderRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.orderService = orderService;
        this.orderRepository = orderRepository;
    }

    private User getCurrentUser(Principal principal) {
        return userRepository.findByUsername(principal.getName()).orElse(null);
    }

    // ==========================================
    // CÁC HÀM GET ĐỂ HIỂN THỊ GIAO DIỆN
    // ==========================================

    @GetMapping("/profile")
    public String profilePage(Model model, Principal principal) {
        model.addAttribute("currentUser", getCurrentUser(principal));
        return "user/profile";
    }

    @GetMapping("/change-password")
    public String changePasswordPage(Model model, Principal principal) {
        model.addAttribute("currentUser", getCurrentUser(principal));
        return "user/change-password";
    }

    @GetMapping("/orders")
    public String orderHistoryPage(Model model, Principal principal) {
        User user = getCurrentUser(principal);
        model.addAttribute("currentUser", user);

        // [TỐI ƯU HÓA] 1. Gọi duy nhất 1 câu lệnh để dập tắt tất cả các chấm đỏ trước
        orderRepository.markAllOrdersAsViewed(user);

        // 2. Mới bắt đầu lấy danh sách đơn hàng ra (lúc này các đơn đều đã là isViewed = true)
        List<Order> orders = orderService.getUserOrderHistory(user);

        model.addAttribute("orders", orders);
        return "user/orders";
    }

    // ==========================================
    // CÁC HÀM POST ĐỂ XỬ LÝ DỮ LIỆU
    // ==========================================

    @PostMapping("/profile/update")
    public String updateProfile(@RequestParam("fullName") String fullName,
                                @RequestParam("defaultAddress") String defaultAddress,
                                @RequestParam("avatarFile") MultipartFile avatarFile,
                                Principal principal, RedirectAttributes redirectAttributes) {
        User user = getCurrentUser(principal);
        String avatarUrl = null;

        try {
            if (!avatarFile.isEmpty()) {
                String fileName = "avatar_" + user.getId() + "_" + System.currentTimeMillis() + ".jpg";
                Path uploadDir = Paths.get("uploads");
                if (!Files.exists(uploadDir)) Files.createDirectories(uploadDir);
                try (InputStream inputStream = avatarFile.getInputStream()) {
                    Files.copy(inputStream, uploadDir.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
                    avatarUrl = "/uploads/" + fileName;
                }
            }
            userService.updateProfile(user.getId(), fullName, defaultAddress, avatarUrl);
            redirectAttributes.addFlashAttribute("successMsg", "Cập nhật thông tin thành công!");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMsg", "Có lỗi xảy ra khi tải ảnh!");
        }
        return "redirect:/user/profile";
    }

    @PostMapping("/change-password")
    public String changePassword(@RequestParam("oldPassword") String oldPassword,
                                 @RequestParam("newPassword") String newPassword,
                                 @RequestParam("confirmPassword") String confirmPassword,
                                 Principal principal, RedirectAttributes redirectAttributes) {
        if (!newPassword.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("errorMsg", "Mật khẩu xác nhận không khớp!");
            return "redirect:/user/change-password";
        }

        User user = getCurrentUser(principal);
        boolean isChanged = userService.changePassword(user.getId(), oldPassword, newPassword);

        if (isChanged) {
            redirectAttributes.addFlashAttribute("successMsg", "Đổi mật khẩu thành công! Hãy dùng mật khẩu mới cho lần đăng nhập sau.");
            return "redirect:/user/profile";
        } else {
            redirectAttributes.addFlashAttribute("errorMsg", "Mật khẩu cũ không chính xác!");
            return "redirect:/user/change-password";
        }
    }

    @PostMapping("/orders/confirm/{id}")
    public String confirmOrderReceived(@PathVariable Long id, Principal principal, RedirectAttributes redirectAttributes) {
        orderService.updateOrderStatus(id, "DELIVERED");
        redirectAttributes.addFlashAttribute("successMsg", "Cảm ơn bạn đã xác nhận nhận hàng!");
        return "redirect:/user/orders";
    }
}