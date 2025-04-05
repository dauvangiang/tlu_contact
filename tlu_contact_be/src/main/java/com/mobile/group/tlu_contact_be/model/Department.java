package com.mobile.group.tlu_contact_be.model;

import com.mobile.group.tlu_contact_be.dto.request.department.CreateDepartmentReq;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Department {
    String code;
    String name;
    String address;
    String logoBase64;
    String phone;
    String email;
    String parentDepartmentId;
    String typeId;
    Boolean deleted = false;
}
