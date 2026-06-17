package com.example.pest_management.service;

import com.example.pest_management.entity.CartItem;
import com.example.pest_management.entity.Product;
import com.example.pest_management.entity.User;
import com.example.pest_management.repository.CartItemRepository;
import com.example.pest_management.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CartService {
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;

    public CartService(CartItemRepository cartItemRepository, ProductRepository productRepository) {
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
    }

    public List<CartItem> getCartEntries(User user) {
        return cartItemRepository.findByUser(user);
    }

    public void addProductToCart(User user, Long productId, int quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Sản phẩm không tồn tại"));
        Optional<CartItem> existingItem = cartItemRepository.findByUserAndProduct(user, product);

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + quantity);
            cartItemRepository.save(item);
        } else {
            CartItem newItem = new CartItem();
            newItem.setUser(user);
            newItem.setProduct(product);
            newItem.setQuantity(quantity);
            cartItemRepository.save(newItem);
        }
    }

    // Hàm cập nhật số lượng (+ / -) trong Giỏ hàng
    public void updateQuantity(Long itemId, String action) {
        CartItem item = cartItemRepository.findById(itemId).orElseThrow();
        if ("increase".equals(action)) {
            item.setQuantity(item.getQuantity() + 1);
        } else if ("decrease".equals(action) && item.getQuantity() > 1) {
            item.setQuantity(item.getQuantity() - 1);
        }
        cartItemRepository.save(item);
    }

    public void removeCartItem(Long itemId) {
        cartItemRepository.deleteById(itemId);
    }

    public void clearCart(User user) {
        cartItemRepository.deleteByUser(user);
    }
}