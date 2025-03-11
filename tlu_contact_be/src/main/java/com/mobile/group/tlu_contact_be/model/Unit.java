package com.mobile.group.tlu_contact_be.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Unit {
    String id;
    String code;
    String name;
    String address;
    String logoURL;
    String phone;
    String email;
    String fax;
    String parentUnit;
    String type;
}
