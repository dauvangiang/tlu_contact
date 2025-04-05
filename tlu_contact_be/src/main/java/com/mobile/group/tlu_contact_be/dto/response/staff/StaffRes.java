package com.mobile.group.tlu_contact_be.dto.response.staff;

import com.mobile.group.tlu_contact_be.model.Department;
import com.mobile.group.tlu_contact_be.model.Staff;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StaffRes {
    String staffId;
    String fullName;
    String position;
    String phone;
    String email;
    String photoBase64;
    List<Map<String, String>> departments;
    String userID;

    public StaffRes(Staff staff, List<Department> departments) {
        this.staffId = staff.getStaffId();
        this.fullName = staff.getFullName();
        this.position = staff.getPosition();
        this.phone = staff.getPhone();
        this.email = staff.getEmail();
        this.photoBase64 = staff.getPhotoBase64();
        this.userID = staff.getUserID();
        
        if (departments != null) {
            this.departments = departments.stream()
                    .map(dept -> Map.of(
                            "code", dept.getCode(),
                            "name", dept.getName()
                    ))
                    .collect(Collectors.toList());
        }
    }
} 