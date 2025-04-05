package com.mobile.group.tlu_contact_be.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.mobile.group.tlu_contact_be.dto.constant.Type;
import com.mobile.group.tlu_contact_be.dto.request.IdsReq;
import com.mobile.group.tlu_contact_be.dto.request.department.CreateDepartmentReq;
import com.mobile.group.tlu_contact_be.dto.request.department.UpdateDepartmentReq;
import com.mobile.group.tlu_contact_be.dto.response.department.DepartmentRes;
import com.mobile.group.tlu_contact_be.exceptions.CustomException;
import com.mobile.group.tlu_contact_be.model.Department;
import com.mobile.group.tlu_contact_be.model.DepartmentType;
import com.mobile.group.tlu_contact_be.repositories.DepartmentRepository;
import com.mobile.group.tlu_contact_be.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class DepartmentService {

    private final DepartmentRepository departmentRepo;

    public Department createDepartment(CreateDepartmentReq request) {
        try {
            Department department = Department.builder()
                    .code(genCode(Type.DEPARTMENT))
                    .name(request.getName())
                    .address(request.getAddress())
                    .logoBase64(request.getLogoBase64())
                    .phone(request.getPhone())
                    .email(request.getEmail())
                    .parentDepartmentId(request.getParentDepartmentId())
                    .typeId(request.getTypeId())
                    .deleted(false)
                    .build();
            ApiFuture<WriteResult> future = departmentRepo.getCollection().document(department.getCode()).set(department);
            future.get();
            return department;
        } catch (InterruptedException | ExecutionException e) {
            throw new CustomException("Failed to create department", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public Department getDepartment(String id) {
        try {
            QueryDocumentSnapshot documentSnapshot = departmentRepo.getDepartment(id);
            if (documentSnapshot == null) throw new CustomException("Department not found", HttpStatus.NOT_FOUND);
            return documentSnapshot.toObject(Department.class);
        } catch (InterruptedException | ExecutionException e) {
            throw new CustomException("Failed to get department", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public List<Department> getAllDepartments(Integer page, Integer size, Boolean sort, String search, Boolean deleted) {
        List<Department> departments = new ArrayList<>();
        try {
            List<QueryDocumentSnapshot> documentSnapshots = departmentRepo.getDepartments(page, size, sort, search, deleted);
            for (QueryDocumentSnapshot document : documentSnapshots) {
                departments.add(document.toObject(Department.class));
            }
            return departments;
        } catch (ExecutionException | InterruptedException e) {
            throw new CustomException("Failed to get departments", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public Department updateDepartment(String departmentId, UpdateDepartmentReq request) {
        try {
            DocumentSnapshot document = departmentRepo.getCollection().document(departmentId).get().get();

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
            if (request.getLogoBase64() != null && !request.getLogoBase64().isBlank()) {
                department.setLogoBase64(request.getLogoBase64());
            }
            if (request.getPhone() != null && !request.getPhone().isBlank()) {
                department.setPhone(request.getPhone());
            }
            if (request.getEmail() != null && !request.getEmail().isBlank()) {
                department.setEmail(request.getEmail());
            }
            if (request.getParentDepartmentId() != null && !request.getParentDepartmentId().isBlank()) {
                department.setParentDepartmentId(request.getParentDepartmentId());
            }
            if (request.getTypeId() != null) {
                department.setTypeId(request.getTypeId());
            }

            departmentRepo.getCollection().document(departmentId).set(department).get();
            return department;
        } catch (InterruptedException | ExecutionException e) {
            throw new CustomException("Failed to update staff", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public void deleteDepartment(IdsReq ids) {
        try {
            Map<String, Object> deleted = Map.of("deleted", true);
            List<String> idsList = ids.getIds();
            for (String id : idsList) {
                departmentRepo.getCollection().document(id).update(deleted).get();
            }
        } catch (InterruptedException | ExecutionException e) {
            throw new CustomException("Failed to delete department", HttpStatus.INTERNAL_SERVER_ERROR);
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
            QuerySnapshot snapshot = departmentRepo.getCollection().whereEqualTo("departmentId", code).get().get();
            return !snapshot.isEmpty();
        } catch (InterruptedException | ExecutionException e) {
            throw new CustomException("Đã có lỗi xảy ra!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public List<Department> getChild(String parentId) {
        try {
            List<QueryDocumentSnapshot> snapshots = departmentRepo.getChild(parentId);
            List<Department> departments = new ArrayList<>();
            for (QueryDocumentSnapshot document : snapshots) {
                departments.add(document.toObject(Department.class));
            }
            return departments;
        } catch (ExecutionException | InterruptedException e) {
            throw new CustomException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}