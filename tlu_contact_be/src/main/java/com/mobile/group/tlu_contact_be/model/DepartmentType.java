package com.mobile.group.tlu_contact_be.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DepartmentType {
    String id;
    String name;
    Boolean deleted = false;

    public DepartmentType(String id, String name) {
        this.id = id;
        this.name = name;
    }
}
