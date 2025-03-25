package com.mobile.group.tlu_contact_be.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.cloud.FirestoreClient;
import com.mobile.group.tlu_contact_be.dto.request.auth.UserLoginReq;
import com.mobile.group.tlu_contact_be.dto.request.user.CreateUserReq;
import com.mobile.group.tlu_contact_be.exceptions.CustomException;
import com.mobile.group.tlu_contact_be.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import com.mobile.group.tlu_contact_be.dto.constant.Role;

@Service
public class UserService {

    private final Firestore db;
    private final FirebaseAuth firebaseAuth;
    @Autowired
    private EmailService emailService;
    @Value("${firebase.api.key}")
    private String FIREBASE_API_KEY;

    public UserService(FirebaseApp firebaseApp) {
        this.db = FirestoreClient.getFirestore(firebaseApp);
        this.firebaseAuth = FirebaseAuth.getInstance();
    }

    public UserRecord register(CreateUserReq request) {
        if (checkEmailExists(request.getEmail())) {
            throw new CustomException("Email đã tồn tại.");
        }

        UserRecord.CreateRequest newUser = new UserRecord.CreateRequest()
                .setEmail(request.getEmail())
                .setPassword(request.getPassword())
                .setEmailVerified(false);
        try {
            UserRecord userRecord = firebaseAuth.createUser(newUser);
            saveUserToFirestore(userRecord.getUid(), request);
            String link = firebaseAuth.generateEmailVerificationLink(userRecord.getEmail());
            emailService.sendCustomEmail(request.getEmail(), link);

            return userRecord;
        } catch (Exception e) {
            throw new CustomException(e.getMessage());
        }
    }

    @Async
    void saveUserToFirestore(String uid, CreateUserReq request) {
        Role role = null;
        if(request.getEmail().endsWith("@tlu.edu.vn")){
            role = Role.STAFF;
        } else if (request.getEmail().endsWith("@e.tlu.edu.vn")){
            role = Role.STUDENT;
        } else {
            throw new CustomException("Địa chỉ email không hợp lệ.");
        }

        User user = User.builder()
                .id(uid)
                .email(request.getEmail())
                .displayName(request.getDisplayName())
                .phoneNumber(request.getPhoneNumber())
                .photoURL(request.getPhotoUrl())
                .role(role)
                .build();

        try {
            db.collection("users").document(uid).set(user).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new CustomException("Đã có lỗi xảy ra.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public Object login(UserLoginReq request) {
        String FIREBASE_LOGIN_URL = "https://identitytoolkit.googleapis.com/v1/accounts:signInWithPassword?key=" + FIREBASE_API_KEY;
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpEntity<UserLoginReq> reqHttpEntity = new HttpEntity<>(request);
            ResponseEntity<Object> response = restTemplate.exchange(
                    FIREBASE_LOGIN_URL,
                    HttpMethod.POST,
                    reqHttpEntity,
                    Object.class
            );

            return response.getBody();
        } catch (Exception ex) {
            throw new CustomException("Email hoặc mật khẩu không đúng.");
        }
    }

    private boolean checkEmailExists(String email) {
        try {
            UserRecord userRecord = firebaseAuth.getUserByEmail(email);
            return userRecord != null;
        } catch (FirebaseAuthException e) {
            return false;
        }
    }

    public User getUser(String id) {
        try {
            DocumentReference docRef = db.collection("users").document(id);
            ApiFuture<DocumentSnapshot> future = docRef.get();
            DocumentSnapshot document = future.get();
            if (document.exists()) {
                User user = document.toObject(User.class);
                user.setId(document.getId());  // Đảm bảo ID được gán
                return user;
            }
            return null;
        } catch (InterruptedException | ExecutionException e) {
            throw new CustomException("Failed to get user", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        try {
            ApiFuture<QuerySnapshot> future = db.collection("users").get();
            List<QueryDocumentSnapshot> documents = future.get().getDocuments();
            for (QueryDocumentSnapshot document : documents) {
                User user = document.toObject(User.class);
                user.setId(document.getId());  // Đảm bảo ID được gán
                users.add(user);
            }
            return users;
        } catch (InterruptedException | ExecutionException e) {
            throw new CustomException("Failed to get all users", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public void updateUser(String id, User user) {
        try {
            db.collection("users").document(id).set(user).get();  // Sử dụng UID làm ID
        } catch (InterruptedException | ExecutionException e) {
            throw new CustomException("Failed to update user", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public void deleteUser(String id) {
        try {
            db.collection("users").document(id).delete().get(); // Sử dụng UID làm ID
        } catch (InterruptedException | ExecutionException e) {
            throw new CustomException("Failed to delete user", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}