package com.example.pest_management.service;

import com.example.pest_management.entity.Insect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.HashMap;
import java.util.Map;
import com.example.pest_management.PestInfo;

@Service
public class TelegramService {

    @Value("${telegram.bot.token}")
    private String botToken;

    @Value("${telegram.chat.id}")
    private String chatId;

    private final RestTemplate restTemplate = new RestTemplate();

    public void sendPestNotification(Insect insect) {
        // Kiểm tra phòng vệ (Guard Clause)
        if (insect == null) return;

        String url = "https://api.telegram.org/bot" + botToken + "/sendMessage";

        // Thực hiện đối chiếu từ tên nhãn DB sang cấu trúc dữ liệu Enum mã cứng
        PestInfo enumInfo = PestInfo.fromLabel(insect.getName());

        String commonName;
        String scientificName;
        String damage;
        String solution;

        if (enumInfo != null) {
            commonName = enumInfo.getCommonName();
            scientificName = enumInfo.getScientificName();
            damage = enumInfo.getDamage();
            solution = enumInfo.getSolution();
        } else {
            commonName = insect.getDisplayName() != null ? insect.getDisplayName() : insect.getName();
            scientificName = insect.getScientificName() != null ? insect.getScientificName() : "Chưa cập nhật";
            damage = insect.getDamage() != null ? insect.getDamage() : "Chưa cập nhật";
            solution = insect.getSolution() != null ? insect.getSolution() : "Chưa cập nhật";
        }

        // Định dạng cấu trúc chuỗi Markdown để Telegram hiển thị đẹp mắt
        String messageText = String.format(
                "🚨 *HỆ THỐNG PHÁT HIỆN SÂU BỆNH* 🚨\n\n" +
                        "📌 *Tên thông thường:* %s\n" +
                        "🔬 *Tên khoa học:* _%s_\n\n" +
                        "❌ *Tác hại:* \n%s\n\n" +
                        "💡 *Giải pháp khuyến nghị:* \n%s",
                commonName,
                scientificName,
                damage,
                solution
        );

        // Tạo Map để đóng gói Payload thành cấu trúc JSON (CHỈ ĐỂ LẠI 1 KHỐI NÀY)
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("chat_id", chatId);
        requestBody.put("text", messageText);
        requestBody.put("parse_mode", "Markdown");

        try {
            // Thực hiện HTTP POST Request sang API của Telegram
            restTemplate.postForEntity(url, requestBody, String.class);
            System.out.println("LOG INFO: Đã gửi thông báo thành công cho loài [" + insect.getName() + "] từ bộ dữ liệu Enum.");
        } catch (Exception e) {
            System.err.println("LOG ERROR: Không thể gửi tin nhắn đến Telegram API: " + e.getMessage());
        }
    }
}