package com.example.pest_management.config;

import com.example.pest_management.entity.Insect;
import com.example.pest_management.entity.Product;
import com.example.pest_management.entity.User;
import com.example.pest_management.repository.InsectRepository;
import com.example.pest_management.repository.ProductRepository;
import com.example.pest_management.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(InsectRepository insectRepository, ProductRepository productRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            // 1. TẠO TÀI KHOẢN ADMIN GỐC
            Optional<User> adminOpt = userRepository.findByUsername("admingoc");
            if (adminOpt.isEmpty()) {
                User admin = new User();
                admin.setUsername("admingoc");
                admin.setPassword(passwordEncoder.encode("123456"));
                admin.setFullName("Giám Đốc Hệ Thống");
                admin.setRole("ROLE_ROOT_ADMIN");
                admin.setBlocked(false);
                admin.setCreatedAt(LocalDateTime.now());
                userRepository.save(admin);
            }

            if (insectRepository.count() == 0) {
                String[] slugs = {"sau_cuon_la_nho", "ngai_buom_dem", "sau_duc_than_2_cham", "muoi_hanh", "ray_nau", "ray_lung_trang", "voi_voi_hai_lua", "ray_xanh_hai_lua"};
                String[] names = {"Sâu cuốn lá nhỏ", "Ngải bướm đêm", "Sâu đục thân 2 chấm", "Muỗi hành", "Rầy nâu", "Rầy lưng trắng", "Vòi voi hại lúa", "Rầy xanh hại lúa"};

                List<Insect> savedInsects = new ArrayList<>();
                for (int i = 0; i < slugs.length; i++) {
                    Insect insect = new Insect();
                    insect.setName(slugs[i]);
                    insect.setDisplayName(names[i]);
                    insect.setDescription("Dịch hại phổ biến trên lúa. Cần phòng trị kịp thời.");
                    insect.setPrevention("Thường xuyên thăm đồng, sử dụng thuốc khi chớm dịch.");
                    savedInsects.add(insectRepository.save(insect));
                }

                // Danh sách 8 loại thuốc thực tế tương ứng 1-1 với 8 loài ở trên
                String[] realProductNames = {
                        "Thuốc sâu Radiant 60SC (15ml)", // Cho Sâu cuốn lá nhỏ
                        "Thuốc Voliam Targo 063SC (10ml)", // Cho Ngài bướm đêm
                        "Thuốc Virtako 40WG (3g)",       // Cho Sâu đục thân 2 chấm
                        "Thuốc rầy Chess 50WG (15g)",    // Cho Muỗi hành
                        "Thuốc Bassa 50EC (100ml)",      // Cho Rầy nâu
                        "Thuốc Oshin 20WP (6.5g)",        // Cho Rầy lưng trắng
                        "Thuốc Marshal 200EC (100ml)",   // Cho Vòi voi hại lúa
                        "Thuốc Trebon 10EC (100ml)"      // Cho Rầy xanh hại lúa
                };

                double[] prices = {85000.0, 12000.0, 35000.0, 45000.0, 55000.0, 60000.0, 110000.0, 95000.0};

                // Chỉ dùng 1 vòng lặp để tạo đúng 8 sản phẩm
                for (int i = 0; i < savedInsects.size(); i++) {
                    Insect insect = savedInsects.get(i);
                    Product product = new Product();

                    product.setName(realProductNames[i]);
                    product.setDescription("Chế phẩm đặc trị hiệu quả cao, chuyên dùng tiêu diệt " + insect.getDisplayName() + ". Hàng chính hãng.");
                    product.setPrice(prices[i]);
                    product.setImageUrl(null); // Admin sẽ tải ảnh thật từ máy lên
                    product.setHot(i % 2 == 0); // Đánh dấu ngẫu nhiên vài sản phẩm là bán chạy
                    product.setInsect(insect);

                    productRepository.save(product);
                }
            }
        };
    }
}