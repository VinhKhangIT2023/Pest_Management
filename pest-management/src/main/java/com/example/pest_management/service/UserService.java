package com.example.pest_management.service;

import com.example.pest_management.entity.User;
import com.example.pest_management.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // ==========================================
    // 1. QUẢN LÝ TÀI KHOẢN (ADMIN)
    // ==========================================
    public List<User> getNormalUsers() {
        return userRepository.findAll().stream()
                .filter(u -> "ROLE_USER".equals(u.getRole()))
                .collect(Collectors.toList());
    }

    public List<User> getAdminUsers() {
        return userRepository.findAll().stream()
                .filter(u -> u.getRole().contains("ADMIN"))
                .collect(Collectors.toList());
    }

    public void createAdmin(User admin) {
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        admin.setRole("ROLE_ADMIN");
        admin.setBlocked(false);
        userRepository.save(admin);
    }

    public void toggleBlockUser(Long id) {
        User user = userRepository.findById(id).orElseThrow();
        user.setBlocked(!user.getIsBlocked());
        userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    // ==========================================
    // 2. HÀM ĐĂNG KÝ TÀI KHOẢN (DÀNH CHO USER MỚI)
    // ==========================================
    public void registerUser(User user) throws Exception {
        // Kiểm tra xem username đã tồn tại chưa
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new Exception("Tên đăng nhập đã được sử dụng!");
        }

        // Mã hóa mật khẩu và phân quyền mặc định là USER
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("ROLE_USER");
        user.setBlocked(false);

        userRepository.save(user);
    }

    // ==========================================
    // 3. CÁC HÀM XỬ LÝ HỒ SƠ CÁ NHÂN (PROFILE)
    // ==========================================
    public void updateProfile(Long userId, String fullName, String address, String avatarUrl) {
        User user = userRepository.findById(userId).orElseThrow();
        user.setFullName(fullName);
        user.setDefaultAddress(address);

        // Nếu có upload ảnh mới thì mới cập nhật đường dẫn ảnh
        if (avatarUrl != null) {
            user.setAvatarUrl(avatarUrl);
        }
        userRepository.save(user);
    }

    public boolean changePassword(Long userId, String oldPassword, String newPassword) {
        User user = userRepository.findById(userId).orElseThrow();

        // Kiểm tra mật khẩu cũ có khớp với mã hóa trong CSDL không
        if (passwordEncoder.matches(oldPassword, user.getPassword())) {
            // Khớp thì mã hóa pass mới và lưu lại
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
            return true;
        }
        return false; // Sai mật khẩu cũ
    }
}