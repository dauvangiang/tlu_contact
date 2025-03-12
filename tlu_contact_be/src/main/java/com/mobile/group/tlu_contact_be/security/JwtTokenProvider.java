package com.mobile.group.tlu_contact_be.security;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {
    public static class FirebaseTokenProvider {
        public static String getUidFromToken(String token) throws FirebaseAuthException {
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(token);
            return decodedToken.getUid();
        }
    }
}
