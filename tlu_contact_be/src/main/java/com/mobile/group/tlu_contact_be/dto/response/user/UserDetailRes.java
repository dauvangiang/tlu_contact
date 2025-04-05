package com.mobile.group.tlu_contact_be.dto.response.user;

import com.mobile.group.tlu_contact_be.dto.constant.Role;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDetailRes {
    String id;
    String email;
    Object contact;
    Role role;
}
