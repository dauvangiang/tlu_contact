package com.mobile.group.tlu_contact_be.dto.request.department;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateDepartmentReq {
    String name;
    String address;
    String logoUrl;
    String phone;
    String email;
    String fax;
    String parentDepartmentId;
    String type;
}
