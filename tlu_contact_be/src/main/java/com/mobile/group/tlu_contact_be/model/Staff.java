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
public class Staff {
    String staffId;
    String fullName;
    String position;
    String phone;
    String email;
    String photoBase64;
    List<String> departmentIds;
    String userID;
    Boolean deleted = false;
}
