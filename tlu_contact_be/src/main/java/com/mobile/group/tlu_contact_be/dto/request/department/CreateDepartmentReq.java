package com.mobile.group.tlu_contact_be.dto.request.department;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateDepartmentReq {
    @NotBlank(message = "Tên đơn vị không được để trống.")
    String name;
    String address;
    String logoBase64;
    @NotBlank(message = "Số điện thoại không được để trống.")
    String phone;
    @NotBlank(message = "Email không được để trống.")
    String email;
    String parentDepartmentId;
    @NotBlank(message = "Loại đơn vị không được để trống")
    String typeId;
}
