package com.mobile.group.tlu_contact_be.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.FirebaseApp;
import com.google.firebase.cloud.FirestoreClient;
import com.mobile.group.tlu_contact_be.exceptions.CustomException;
import com.mobile.group.tlu_contact_be.model.Staff;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class StaffService {

    private final Firestore db = FirestoreClient.getFirestore();

    public String createStaff(Staff staff) {
        try {
            if (existsByEmail(staff.getEmail())) {
                throw new RuntimeException("Email đã tồn tại!");
            }

            if (existsByPhone(staff.getPhone())) {
                throw new RuntimeException("Số điện thoại đã tồn tại!");
            }

            ApiFuture<WriteResult> future = db.collection("staffs").document(staff.getStaffId()).set(staff);
            future.get();
            return staff.getStaffId();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Failed to create staff", e);
        }
    }

    public Staff getStaff(String staffId) {
        try {
            DocumentReference docRef = db.collection("staffs").document(staffId);
            ApiFuture<DocumentSnapshot> future = docRef.get();
            DocumentSnapshot document = future.get();
            if (document.exists()) {
                return document.toObject(Staff.class);
            }
            throw new CustomException("CBGV không tồn tại.", HttpStatus.NOT_FOUND);
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Failed to get staff", e);
        }
    }

    public List<Staff> getAllStaff() {
        List<Staff> staffList = new ArrayList<>();
        try {
            ApiFuture<QuerySnapshot> future = db.collection("staffs").get();
            List<QueryDocumentSnapshot> documents = future.get().getDocuments();
            for (QueryDocumentSnapshot document : documents) {
                staffList.add(document.toObject(Staff.class));
            }
            return staffList;
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Failed to get all staff", e);
        }
    }

    public Staff updateStaff(String staffId, Staff request) {
        try {
            DocumentReference docRef = db.collection("staffs").document(staffId);
            ApiFuture<DocumentSnapshot> future = docRef.get();
            DocumentSnapshot document = future.get();

            if (!document.exists()) {
                throw new RuntimeException("Nhân viên không tồn tại!");
            }

            Staff existingStaff = document.toObject(Staff.class);
            if (existingStaff == null) {
                throw new CustomException("CBGV không tồn tại.", HttpStatus.NOT_FOUND);
            }

            if (request.getFullName() != null && !request.getFullName().isBlank()) {
                existingStaff.setFullName(request.getFullName());
            }
            if (request.getPosition() != null && !request.getPosition().isBlank()) {
                existingStaff.setPosition(request.getPosition());
            }
            if (request.getUnit() != null && !request.getUnit().isBlank()) {
                existingStaff.setUnit(request.getUnit());
            }
            if (request.getPhotoURL() != null && !request.getPhotoURL().isBlank()) {
                existingStaff.setPhotoURL(request.getPhotoURL());
            }

            if (!existingStaff.getEmail().equals(request.getEmail()) && existsByEmail(request.getEmail())) {
                throw new RuntimeException("Email đã tồn tại!");
            }
            if (!existingStaff.getPhone().equals(request.getPhone()) && existsByPhone(request.getPhone())) {
                throw new RuntimeException("Số điện thoại đã tồn tại!");
            }

            request.setStaffId(existingStaff.getStaffId());

            db.collection("staffs").document(staffId).set(request).get();

            return request;

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

    private boolean existsByEmail(String email) {
        try {
            ApiFuture<QuerySnapshot> future = db.collection("staff")
                    .whereEqualTo("email", email)
                    .limit(1)
                    .get();
            return !future.get().getDocuments().isEmpty();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Failed to check email existence", e);
        }
    }

    private boolean existsByPhone(String phone) {
        try {
            ApiFuture<QuerySnapshot> future = db.collection("staff")
                    .whereEqualTo("phone", phone)
                    .limit(1)
                    .get();
            return !future.get().getDocuments().isEmpty();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Failed to check phone existence", e);
        }
    }

}
