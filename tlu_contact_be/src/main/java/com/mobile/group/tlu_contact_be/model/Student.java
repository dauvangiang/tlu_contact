package com.mobile.group.tlu_contact_be.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Student {
    String studentId;
    String fullName;
    String photoURL;
    String phone;
    String email;
    String address;
    String className;
    String departmentId;
    String userID;
    Boolean deleted = false;
}