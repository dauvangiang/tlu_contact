package com.mobile.group.tlu_contact_be.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mobile.group.tlu_contact_be.model.Staff;
import com.mobile.group.tlu_contact_be.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@RestController
@RequestMapping("/api/staff")
public class StaffController {

    private static final String UPLOAD_DIR = "uploads/";

    @Autowired
    private StaffService staffService;

    @GetMapping
    public ResponseEntity<List<Staff>> getAllStaff() {
        return ResponseEntity.ok(staffService.getAllStaff());
    }

    @GetMapping("/{staffId}")
    public ResponseEntity<Staff> getStaff(@PathVariable String staffId) {
        Staff staff = staffService.getStaff(staffId);
        return staff != null ? ResponseEntity.ok(staff) : ResponseEntity.notFound().build();
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> createStaff(@RequestPart("staff") String staffJson,
                                              @RequestParam(value = "file", required = false) MultipartFile file) {
        try {
            // Chuyển JSON thành đối tượng Staff
            ObjectMapper objectMapper = new ObjectMapper();
            Staff staff = objectMapper.readValue(staffJson, Staff.class);

            // Kiểm tra email và phone trước khi thêm vào Firestore
            if (staffService.existsByEmail(staff.getEmail())) {
                return ResponseEntity.badRequest().body("Email đã tồn tại!");
            }
            if (staffService.existsByPhone(staff.getPhone())) {
                return ResponseEntity.badRequest().body("Số điện thoại đã tồn tại!");
            }

            // Lưu file (nếu có)
            if (file != null && !file.isEmpty()) {
                String fileName = saveFile(file);
                staff.setPhotoURL("/uploads/" + fileName);
            }

            // Tạo nhân viên mới
            String staffId = staffService.createStaff(staff);
            return ResponseEntity.ok("Nhân viên được tạo thành công với ID: " + staffId);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Failed to save file");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }



    @PutMapping(value = "/{staffId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, Object>> updateStaff(@PathVariable String staffId,
                                                           @RequestPart("staff") String staffJson,
                                                           @RequestParam(value = "file", required = false) MultipartFile file) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Staff newStaffData = objectMapper.readValue(staffJson, Staff.class);

            // Kiểm tra xem nhân viên có tồn tại không
            Staff existingStaff = staffService.getStaff(staffId);
            if (existingStaff == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        Collections.singletonMap("error", "Nhân viên không tồn tại!"));
            }

            // Cập nhật các trường được gửi lên (chỉ thay đổi trường không null)
            if (newStaffData.getFullName() != null) {
                existingStaff.setFullName(newStaffData.getFullName());
            }
            if (newStaffData.getPosition() != null) {
                existingStaff.setPosition(newStaffData.getPosition());
            }
            if (newStaffData.getEmail() != null && !newStaffData.getEmail().equals(existingStaff.getEmail()) && staffService.existsByEmail(newStaffData.getEmail())) {
                return ResponseEntity.badRequest().body(Collections.singletonMap("error", "Email đã tồn tại!"));
            } else if (newStaffData.getEmail() != null) {
                existingStaff.setEmail(newStaffData.getEmail());
            }
            if (newStaffData.getPhone() != null && !newStaffData.getPhone().equals(existingStaff.getPhone()) && staffService.existsByPhone(newStaffData.getPhone())) {
                return ResponseEntity.badRequest().body(Collections.singletonMap("error", "Số điện thoại đã tồn tại!"));
            } else if (newStaffData.getPhone() != null) {
                existingStaff.setPhone(newStaffData.getPhone());
            }
            if (newStaffData.getUnit() != null) {
                existingStaff.setUnit(newStaffData.getUnit());
            }

            // Nếu có ảnh mới, cập nhật ảnh
            if (file != null && !file.isEmpty()) {
                String fileName = saveFile(file);
                existingStaff.setPhotoURL("/uploads/" + fileName);
            }

            // Cập nhật thông tin nhân viên
            staffService.updateStaff(staffId, existingStaff);

            // Phản hồi JSON thành công
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Cập nhật nhân viên thành công!");
            response.put("staff", existingStaff);
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    Collections.singletonMap("error", "Đã xảy ra lỗi khi cập nhật nhân viên!"));
        }
    }





    @DeleteMapping("/{staffId}")
    public ResponseEntity<Void> deleteStaff(@PathVariable String staffId) {
        staffService.deleteStaff(staffId);
        return ResponseEntity.noContent().build();
    }

    private String saveFile(MultipartFile file) throws IOException {
        if (!Files.exists(Paths.get(UPLOAD_DIR))) {
            Files.createDirectories(Paths.get(UPLOAD_DIR));
        }
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path filePath = Paths.get(UPLOAD_DIR + fileName);
        Files.write(filePath, file.getBytes());
        return fileName;
    }
}
