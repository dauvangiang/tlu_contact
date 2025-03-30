package com.mobile.group.tlu_contact_be.dto.response.department;

import com.mobile.group.tlu_contact_be.model.Department;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.Map;

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
    String logoURL;
    String phone;
    String email;
    String fax;
    String type;
    Map<String, String> parentDepartment;
    List<Map<String, String>> dependentDepartments;

    public DepartmentRes(Department department, Map<String, String> parentDepartment, List<Map<String, String>> dependentDepartments) {
        this.code = department.getCode();
        this.name = department.getName();
        this.address = department.getAddress();
        this.logoURL = department.getLogoURL();
        this.phone = department.getPhone();
        this.email = department.getEmail();
        this.fax = department.getFax();
        this.type = department.getType();
        this.parentDepartment = parentDepartment;
        this.dependentDepartments = dependentDepartments;
    }
}
