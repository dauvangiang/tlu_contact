package com.mobile.group.tlu_contact_be.dto.request.user;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateUserReq {
    @NotBlank
    String email;
    String displayName;
    String photoBase64;
    String phoneNumber;
    @NotBlank
    String password;
    @NotBlank
    String confirmPassword;

    @AssertTrue(message = "Password don't matching")
    public boolean isValid() {
        return password.equals(confirmPassword);
    }
}
