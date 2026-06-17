package com.example.pest_management.controller;

import com.example.pest_management.entity.Order;
import com.example.pest_management.entity.User;
import com.example.pest_management.repository.OrderRepository;
import com.example.pest_management.repository.UserRepository;
import com.example.pest_management.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

@Controller
public class HomeController {

    private final ProductService productService;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    public HomeController(ProductService productService, UserRepository userRepository, OrderRepository orderRepository) {
        this.productService = productService;
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
    }

    @GetMapping("/")
    public String homePage(@RequestParam(value = "keyword", required = false) String keyword, Model model, Principal principal) {
        if (principal != null) {
            User currentUser = userRepository.findByUsername(principal.getName()).orElse(null);
            model.addAttribute("currentUser", currentUser);
        }

        if (keyword != null && !keyword.isEmpty()) {
            model.addAttribute("products", productService.searchProducts(keyword));
        } else {
            model.addAttribute("products", productService.getAllProducts());
        }

        return "user/index";
    }

    @GetMapping("/product/{id}")
    public String productDetail(@PathVariable Long id, Model model, Principal principal) {
        if (principal != null) {
            model.addAttribute("currentUser", userRepository.findByUsername(principal.getName()).orElse(null));
        }
        model.addAttribute("product", productService.getProductById(id));
        return "user/product-detail";
    }
}