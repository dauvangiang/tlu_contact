package com.mobile.group.tlu_contact_be.controller;

import com.mobile.group.tlu_contact_be.dto.response.BaseResponse;
import com.mobile.group.tlu_contact_be.model.Staff;
import com.mobile.group.tlu_contact_be.service.StaffService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/")
@RequiredArgsConstructor
public class StaffController {

    private final StaffService staffService;

    @GetMapping("v1/staffs")
    public ResponseEntity<List<Staff>> getAllStaff() {
        return ResponseEntity.ok(staffService.getAllStaff());
    }

    @GetMapping("v1/staffs/{staffId}")
    public ResponseEntity<Staff> getStaff(@PathVariable String staffId) {
        return ResponseEntity.ok(staffService.getStaff(staffId));
    }

    @PostMapping("v1/staffs/create")
    public ResponseEntity<Object> createStaff(@RequestBody Staff request) {
        return new ResponseEntity<>(staffService.createStaff(request), HttpStatus.CREATED);
    }

    @PutMapping("v1/staffs/update/{staffId}")
    public ResponseEntity<Object> updateStaff(@PathVariable String staffId, @RequestBody Staff request) {
        return ResponseEntity.ok(staffService.updateStaff(staffId, request));
    }

    @DeleteMapping("v1/staffs/delete/{staffId}")
    public ResponseEntity<Object> deleteStaff(@PathVariable String staffId) {
        staffService.deleteStaff(staffId);
        return ResponseEntity.ok(new BaseResponse<>("Đã xóa thành công"));
    }
}
