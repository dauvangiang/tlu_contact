package com.mobile.group.tlu_contact_be.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.cloud.FirestoreClient;
import com.mobile.group.tlu_contact_be.dto.request.auth.UserLoginReq;
import com.mobile.group.tlu_contact_be.dto.request.user.AddUserReq;
import com.mobile.group.tlu_contact_be.dto.response.user.UserDetailRes;
import com.mobile.group.tlu_contact_be.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

//@Service
//public class UserService {
//
//    private final Firestore db;
//
//    public UserService() {
//        this.db = FirestoreClient.getFirestore();
//    }
//
//    // Create
//    public String createUser(User user) throws ExecutionException, InterruptedException {
//        ApiFuture<DocumentReference> future = db.collection("users").add(user);
//        return future.get().getId();
//    }
//
//    // Read (lấy một user)
//    public User getUser(String id) throws ExecutionException, InterruptedException {
//        DocumentReference docRef = db.collection("users").document(id);
//        ApiFuture<DocumentSnapshot> future = docRef.get();
//        DocumentSnapshot document = future.get();
//        if (document.exists()) {
//            return document.toObject(User.class);
//        }
//        return null;
//    }
//
//    // Read (lấy tất cả users)
//    public List<User> getAllUsers() throws ExecutionException, InterruptedException {
//        ApiFuture<QuerySnapshot> future = db.collection("users").get();
//        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
//        List<User> users = new ArrayList<>();
//        for (QueryDocumentSnapshot document : documents) {
//            User user = document.toObject(User.class);
//            user.setId(document.getId());
//            users.add(user);
//        }
//        return users;
//    }
//
//    // Update
//    public void updateUser(String id, User user) throws ExecutionException, InterruptedException {
//        db.collection("users").document(id).set(user);
//    }
//
//    // Delete
//    public void deleteUser(String id) {
//        db.collection("users").document(id).delete();
//    }
//}

import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;
import com.mobile.group.tlu_contact_be.model.User;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class UserService {

    private final Firestore db;
    private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    @Value("${firebase.api.key}")
    private String FIREBASE_API_KEY;

    public UserService(FirebaseApp firebaseApp) {
        this.db = FirestoreClient.getFirestore(firebaseApp);
    }

    public UserRecord register(AddUserReq request) {
        if (checkEmailExists(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

//        User newUser = User.builder()
//                .email(request.getEmail())
//                .password(request.getPassword()) //Nhớ mã hóa mật khẩu
//                .displayName(request.getDisplayName())
//                .phoneNumber(request.getPhoneNumber())
//                .photoURL(request.getPhotoUrl())
//                .build();

        UserRecord.CreateRequest newUser = new UserRecord.CreateRequest()
                .setEmail(request.getEmail())
                .setPassword(request.getPassword())
                .setDisplayName(request.getDisplayName())
                .setPhoneNumber(request.getPhoneNumber())
                .setPhotoUrl(request.getPhotoUrl());

        try {
            UserRecord userRecord = firebaseAuth.createUser(newUser);
            return userRecord;
        } catch (FirebaseAuthException e) {
            throw new RuntimeException(e);
        }
    }

    public Object login(UserLoginReq request) {
        String FIREBASE_LOGIN_URL = "https://identitytoolkit.googleapis.com/v1/accounts:signInWithPassword?key=" + FIREBASE_API_KEY;
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<UserLoginReq> reqHttpEntity = new HttpEntity<>(request);
        ResponseEntity<Object> response = restTemplate.exchange(
                FIREBASE_LOGIN_URL,
                HttpMethod.POST,
                reqHttpEntity,
                Object.class
        );

        return response.getBody();
    }

    private boolean checkEmailExists(String email) {
        try {
            UserRecord userRecord = firebaseAuth.getUserByEmail(email);
            return userRecord != null;
        } catch (FirebaseAuthException e) {
            return false;
        }
    }






    // Create
    public String createUser(User user) throws ExecutionException, InterruptedException {
        ApiFuture<DocumentReference> future = db.collection("users").add(user);
        return future.get().getId();
    }

    // Read (lấy một user)
    public User getUser(String id) throws ExecutionException, InterruptedException {
        DocumentReference docRef = db.collection("users").document(id);
        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot document = future.get();
        if (document.exists()) {
            return document.toObject(User.class);
        }
        return null;
    }

    // Read (lấy tất cả users)
    public List<User> getAllUsers() throws ExecutionException, InterruptedException {
        ApiFuture<QuerySnapshot> future = db.collection("users").get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        List<User> users = new ArrayList<>();
        for (QueryDocumentSnapshot document : documents) {
            User user = document.toObject(User.class);
            user.setId(document.getId());
            users.add(user);
        }
        return users;
    }

    // Update
    public void updateUser(String id, User user) throws ExecutionException, InterruptedException {
        db.collection("users").document(id).set(user);
    }

    // Delete
    public void deleteUser(String id) {
        db.collection("users").document(id).delete();
    }
}