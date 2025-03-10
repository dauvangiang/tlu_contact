package com.mobile.group.tlu_contact_be.dto.response;

public record LoginResponse(String idToken, String refreshToken, String expiresIn) {}
