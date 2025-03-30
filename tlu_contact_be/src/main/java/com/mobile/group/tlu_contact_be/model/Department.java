package com.mobile.group.tlu_contact_be.model;

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
    String logoURL;
    String phone;
    String email;
    String fax;
    String parentDepartmentId;
    String type;
    List<String> dependentDepartmentIds;
    Boolean deleted = false;
}
