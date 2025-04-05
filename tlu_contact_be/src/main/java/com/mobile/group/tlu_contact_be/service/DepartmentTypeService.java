package com.mobile.group.tlu_contact_be.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.WriteResult;
import com.mobile.group.tlu_contact_be.dto.request.IdsReq;
import com.mobile.group.tlu_contact_be.exceptions.CustomException;
import com.mobile.group.tlu_contact_be.model.Department;
import com.mobile.group.tlu_contact_be.model.DepartmentType;
import com.mobile.group.tlu_contact_be.repositories.DepartmentTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class DepartmentTypeService {
    private final DepartmentTypeRepository departmentTypeRepository;

    public List<DepartmentType> getDepartmentTypes() {
        try {
            List<QueryDocumentSnapshot> snapshots = departmentTypeRepository.getDepartmentTypes();
            return snapshots.stream().map(
                    snapshot -> snapshot.toObject(DepartmentType.class)
            ).toList();
        } catch (ExecutionException | InterruptedException e) {
            throw new CustomException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public List<Map<String, String>> getFilterDepartments(String filterId, String parentId) {
        try {
            List<QueryDocumentSnapshot> snapshots = departmentTypeRepository.getFilterDepartments(filterId, parentId);
            return snapshots.stream().map( snapshot -> {
                        Department department = snapshot.toObject(Department.class);
                        return  Map.of("code", department.getCode(), "name", department.getName());
                    }).toList();
        } catch (ExecutionException | InterruptedException e) {
            throw new CustomException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public DepartmentType createDepartmentType(DepartmentType type) {
        try {
            DocumentReference docRef = departmentTypeRepository.getTypeCollection().document();
            type.setId(docRef.getId());
            ApiFuture<WriteResult> future = docRef.set(type);
            future.get();
            return type;
        } catch (ExecutionException | InterruptedException e) {
            throw new CustomException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public DepartmentType updateDepartmentType(String id, DepartmentType type) {
        try {
            QueryDocumentSnapshot snapshot = departmentTypeRepository.getTypeByID(id);
            if (snapshot == null) {
                throw new CustomException("No such department type", HttpStatus.NOT_FOUND);
            }

            DepartmentType dType = snapshot.toObject(DepartmentType.class);
            if (type.getName() != null && !type.getName().isBlank()) {
                dType.setName(type.getName());
            }
            departmentTypeRepository.getTypeCollection().document(id).set(dType).get();
            return dType;
        } catch (ExecutionException | InterruptedException e) {
            throw new CustomException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public void deleteDepartmentType(IdsReq request) {
        try {
            Map<String, Object> deleted = Map.of("deleted", true);
            List<String> ids = request.getIds();
            for (String id : ids) {
                departmentTypeRepository.getTypeCollection().document(id).update(deleted).get();
            }
        } catch (ExecutionException | InterruptedException e) {
            throw new CustomException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
