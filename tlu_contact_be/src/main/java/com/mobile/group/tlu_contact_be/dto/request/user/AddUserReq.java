package com.mobile.group.tlu_contact_be.dto.request.user;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AddUserReq {
    String email;
    String displayName;
    String photoUrl;
    String phoneNumber;
    String password;
}
