package com.example.pest_management.repository;

import com.example.pest_management.entity.DetectionHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DetectionHistoryRepository extends JpaRepository<DetectionHistory, Long> {
    // Xem lịch sử nhận diện của một User, xếp từ lần mới nhất trở về trước
    List<DetectionHistory> findByUserIdOrderByDetectedAtDesc(Long userId);
}