//package com.mobile.group.tlu_contact_be.service;
//
//import com.google.api.core.ApiFuture;
//import com.google.cloud.firestore.*;
//import com.google.firebase.FirebaseApp;
//import com.google.firebase.cloud.FirestoreClient;
//import com.mobile.group.tlu_contact_be.model.Staff;
//import org.springframework.stereotype.Service;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.concurrent.ExecutionException;
//
//@Service
//public class StaffService {
//
//    private final Firestore db;
//    public StaffService(FirebaseApp firebaseApp) {
//        this.db = FirestoreClient.getFirestore();
//    }
//
//    public String createStaff(Staff staff) {
//        try {
//            ApiFuture<WriteResult> future = db.collection("staff").document(staff.getStaffId()).set(staff);
//            future.get();
//            return staff.getStaffId();
//        } catch (InterruptedException | ExecutionException e) {
//            throw new RuntimeException("Failed to create staff", e);
//        }
//    }
//
//    public Staff getStaff(String staffId) {
//        try {
//            DocumentReference docRef = db.collection("staff").document(staffId);
//            ApiFuture<DocumentSnapshot> future = docRef.get();
//            DocumentSnapshot document = future.get();
//            if (document.exists()) {
//                return document.toObject(Staff.class);
//            }
//            return null;
//        } catch (InterruptedException | ExecutionException e) {
//            throw new RuntimeException("Failed to get staff", e);
//        }
//    }
//
//    public List<Staff> getAllStaff() {
//        List<Staff> staffList = new ArrayList<>();
//        try {
//            ApiFuture<QuerySnapshot> future = db.collection("staff").get();
//            List<QueryDocumentSnapshot> documents = future.get().getDocuments();
//            for (QueryDocumentSnapshot document : documents) {
//                staffList.add(document.toObject(Staff.class));
//            }
//            return staffList;
//        } catch (InterruptedException | ExecutionException e) {
//            throw new RuntimeException("Failed to get all staff", e);
//        }
//    }
//
//    public Staff updateStaff(String staffId, Staff staff) {
//        try {
//            db.collection("staff").document(staffId).set(staff).get();
//        } catch (InterruptedException | ExecutionException e) {
//            throw new RuntimeException("Failed to update staff", e);
//        }
//        return staff;
//    }
//
//    public void deleteStaff(String staffId) {
//        try {
//            db.collection("staff").document(staffId).delete().get();
//        } catch (InterruptedException | ExecutionException e) {
//            throw new RuntimeException("Failed to delete staff", e);
//        }
//    }
//
//
//}
package com.mobile.group.tlu_contact_be.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import com.mobile.group.tlu_contact_be.model.Staff;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class StaffService {
    private final Firestore db = FirestoreClient.getFirestore();

    /**
     * Kiểm tra xem email đã tồn tại hay chưa.
     */
    public boolean existsByEmail(String email) {
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

    /**
     * Kiểm tra xem số điện thoại đã tồn tại hay chưa.
     */
    public boolean existsByPhone(String phone) {
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

    /**
     * Tạo nhân viên mới sau khi kiểm tra trùng lặp email và số điện thoại.
     */
    public String createStaff(Staff staff) {
        try {
            // Kiểm tra email trùng
            if (existsByEmail(staff.getEmail())) {
                throw new RuntimeException("Email đã tồn tại!");
            }

            // Kiểm tra số điện thoại trùng
            if (existsByPhone(staff.getPhone())) {
                throw new RuntimeException("Số điện thoại đã tồn tại!");
            }

            // Thêm nhân viên vào Firestore
            ApiFuture<WriteResult> future = db.collection("staff").document(staff.getStaffId()).set(staff);
            future.get();
            return staff.getStaffId();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Failed to create staff", e);
        }
    }

    /**
     * Lấy thông tin nhân viên theo ID.
     */
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

    /**
     * Lấy danh sách tất cả nhân viên.
     */
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

    /**
     * Cập nhật thông tin nhân viên.
     */
    public Staff updateStaff(String staffId, Staff newStaffData) {
        try {
            DocumentReference docRef = db.collection("staff").document(staffId);
            ApiFuture<DocumentSnapshot> future = docRef.get();
            DocumentSnapshot document = future.get();

            if (!document.exists()) {
                throw new RuntimeException("Nhân viên không tồn tại!");
            }

            // Lấy thông tin nhân viên cũ
            Staff existingStaff = document.toObject(Staff.class);

            // Kiểm tra email có bị trùng không (trừ nhân viên hiện tại)
            if (!existingStaff.getEmail().equals(newStaffData.getEmail()) && existsByEmail(newStaffData.getEmail())) {
                throw new RuntimeException("Email đã tồn tại!");
            }

            // Kiểm tra số điện thoại có bị trùng không (trừ nhân viên hiện tại)
            if (!existingStaff.getPhone().equals(newStaffData.getPhone()) && existsByPhone(newStaffData.getPhone())) {
                throw new RuntimeException("Số điện thoại đã tồn tại!");
            }

            // Giữ nguyên staffId cũ
            newStaffData.setStaffId(existingStaff.getStaffId());

            // Cập nhật dữ liệu (trừ staffId)
            db.collection("staff").document(staffId).set(newStaffData).get();
            return newStaffData;
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Đã xảy ra lỗi khi cập nhật nhân viên!", e);
        }
    }


    /**
     * Xóa nhân viên theo ID.
     */
    public void deleteStaff(String staffId) {
        try {
            db.collection("staff").document(staffId).delete().get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Failed to delete staff", e);
        }
    }
}
