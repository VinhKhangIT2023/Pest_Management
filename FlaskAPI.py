from flask import Flask, request
from ultralytics import YOLO
import os

app = Flask(__name__)

# ==========================================
# 1. TẢI MÔ HÌNH CỦA BẠN VÀO BỘ NHỚ
# Đã đổi tên file thành file bạn vừa upload
# ==========================================
MODEL_PATH = "yolov8n_pest_model_v2_final.pt" 

if os.path.exists(MODEL_PATH):
    model = YOLO(MODEL_PATH)
    print(f"=== [HỆ THỐNG AI] Đã tải thành công mô hình {MODEL_PATH} ===")
else:
    print(f"=== [CẢNH BÁO] Không tìm thấy file {MODEL_PATH}! Vui lòng copy file này để chung thư mục với FlaskAPI.py ===")

# ==========================================
# 2. TỪ ĐIỂN ÁNH XẠ LỚP (CLASS MAPPING)
# ==========================================
# Lưu ý: Thứ tự 0 đến 7 này phải khớp với thứ tự các nhãn (classes) 
# mà bạn đã định nghĩa trong file data.yaml lúc train mô hình.
CLASS_MAPPING = {
    0: "sau_cuon_la_nho",
    1: "ngai_buom_dem",
    2: "sau_duc_than_2_cham",
    3: "muoi_hanh",
    4: "ray_nau",
    5: "ray_lung_trang",
    6: "voi_voi_hai_lua",
    7: "ray_xanh_hai_lua"
}

@app.route('/predict', methods=['POST'])
def predict():
    if 'file' not in request.files:
        print("[LỖI] Request không chứa file")
        return "unknown", 400
        
    file = request.files['file']
    if file.filename == '':
        return "unknown", 400

    # Lưu file tạm để đưa vào model
    temp_filepath = "temp_prediction_image.jpg"
    file.save(temp_filepath)
    
    try:
        # Chạy AI dự đoán (Độ tin cậy > 25%)
        results = model(temp_filepath, conf=0.25)
        
        if len(results[0].boxes) > 0:
            # Lấy object tự tin nhất
            first_box = results[0].boxes[0]
            class_id = int(first_box.cls[0].item())
            confidence = float(first_box.conf[0].item())
            
            # Map sang Slug để gửi về Spring Boot
            predicted_slug = CLASS_MAPPING.get(class_id, "unknown")
            
            print(f"[AI PHÁT HIỆN]: Nhãn ID = {class_id} -> Slug = '{predicted_slug}' (Độ tin cậy: {confidence:.2f})")
            
            if os.path.exists(temp_filepath):
                os.remove(temp_filepath)
                
            return predicted_slug
            
        else:
            print("[AI THÔNG BÁO]: Không phát hiện thấy dịch hại.")
            if os.path.exists(temp_filepath):
                os.remove(temp_filepath)
            return "unknown"
            
    except Exception as e:
        print(f"[LỖI]: {str(e)}")
        if os.path.exists(temp_filepath):
            os.remove(temp_filepath)
        return "unknown"

if __name__ == '__main__':
    # Chạy API ở port 5000
    app.run(host='0.0.0.0', port=5000, debug=True)