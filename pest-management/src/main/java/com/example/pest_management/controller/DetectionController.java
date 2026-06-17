package com.example.pest_management.controller;

import com.example.pest_management.entity.Insect;
import com.example.pest_management.entity.User;
import com.example.pest_management.repository.InsectRepository;
import com.example.pest_management.repository.UserRepository;
import com.example.pest_management.service.DetectionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import com.example.pest_management.service.TelegramService;

import java.security.Principal;
import java.util.Optional;

@Controller
public class DetectionController {
    private final DetectionService detectionService;
    private final InsectRepository insectRepository;
    private final UserRepository userRepository; // Bổ sung UserRepository
    private final TelegramService telegramService;

    public DetectionController(DetectionService detectionService, InsectRepository insectRepository, UserRepository userRepository, TelegramService telegramService) {
        this.detectionService = detectionService;
        this.insectRepository = insectRepository;
        this.userRepository = userRepository;
        this.telegramService = telegramService;
    }

    @PostMapping("/detect")
    // Sửa "image" thành "file" và bổ sung Principal
    public String detectImage(@RequestParam("file") MultipartFile file, Model model, Principal principal) {

        // Lấy thông tin tài khoản đang đăng nhập để truyền ra Navbar
        if (principal != null) {
            User currentUser = userRepository.findByUsername(principal.getName()).orElse(null);
            model.addAttribute("currentUser", currentUser);
        }

        try {
            String predictedLabel = detectionService.detectPestFromImage(file);
            Optional<Insect> insectOpt = insectRepository.findByName(predictedLabel.trim());

            if (insectOpt.isPresent()) {
                Insect insect = insectOpt.get();
                model.addAttribute("insect", insect);
                model.addAttribute("products", insect.getProducts());
                telegramService.sendPestNotification(insect);
            } else {
                model.addAttribute("error", "AI nhận diện ra nhãn '" + predictedLabel + "' nhưng hệ thống chưa có dữ liệu loài này.");
            }
        } catch (Exception e) {
            model.addAttribute("error", "Kết nối máy chủ AI thất bại! Bạn đã khởi chạy dự án Python chứa mô hình YOLOv8 chưa?");
        }
        return "user/result";
    }
}