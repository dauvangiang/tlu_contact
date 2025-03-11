package com.mobile.group.tlu_contact_be.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.firebase.auth.UserRecord;
import com.mobile.group.tlu_contact_be.dto.constant.Role;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    String id;         // UID từ Firebase Authentication
    String displayName;
    String email;
    String phoneNumber;
    Role role;
    String photoURL;
    String staffId;     // Mã CBGV (nếu là CBGV)
    String studentId;   // Mã sinh viên (nếu là SV)
    String unit; //ID của unit, đơn vị trực thuộc
    @JsonIgnore
    String password;   // Không cần lưu trữ mật khẩu ở đây (chỉ dùng để đăng ký)
}