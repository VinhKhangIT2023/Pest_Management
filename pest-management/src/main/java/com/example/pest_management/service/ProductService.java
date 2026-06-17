package com.example.pest_management.service;

import com.example.pest_management.entity.Product;
import com.example.pest_management.repository.ProductRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAllProducts() { return productRepository.findAll(); }

    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy sản phẩm có ID: " + id));
    }

    public void saveProduct(Product product) { productRepository.save(product); }
    public void deleteProduct(Long id) { productRepository.deleteById(id); }
    public List<Product> searchProducts(String keyword) {
        return productRepository.findByNameContainingIgnoreCase(keyword);
    }
}