package com.mobile.group.tlu_contact_be.dto.response.department;

import com.mobile.group.tlu_contact_be.dto.request.department.CreateDepartmentReq;
import com.mobile.group.tlu_contact_be.model.Department;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DepartmentRes {
    String code;
    String name;
    String address;
    String logoBase64;
    String phone;
    String email;
    String typeId;
    String parentDepartmentId;

    public DepartmentRes(Department department, String parentDepartmentId) {
        this.code = department.getCode();
        this.name = department.getName();
        this.address = department.getAddress();
        this.logoBase64 = department.getLogoBase64();
        this.phone = department.getPhone();
        this.email = department.getEmail();
        this.typeId = department.getTypeId();
        this.parentDepartmentId = parentDepartmentId;
    }
}
