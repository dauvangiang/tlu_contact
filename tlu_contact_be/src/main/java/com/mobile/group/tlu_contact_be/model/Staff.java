package com.mobile.group.tlu_contact_be.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Staff {
    String id;
    String staffId;
    String fullName;
    String position;
    String phone;
    String email;
    String photoURL;
    String unit;
}
