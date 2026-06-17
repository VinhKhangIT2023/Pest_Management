package com.example.pest_management;

public enum PestInfo {
    // 1. Rầy nâu
    RAY_NAU(
            "Rầy nâu",
            "Nilaparvata lugens",
            "Chích hút nhựa ở gốc thân cây lúa làm cây bị khô héo, khi mật độ cao gây ra hiện tượng 'cháy rầy'. Đồng thời là môi giới truyền các bệnh virus nguy hiểm như lúa lùn xoắn lá, lùn sọc đen.",
            "Trồng giống kháng rầy, thực hiện sạ thưa, bón phân cân đối (tránh thừa đạm). Khi mật độ rầy cao, giữ mực nước ruộng để che chắn gốc và phun các loại thuốc đặc trị như Chess, P chess, bọ rầy theo nguyên tắc 4 đúng."
    ),

    // 2. Rầy lưng trắng
    RAY_LUNG_TRANG(
            "Rầy lưng trắng",
            "Sogatella furcifera",
            "Sâu non và trưởng thành chích hút nhựa lá và thân cây lúa làm lá bị vàng, cây còi cọc. Nguy hại hơn, đây là môi giới chính truyền virus gây bệnh lùn sọc đen phương Nam, có thể làm thất thu hoàn toàn năng suất.",
            "Vệ sinh bờ ruộng, bẫy đèn theo dõi lịch di cư để xuống giống né rầy. Sử dụng các loại thuốc nội hấp, lưu dẫn để phun trừ khi mật độ rầy tuổi nhỏ (tuổi 1-3) vượt ngưỡng kinh tế."
    ),

    // 3. Rầy xanh đuôi đen (Trong bảng ghi: Rầy xanh hại lúa)
    RAY_XANH_HAI_LUA(
            "Rầy xanh hại lúa",
            "Nephotettix virescens",
            "Trực tiếp chích hút nhựa lá lúa làm đầu lá bị khô cháy (tước lá). Là tác nhân chính truyền bệnh virus vàng lụi (tungro) và bệnh lá lúa lùn vàng, khiến cây lúa không thể trỗ bông.",
            "Bảo vệ các loài thiên địch tự nhiên như nhện nước, bọ xít mù xanh. Không phun thuốc hóa học quá sớm ở đầu vụ để tránh mất cân bằng sinh thái; chỉ phun thuốc khi xuất hiện triệu chứng bệnh virus lan truyền."
    ),

    // 4. Sâu cuốn lá nhỏ
    SAU_CUON_LA_NHO(
            "Sâu cuốn lá nhỏ",
            "Cnaphalocrocis medinalis",
            "Sâu non nhả tơ cuốn lá lúa thành bao và nằm bên trong cắn phá phần xanh (diệp lục), để lại lớp màng trắng. Làm giảm nghiêm trọng diện tích quang hợp, khiến hạt bị lép, bông ngắn và giảm năng suất.",
            "Tăng cường bón phân kali để lá lúa cứng sừng, hạn chế sâu đục. Bảo vệ ong ký sinh trứng và nhộng sâu. Chỉ phun các loại thuốc gốc sinh học hoặc điều hòa sinh trưởng (như Radiant, Voliam Targo) khi mật độ sâu vượt ngưỡng ở giai đoạn làm đòng."
    ),

    // 5. Sâu đục thân 2 chấm
    SAU_DUC_THAN_2_CHAM(
            "Sâu đục thân 2 chấm",
            "Scirpophaga incertulas",
            "Sâu non đục qua màng bẹ vào bên trong thân cây, cắt đứt mạch dẫn dinh dưỡng. Ở giai đoạn đẻ nhánh gây ra hiện tượng 'dảnh héo'; ở giai đoạn làm đòng/trỗ bông gây ra hiện tượng 'bông bạc' (hạt lép trắng toàn bộ).",
            "Cày lật gốc rạ sau thu hoạch để tiêu diệt nhộng ẩn nấp. Thu gom và ngắt ổ trứng sâu trên lá mạ. Sử dụng các loại thuốc dạng hạt rải vào gốc ruộng (như Virtako, Basudin) vào thời điểm bướm rộ từ 5-7 ngày."
    ),

    // 6. Muỗi hành (Sâu hại bông lúa)
    MUOI_HANH(
            "Muỗi hành",
            "Orseolia oryzae",
            "Ấu trùng muỗi đục vào điểm sinh trưởng của cây lúa, tiết ra chất độc kích thích làm bẹ lá phát triển bất thường thành dạng ống tròn màu trắng xanh (giống như lá hành), cây lúa bị nghẹn không thể trỗ bông.",
            "Giữ ruộng sạch cỏ dại (nơi lưu trú của muỗi). Bón phân cân đối đạm - lân - kali. Do muỗi hành nằm sâu trong ống hành nên các loại thuốc phun thông thường ít hiệu quả, cần sử dụng thuốc có tính lưu dẫn, xông hơi mạnh hoặc thuốc hạt rải ruộng."
    ),

    // 7. Bọ xít dài (Trong bảng ghi: Vòi voi hại lúa)
    VOI_VOI_HAI_LUA(
            "Vòi voi hại lúa",
            "Leptocorisa acuta",
            "Tập trung chích hút hạt lúa ở giai đoạn ngậm sữa (vừa ngậm hạt). Hạt lúa bị hút sẽ để lại các vết chấm đen, hạt bị lép lửng, biến dạng, gạo bị thối đen hoặc dễ vỡ vụn khi xay xát.",
            "Vệ sinh sạch cỏ bờ vì đây là ký chủ phụ của bọ xít. Tiến hành gieo cấy đồng loạt để né tránh thời điểm nhạy cảm. Khi bọ xít xuất hiện rộ lúc lúa trỗ-ngậm sữa, phun thuốc vào lúc sáng sớm hoặc chiều mát khi chúng ít di chuyển."
    ),

    // 8. Ngài bướm đêm (Trong bảng ghi: Ngài bướm đêm hại lúa)
    NGAI_BUOM_DEM(
            "Ngài bướm đêm hại lúa",
            "Spodoptera frugiperda",
            "Sâu non (sâu keo mùa thu) có sức cắn phá cực mạnh, ăn khuyết lá, cắn cụt ngọn lúa non, làm xơ xác toàn bộ ruộng lúa chỉ trong thời gian ngắn, đặc biệt gây hại nặng ở giai đoạn mạ và lúa đẻ nhánh.",
            "Làm đất kỹ, phơi ải để diệt nhộng trong đất. Đặt bẫy bướm (bẫy chua ngọt hoặc bẫy đèn). Phun thuốc hóa học thế hệ mới vào lúc chiều tối (khi sâu bò ra ăn) và đảm bảo phun ướt đều cả hai mặt lá."
    );

    private final String commonName;
    private final String scientificName;
    private final String damage;
    private final String solution;

    PestInfo(String commonName, String scientificName, String damage, String solution) {
        this.commonName = commonName;
        this.scientificName = scientificName;
        this.damage = damage;
        this.solution = solution;
    }

    public static PestInfo fromLabel(String label) {
        if (label == null) return null;
        try {
            // Chuẩn hóa chuỗi đầu vào: xóa khoảng trắng, chuyển về chữ in hoa để so khớp cấu trúc định danh
            // Ví dụ: "Ray_nau" hoặc "ray_nau" -> "RAY_NAU"
            return PestInfo.valueOf(label.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    // Các hàm Getter dùng để trích xuất dữ liệu
    public String getCommonName() { return commonName; }
    public String getScientificName() { return scientificName; }
    public String getDamage() { return damage; }
    public String getSolution() { return solution; }
}