package com.mobile.group.tlu_contact_be.controller;

import com.mobile.group.tlu_contact_be.dto.request.department.CreateDepartmentReq;
import com.mobile.group.tlu_contact_be.dto.request.department.UpdateDepartmentRep;
import com.mobile.group.tlu_contact_be.dto.response.BaseResponse;
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
    public ResponseEntity<Department> getDepartment(@PathVariable String departmentId) {
        Department department = departmentService.getDepartment(departmentId);
        if (department != null) {
            return ResponseEntity.ok(department);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Lấy danh sách tất cả đơn vị
    @GetMapping("v1/departments")
    public ResponseEntity<BaseResponse<List<Department>>> getDepartments() {
        List<Department> departments = departmentService.getAllDepartments();
        return ResponseEntity.ok(new BaseResponse<>(departments, departments.size()));
    }

    // Cập nhật thông tin một đơn vị
    @PostMapping("v1/departments/update/{departmentId}")
    public ResponseEntity<BaseResponse<Department>> updateDepartment(@PathVariable String departmentId, @RequestBody UpdateDepartmentRep request) {
        return ResponseEntity.ok(new BaseResponse<>(departmentService.updateDepartment(departmentId, request)));
    }

    // Xóa một đơn vị
    @PostMapping("v1/departments/delete/{departmentId}")
    public ResponseEntity<BaseResponse<Object>> deleteDepartment(@PathVariable List<String> ids) {
        departmentService.deleteDepartment(ids);
        return ResponseEntity.ok(new BaseResponse<>("Success"));
    }
}
