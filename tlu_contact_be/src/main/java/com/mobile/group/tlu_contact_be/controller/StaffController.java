package com.mobile.group.tlu_contact_be.controller;

import com.mobile.group.tlu_contact_be.dto.response.BaseResponse;
import com.mobile.group.tlu_contact_be.model.Staff;
import com.mobile.group.tlu_contact_be.service.StaffService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/")
@RequiredArgsConstructor
public class StaffController {

    private final StaffService staffService;

    @GetMapping("v1/staff")
    public ResponseEntity<Object> getAllStaff() {
        return ResponseEntity.ok(new BaseResponse<>(staffService.getAllStaff()));
    }

    @GetMapping("v1/staff/{staffId}")
    public ResponseEntity<Object> getStaff(@PathVariable String staffId) {
        return ResponseEntity.ok(new BaseResponse<>(staffService.getStaff(staffId)));
    }

    @PostMapping("v1/staff/create")
    public ResponseEntity<Object> createStaff(@RequestBody Staff request) {
        return new ResponseEntity<>(new BaseResponse<>(staffService.createStaff(request)), HttpStatus.CREATED );
    }

    @PostMapping("v1/staff/update/{staffId}")
    public ResponseEntity<Object> updateStaff(@PathVariable String staffId, @RequestBody Staff request) {
        return ResponseEntity.ok(new BaseResponse<>(staffService.updateStaff(staffId, request)));
    }

    @GetMapping("v1/staff/delete/{staffId}")
    public ResponseEntity<Object> deleteStaff(@PathVariable String staffId) {
        staffService.deleteStaff(staffId);
        return ResponseEntity.ok(new BaseResponse<>("Đã xóa thành công"));
    }
}
