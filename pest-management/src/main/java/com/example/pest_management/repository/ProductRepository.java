package com.example.pest_management.repository;

import com.example.pest_management.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    // Tìm kiếm theo tên
    List<Product> findByNameContainingIgnoreCase(String keyword);

    // Tìm kiếm tất cả sản phẩm thuộc về 1 loài sâu bệnh cụ thể
    List<Product> findByInsectId(Long insectId);
}