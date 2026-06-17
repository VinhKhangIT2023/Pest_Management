# 🌾 PEST AI - Hệ Thống Quản Lý & Chẩn Đoán Dịch Hại Nông Nghiệp

**PEST AI** là một hệ thống web ứng dụng Trí Tuệ Nhân Tạo (AI) vào lĩnh vực nông nghiệp. Hệ thống không chỉ cung cấp nền tảng thương mại điện tử để mua bán các loại thuốc bảo vệ thực vật, mà còn tích hợp mô hình Deep Learning giúp người nông dân tải ảnh lên để tự động chẩn đoán bệnh trên lúa và nhận đề xuất thuốc đặc trị tương ứng.

Dự án được thiết kế theo kiến trúc Microservices, tách biệt hoàn toàn luồng xử lý giao diện/nghiệp vụ và luồng xử lý AI.

## 🚀 Công nghệ & Kiến trúc
* **Core Backend:** Java, Spring Boot, Spring Security, Spring Data JPA.
* **AI Service:** Python, Flask, Ultralytics YOLOv8 (Mô hình nhận diện hình ảnh).
* **Database:** MySQL.
* **Frontend:** HTML5, CSS3, JavaScript (kết hợp AJAX/Fetch API), Bootstrap, Thymeleaf.
* **Giao tiếp liên dịch vụ:** RESTful API (RestTemplate).

---

## 📁 Cấu trúc thư mục

Dự án bao gồm 2 phân hệ độc lập chạy song song:

```text
📦 PEST-AI-Project
 ┣ 📂 API_Server          # Microservice đảm nhận việc phân tích ảnh bằng AI
 ┃ ┣ 📜 FlaskAPI.py       # Điểm vào (Entry point) khởi chạy server Python
 ┃ ┗ 📜 yolov8n_pest_model_v2_final.pt # Trọng số mô hình AI đã huấn luyện
 ┣ 📂 pest-management     # Core Service quản lý giao diện, người dùng và đơn hàng
 ┃ ┣ 📂 src               # Mã nguồn Java (Controller, Service, Repository, Entity)
 ┃ ┣ 📂 src/main/resources/templates # Các file giao diện HTML (Thymeleaf)
 ┃ ┗ 📜 pom.xml           # Tệp cấu hình thư viện Maven
 ┗ 📜 README.md           # Tài liệu hướng dẫn dự án
```

(Lưu ý: Thư mục `uploads/` chứa ảnh sản phẩm/avatar sẽ tự động được hệ thống tạo ra khi khởi chạy).

## ⚙️ Hướng dẫn cài đặt & Khởi chạy

Để chạy dự án này trên máy cá nhân (Localhost), hãy thực hiện tuần tự các bước sau:

### 1. Yêu cầu hệ thống (Prerequisites)
Đảm bảo máy tính của bạn đã cài đặt các phần mềm sau:
- Java Development Kit (JDK) 17 trở lên.
- Python 3.9 trở lên.
- MySQL Server 8.0 trở lên.
- Maven (Nên sử dụng các IDE tích hợp sẵn như IntelliJ IDEA hoặc Eclipse).

### 2. Thiết lập Cơ sở dữ liệu
Khởi động MySQL Server và tạo một schema (database) trống với tên: `pest_management`.

Truy cập vào thư mục cấu hình của Backend: `pest-management/src/main/resources/`.

Tìm file `application.properties.example`, đổi tên file này thành `application.properties`.

Mở file vừa đổi tên và thay đổi thông tin username và password cho khớp với cấu hình MySQL trên máy của bạn:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/pest_management
spring.datasource.username=root
spring.datasource.password=mat_khau_mysql_cua_ban
```

(Hệ thống sử dụng Hibernate `ddl-auto=update`, tự động ánh xạ bảng và tạo dữ liệu giả lập (Seed Data) cho bệnh và thuốc ở lần chạy đầu tiên).

### 3. Khởi chạy AI Microservice (Flask)
Mở cửa sổ Terminal / Command Prompt, di chuyển vào thư mục `API_Server`:

```bash
cd API_Server
```

Cài đặt các thư viện Python theo yêu cầu:

```bash
pip install flask ultralytics werkzeug
```

Chạy máy chủ nhận diện AI:

```bash
python FlaskAPI.py
```

Lưu ý: Nếu Terminal hiển thị `Running on http://0.0.0.0:5000/` là đã thành công. Phải luôn giữ cửa sổ Terminal này mở để hệ thống có thể nhận ảnh từ Backend gửi sang.

### 4. Khởi chạy Core Backend (Spring Boot)
- Mở thư mục `pest-management` bằng IntelliJ IDEA.
- Đợi Maven tải xuống các thư viện phụ thuộc (Sync Dependencies).
- Tìm và chạy file `PestManagementApplication.java`.
- Máy chủ sẽ bắt đầu lắng nghe ở cổng mặc định **8080**.

## 💡 Hướng dẫn sử dụng

Mở trình duyệt web và truy cập: http://localhost:8080/

Để kiểm thử các luồng quản trị (Thêm/Xóa sản phẩm, Duyệt đơn hàng, Xem thống kê), hãy đăng nhập bằng tài khoản Quản trị viên tối cao (Root Admin) đã được hệ thống khởi tạo sẵn:
- **Tài khoản:** admingoc
- **Mật khẩu:** 123456

Để kiểm thử luồng khách hàng (Chụp ảnh nhận diện bệnh, Thêm vào giỏ hàng), bạn có thể đăng xuất và tự đăng ký một tài khoản khách hàng mới.

## 👤 Tác giả

Nguyễn Đặng Vĩnh Khang - Sinh viên chuyên ngành Công nghệ Thông tin - Trường Đại học Công Thương TP.HCM (HUIT).