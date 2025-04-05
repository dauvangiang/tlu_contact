package com.mobile.group.tlu_contact_be.controller;

import com.mobile.group.tlu_contact_be.dto.request.IdsReq;
import com.mobile.group.tlu_contact_be.dto.response.BaseResponse;
import com.mobile.group.tlu_contact_be.model.DepartmentType;
import com.mobile.group.tlu_contact_be.service.DepartmentTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/")
@RequiredArgsConstructor
public class DepartmentTypeController {
    private final DepartmentTypeService departmentTypeService;

    @GetMapping("v1/department-types")
    public ResponseEntity<BaseResponse<List<DepartmentType>>> getAllDepartmentTypes() {
        return ResponseEntity.ok(new BaseResponse<>(departmentTypeService.getDepartmentTypes()));
    }

    @GetMapping("v1/department-types/filter")
    public ResponseEntity<BaseResponse<List<Map<String, String>>>> getDepartmentTypeFilter(
            @RequestParam(required = false) String filterId, @RequestParam(required = false) String parentId
    ) {
        return ResponseEntity.ok(new BaseResponse<>(departmentTypeService.getFilterDepartments(filterId, parentId)));
    }

    @PostMapping("v1/department-types/create")
    public ResponseEntity<BaseResponse<DepartmentType>> createDepartmentType(@RequestBody DepartmentType request) {
        return new ResponseEntity<>(new BaseResponse<>(departmentTypeService.createDepartmentType(request)), HttpStatus.CREATED);
    }

    @PostMapping("v1/department-types/update/{id}")
    public ResponseEntity<BaseResponse<DepartmentType>> updateDepartmentType(@PathVariable String id, @RequestBody DepartmentType request) {
        return ResponseEntity.ok(new BaseResponse<>(departmentTypeService.updateDepartmentType(id, request)));
    }

    @PostMapping("v1/department-types/delete")
    public ResponseEntity<BaseResponse<String>> deleteDepartmentType(@RequestBody IdsReq request) {
        departmentTypeService.deleteDepartmentType(request);
        return ResponseEntity.ok(new BaseResponse<>());
    }
}
