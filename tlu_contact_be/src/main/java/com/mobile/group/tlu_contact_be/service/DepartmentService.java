package com.mobile.group.tlu_contact_be.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.FirebaseApp;
import com.google.firebase.cloud.FirestoreClient;
import com.mobile.group.tlu_contact_be.dto.constant.Type;
import com.mobile.group.tlu_contact_be.dto.request.department.CreateDepartmentReq;
import com.mobile.group.tlu_contact_be.dto.request.department.UpdateDepartmentRep;
import com.mobile.group.tlu_contact_be.exceptions.CustomException;
import com.mobile.group.tlu_contact_be.model.Department;
import com.mobile.group.tlu_contact_be.model.Staff;
import com.mobile.group.tlu_contact_be.utils.Utils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
public class DepartmentService {

    private final Firestore db = FirestoreClient.getFirestore();

    public Department createDepartment(CreateDepartmentReq request) {
        try {
            Department department = Department.builder()
                    .code(genCode(Type.DEPARTMENT))
                    .name(request.getName())
                    .address(request.getAddress())
                    .logoURL(request.getLogoUrl())
                    .phone(request.getPhone())
                    .email(request.getEmail())
                    .fax(request.getFax())
                    .parentDepartment(request.getParentDepartment())
                    .type(request.getType())
                    .build();
            ApiFuture<WriteResult> future = db.collection("departments").document(department.getCode()).set(department);
            future.get(); // Đợi thao tác hoàn thành
            return department;
        } catch (InterruptedException | ExecutionException e) {
            throw new CustomException("Failed to create department", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public Department getDepartment(String departmentCode) {
        try {
            Filter filter = Filter.and(
                    Filter.equalTo("deleted", true),
                    Filter.equalTo("code", departmentCode)
            );

            Query query = db.collection("departments").where(filter);
            QuerySnapshot querySnapshot = query.get().get();
            if (!querySnapshot.isEmpty()) {
                return querySnapshot.getDocuments().getFirst().toObject(Department.class);
            }
            return null;
        } catch (InterruptedException | ExecutionException e) {
            throw new CustomException("Failed to get department", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public List<Department> getAllDepartments() {
        List<Department> departments = new ArrayList<>();
        try {
            Filter filter = Filter.and(
                    Filter.equalTo("deleted", false)
            );
            QuerySnapshot querySnapshot = db.collection("departments").where(filter).get().get();
            List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
            for (QueryDocumentSnapshot document : documents) {
                departments.add(document.toObject(Department.class));
            }
            return departments;
        } catch (InterruptedException | ExecutionException e) {
            throw new CustomException("Failed to get all departments", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public Department updateDepartment(String departmentId, UpdateDepartmentRep request) {
        try {
            DocumentSnapshot document = db.collection("departments").document(departmentId).get().get();

            if (!document.exists()) {
                throw new CustomException("Đơn vị không tồn tại!", HttpStatus.NOT_FOUND);
            }

            Department department = document.toObject(Department.class);
            if (department == null) {
                throw new CustomException("Đơn vị không tồn tại!", HttpStatus.NOT_FOUND);
            }

            if (request.getName() != null && !request.getName().isBlank()) {
                department.setName(request.getName());
            }
            if (request.getAddress() != null && !request.getAddress().isBlank()) {
                department.setAddress(request.getAddress());
            }
            if (request.getLogoUrl() != null && !request.getLogoUrl().isBlank()) {
                department.setLogoURL(request.getLogoUrl());
            }
            if (request.getPhone() != null && !request.getPhone().isBlank()) {
                department.setPhone(request.getPhone());
            }
            if (request.getEmail() != null && !request.getEmail().isBlank()) {
                department.setEmail(request.getEmail());
            }
            if (request.getFax() != null && !request.getFax().isBlank()) {
                department.setFax(request.getFax());
            }
            if (request.getParentDepartment() != null && !request.getParentDepartment().isBlank()) {
                department.setParentDepartment(request.getParentDepartment());
            }
            if (request.getType() != null && !request.getType().isBlank()) {
                department.setType(request.getType());
            }

            db.collection("departments").document(departmentId).set(department).get();
            return department;
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Failed to update staff", e);
        }
    }

    public void deleteDepartment(List<String> ids) {
        try {
            Map<String, Object> deleted = Map.of("deleted", true);
            for (String id : ids) {
                db.collection("departments").document(id).update(deleted).get();
            }
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Failed to delete department", e);
        }
    }

    private String genCode(Type type) {
        String newCode;
        do {
            newCode = Utils.genCode(type);
        } while (isCodeExists(newCode));
        return newCode;
    }

    private boolean isCodeExists(String code) {
        try {
            CollectionReference departmentsRef = db.collection("departments");
            QuerySnapshot snapshot = departmentsRef.whereEqualTo("departmentId", code).get().get();
            return !snapshot.isEmpty();
        } catch (InterruptedException | ExecutionException e) {
            throw new CustomException("Đã có lỗi xảy ra!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}