package com.mobile.group.tlu_contact_be.model;

import lombok.Data;

@Data
public class User {
    private String id;
    private String displayName;
    private String email;
    private String phoneNumber;
    private String role;
    private String photoURL;
}