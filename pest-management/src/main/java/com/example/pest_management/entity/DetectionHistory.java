package com.example.pest_management.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "detection_histories")
@Data
public class DetectionHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String uploadedImageUrl; // Lưu đường dẫn ảnh user đã gửi lên hệ thống

    @ManyToOne
    @JoinColumn(name = "detected_insect_id")
    private Insect detectedInsect; // Kết quả nhận diện ra con gì

    private Double confidence; // Độ chính xác AI (ví dụ: 0.88 tức là 88%)
    private LocalDateTime detectedAt = LocalDateTime.now();
}