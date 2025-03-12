package com.mobile.group.tlu_contact_be.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.firebase.auth.UserRecord;
import com.mobile.group.tlu_contact_be.dto.constant.Role;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    String id;
    String displayName;
    String email;
    String phoneNumber;
    Role role;
    String photoURL;
    String unit;
    @JsonIgnore
    String password;
}