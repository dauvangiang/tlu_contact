package com.mobile.group.tlu_contact_be.dto.request.auth;

import lombok.Data;

@Data
public class UserLoginReq {
    private String email;
    private String password;
    private final boolean returnSecureToken = true;
}