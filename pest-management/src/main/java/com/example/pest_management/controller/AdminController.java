package com.example.pest_management.controller;

import com.example.pest_management.entity.Order;
import com.example.pest_management.entity.Product;
import com.example.pest_management.entity.User;
import com.example.pest_management.repository.OrderRepository;
import com.example.pest_management.repository.InsectRepository;
import com.example.pest_management.repository.UserRepository;
import com.example.pest_management.service.OrderService;
import com.example.pest_management.service.ProductService;
import com.example.pest_management.service.UserService;
import com.example.pest_management.PestInfo;
import com.example.pest_management.service.TelegramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final ProductService productService;
    private final UserService userService;
    private final OrderService orderService;
    private final OrderRepository orderRepository;
    private final InsectRepository insectRepository;
    private final UserRepository userRepository;

    public AdminController(ProductService productService, UserService userService, OrderService orderService, OrderRepository orderRepository, InsectRepository insectRepository, UserRepository userRepository) {
        this.productService = productService;
        this.userService = userService;
        this.orderService = orderService;
        this.orderRepository = orderRepository;
        this.insectRepository = insectRepository;
        this.userRepository = userRepository;
    }

    private User getCurrentUser(Principal principal) {
        return userRepository.findByUsername(principal.getName()).orElse(null);
    }

    // ==========================================
    // 1. QUẢN LÝ THỐNG KÊ (CHỈ ROOT ADMIN)
    // ==========================================
    @GetMapping({"", "/dashboard"})
    public String dashboard(Model model, Principal principal) {
        User currentUser = getCurrentUser(principal);
        if (currentUser.getRole().equals("ROLE_ADMIN")) {
            return "redirect:/admin/orders";
        }

        model.addAttribute("currentUser", currentUser);

        double totalRevenue = orderRepository.findAll().stream()
                .filter(o -> "DELIVERED".equals(o.getStatus()))
                .mapToDouble(o -> o.getTotalAmount()).sum();

        long totalDeliveredOrders = orderRepository.findAll().stream()
                .filter(o -> "DELIVERED".equals(o.getStatus()))
                .count();

        model.addAttribute("totalRevenue", totalRevenue);
        model.addAttribute("totalOrders", totalDeliveredOrders);
        model.addAttribute("totalUsers", userService.getNormalUsers().size());
        return "admin/dashboard";
    }

    // ==========================================
    // 2. QUẢN LÝ SẢN PHẨM (CHỈ ROOT ADMIN)
    // ==========================================
    @GetMapping("/products")
    public String manageProducts(@RequestParam(value = "keyword", required = false) String keyword, Model model, Principal principal) {
        User currentUser = getCurrentUser(principal);
        if (currentUser.getRole().equals("ROLE_ADMIN")) return "redirect:/admin/orders";

        model.addAttribute("currentUser", currentUser);
        if (keyword != null && !keyword.isEmpty()) {
            model.addAttribute("products", productService.searchProducts(keyword));
        } else {
            model.addAttribute("products", productService.getAllProducts());
        }
        model.addAttribute("insects", insectRepository.findAll());
        model.addAttribute("newProduct", new Product());
        model.addAttribute("keyword", keyword);
        return "admin/products";
    }

    @PostMapping("/products/save")
    public String saveProduct(@ModelAttribute Product product, @RequestParam("imageFile") MultipartFile imageFile, Principal principal) {
        User currentUser = getCurrentUser(principal);
        if (currentUser.getRole().equals("ROLE_ADMIN")) return "redirect:/admin/orders";

        try {
            if (!imageFile.isEmpty()) {
                String fileName = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();
                Path uploadDir = Paths.get("uploads");
                if (!Files.exists(uploadDir)) Files.createDirectories(uploadDir);
                try (InputStream inputStream = imageFile.getInputStream()) {
                    Files.copy(inputStream, uploadDir.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
                    product.setImageUrl("/uploads/" + fileName);
                }
            } else if (product.getId() != null) {
                product.setImageUrl(productService.getProductById(product.getId()).getImageUrl());
            }
            productService.saveProduct(product);
        } catch (Exception e) { e.printStackTrace(); }
        return "redirect:/admin/products";
    }

    @GetMapping("/products/edit/{id}")
    public String editProduct(@PathVariable Long id, Model model, Principal principal) {
        User currentUser = getCurrentUser(principal);
        if (currentUser.getRole().equals("ROLE_ADMIN")) return "redirect:/admin/orders";

        model.addAttribute("currentUser", currentUser);
        model.addAttribute("product", productService.getProductById(id));
        model.addAttribute("insects", insectRepository.findAll());
        return "admin/edit-product";
    }

    @PostMapping("/products/delete/{id}")
    public String deleteProduct(@PathVariable Long id, Principal principal) {
        User currentUser = getCurrentUser(principal);
        if (currentUser.getRole().equals("ROLE_ADMIN")) return "redirect:/admin/orders";

        productService.deleteProduct(id);
        return "redirect:/admin/products";
    }

    // ==========================================
    // 3. QUẢN LÝ VẬN HÀNH ĐƠN HÀNG (CHỈ SUB ADMIN)
    // ==========================================
    @GetMapping("/orders")
    public String manageOrders(Model model, Principal principal) {
        User currentUser = getCurrentUser(principal);
        if (currentUser.getRole().equals("ROLE_ROOT_ADMIN")) return "redirect:/admin/dashboard";

        model.addAttribute("currentUser", currentUser);
        model.addAttribute("orders", orderService.getAllOrdersForAdmin());
        return "admin/orders";
    }

    @PostMapping("/orders/ship/{id}")
    public String shipOrder(@PathVariable Long id, Principal principal) {
        User currentUser = getCurrentUser(principal);
        if (currentUser.getRole().equals("ROLE_ROOT_ADMIN")) return "redirect:/admin/dashboard";

        // Lấy đơn hàng ra, cập nhật trạng thái và BẬT CHẤM ĐỎ THÔNG BÁO
        Order order = orderRepository.findById(id).orElseThrow();
        order.setStatus("SHIPPING");
        order.setViewed(false); // Báo hiệu: Đơn vừa cập nhật, User chưa xem
        orderRepository.save(order);

        return "redirect:/admin/orders";
    }

    // ==========================================
    // 4. QUẢN LÝ NGƯỜI DÙNG & ADMIN SYSTEM
    // ==========================================
    @GetMapping("/users")
    public String manageUsers(Model model, Principal principal) {
        model.addAttribute("currentUser", getCurrentUser(principal));
        model.addAttribute("users", userService.getNormalUsers());
        return "admin/users";
    }

    @GetMapping("/admins")
    public String manageAdmins(Model model, Principal principal) {
        User currentUser = getCurrentUser(principal);
        if (currentUser.getRole().equals("ROLE_ADMIN")) return "redirect:/admin/orders";

        model.addAttribute("currentUser", currentUser);
        model.addAttribute("admins", userService.getAdminUsers());
        return "admin/admins";
    }

    @PostMapping("/users/create-admin")
    public String createAdmin(@RequestParam("fullName") String fullName, @RequestParam("username") String username, @RequestParam("password") String password, Principal principal) {
        User currentUser = getCurrentUser(principal);
        if (!currentUser.getRole().equals("ROLE_ROOT_ADMIN")) {
            return "redirect:/admin/admins?error=nopermission";
        }
        try {
            User admin = new User();
            admin.setFullName(fullName);
            admin.setUsername(username);
            admin.setPassword(password);
            userService.createAdmin(admin);
        } catch (Exception e) { e.printStackTrace(); }
        return "redirect:/admin/admins";
    }

    @PostMapping("/users/toggle-block/{id}")
    public String toggleBlockUser(@PathVariable Long id, Principal principal) {
        User targetUser = userRepository.findById(id).orElseThrow();
        User currentUser = getCurrentUser(principal);
        if (targetUser.getRole().contains("ADMIN") && !currentUser.getRole().equals("ROLE_ROOT_ADMIN")) {
            return "redirect:/admin/admins?error=nopermission";
        }
        userService.toggleBlockUser(id);
        return targetUser.getRole().contains("ADMIN") ? "redirect:/admin/admins" : "redirect:/admin/users";
    }

    @PostMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable Long id, Principal principal) {
        User targetUser = userRepository.findById(id).orElseThrow();
        User currentUser = getCurrentUser(principal);
        if (targetUser.getRole().contains("ADMIN") && !currentUser.getRole().equals("ROLE_ROOT_ADMIN")) {
            return "redirect:/admin/admins?error=nopermission";
        }
        userService.deleteUser(id);
        return targetUser.getRole().contains("ADMIN") ? "redirect:/admin/admins" : "redirect:/admin/users";
    }
}