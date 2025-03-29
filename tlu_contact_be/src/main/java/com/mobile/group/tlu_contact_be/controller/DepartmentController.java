package com.mobile.group.tlu_contact_be.controller;

import com.mobile.group.tlu_contact_be.dto.request.IdsReq;
import com.mobile.group.tlu_contact_be.dto.request.department.CreateDepartmentReq;
import com.mobile.group.tlu_contact_be.dto.request.department.UpdateDepartmentReq;
import com.mobile.group.tlu_contact_be.dto.response.BaseResponse;
import com.mobile.group.tlu_contact_be.dto.response.department.DepartmentRes;
import com.mobile.group.tlu_contact_be.model.Department;
import com.mobile.group.tlu_contact_be.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/")
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentService departmentService;

    @PostMapping("v1/departments/create")
    public ResponseEntity<BaseResponse<Department>> createDepartment(@RequestBody CreateDepartmentReq request) {
        return ResponseEntity.ok(new BaseResponse<>(departmentService.createDepartment(request)));
    }

    @GetMapping("v1/departments/{departmentId}")
    public ResponseEntity<DepartmentRes> getDepartment(@PathVariable String departmentId) {
        return ResponseEntity.ok(departmentService.getDepartment(departmentId));
    }

    @GetMapping("v1/departments")
    public ResponseEntity<BaseResponse<List<DepartmentRes>>> getDepartments(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) Boolean sort,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Boolean deleted
    ) {
        List<DepartmentRes> departments = departmentService.getAllDepartments(page, size, sort, search, deleted);
        return ResponseEntity.ok(new BaseResponse<>(departments, departments.size()));
    }

    // Cập nhật thông tin một đơn vị
    @PostMapping("v1/departments/update/{departmentId}")
    public ResponseEntity<BaseResponse<Department>> updateDepartment(@PathVariable String departmentId, @RequestBody UpdateDepartmentReq request) {
        return ResponseEntity.ok(new BaseResponse<>(departmentService.updateDepartment(departmentId, request)));
    }

    // Xóa một đơn vị
    @PostMapping("v1/departments/delete")
    public ResponseEntity<BaseResponse<Object>> deleteDepartment(@RequestBody IdsReq ids) {
        departmentService.deleteDepartment(ids);
        return ResponseEntity.ok(new BaseResponse<>("Success"));
    }
}
