package com.example.pest_management.controller;

import com.example.pest_management.entity.Product;
import com.example.pest_management.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/products/{id}")
    public String showProductDetail(@PathVariable("id") Long id, Model model) {
        Product product = productService.getProductById(id);
        model.addAttribute("product", product);
        return "user/product-detail";
    }

    // HÀM MỚI: API trả về JSON cho thanh tìm kiếm gợi ý
    @GetMapping("/api/search")
    @ResponseBody
    public ResponseEntity<?> searchApi(@RequestParam("keyword") String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) return ResponseEntity.ok(List.of());

        List<Map<String, Object>> result = productService.searchProducts(keyword).stream()
                .map(p -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", p.getId());
                    map.put("name", p.getName());
                    map.put("imageUrl", p.getImageUrl() != null ? p.getImageUrl() : "");
                    map.put("price", p.getPrice());
                    return map;
                })
                .limit(5) // Chỉ lấy 5 kết quả gợi ý đầu tiên
                .collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }
}