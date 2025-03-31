package com.mobile.group.tlu_contact_be.security.interceptor;

import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.database.annotations.NotNull;
import com.mobile.group.tlu_contact_be.exceptions.CustomException;
import com.mobile.group.tlu_contact_be.model.User;
import com.mobile.group.tlu_contact_be.security.JwtTokenProvider;
import com.mobile.group.tlu_contact_be.security.SecurityContexts;
import com.mobile.group.tlu_contact_be.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class UserInterceptor implements HandlerInterceptor {
    
    private final UserService userService;

    @Override
    public boolean preHandle(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) throws Exception {
        CustomException exception = new CustomException("login_required", HttpStatus.UNAUTHORIZED);
        String vendorCode = request.getHeader("Authorization");
        if (Strings.isEmpty(vendorCode)) {
            throw exception;
        }
        String[] header = vendorCode.split(" ");
        if (header.length != 2) {
            throw exception;
        }
        String token = header[1];
        try {
            String uid = JwtTokenProvider.FirebaseTokenProvider.getUidFromToken(token);
            SecurityContexts.newContext();
            User user = userService.getUser(uid);
            SecurityContexts.getContext().setData(user);
            return true;
        } catch (FirebaseAuthException ex) {
            throw exception;
        }
    }
}
