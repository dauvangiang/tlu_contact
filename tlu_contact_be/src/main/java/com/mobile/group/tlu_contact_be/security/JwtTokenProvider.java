package com.mobile.group.tlu_contact_be.security;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.mobile.group.tlu_contact_be.exceptions.CustomException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {
    public static class FirebaseTokenProvider {
        public static String getUidFromToken(String token) throws FirebaseAuthException {
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(token);
            if (!decodedToken.isEmailVerified()) {
                throw new CustomException("Email chưa được xác thực.", HttpStatus.UNAUTHORIZED);
            }
            return decodedToken.getUid();
        }
    }
}
