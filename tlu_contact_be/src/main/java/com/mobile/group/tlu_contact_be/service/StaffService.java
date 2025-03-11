package com.mobile.group.tlu_contact_be.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.FirebaseApp;
import com.google.firebase.cloud.FirestoreClient;
import com.mobile.group.tlu_contact_be.model.Staff;
import com.mobile.group.tlu_contact_be.model.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class StaffService {

    private final Firestore db;

    public StaffService(FirebaseApp firebaseApp) {
        this.db = FirestoreClient.getFirestore();
    }

    public String createStaff(Staff staff) {
        try {
            ApiFuture<WriteResult> future = db.collection("staff").document(staff.getStaffId()).set(staff);
            future.get();
            return staff.getStaffId();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Failed to create staff", e);
        }
    }

    public Staff getStaff(String staffId) {
        try {
            DocumentReference docRef = db.collection("staff").document(staffId);
            ApiFuture<DocumentSnapshot> future = docRef.get();
            DocumentSnapshot document = future.get();
            if (document.exists()) {
                return document.toObject(Staff.class);
            }
            return null;
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Failed to get staff", e);
        }
    }

    public List<Staff> getAllStaff() {
        List<Staff> staffList = new ArrayList<>();
        try {
            ApiFuture<QuerySnapshot> future = db.collection("staff").get();
            List<QueryDocumentSnapshot> documents = future.get().getDocuments();
            for (QueryDocumentSnapshot document : documents) {
                staffList.add(document.toObject(Staff.class));
            }
            return staffList;
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Failed to get all staff", e);
        }
    }

    public void updateStaff(String staffId, Staff staff) {
        try {
            db.collection("staff").document(staffId).set(staff).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Failed to update staff", e);
        }
    }

    public void deleteStaff(String staffId) {
        try {
            db.collection("staff").document(staffId).delete().get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Failed to delete staff", e);
        }
    }
}