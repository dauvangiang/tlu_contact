package com.mobile.group.tlu_contact_be.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.FirebaseApp;
import com.google.firebase.cloud.FirestoreClient;
import com.mobile.group.tlu_contact_be.model.User;
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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class UserService {

    private final Firestore db;

    public UserService(FirebaseApp firebaseApp) {
        this.db = FirestoreClient.getFirestore(firebaseApp);
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