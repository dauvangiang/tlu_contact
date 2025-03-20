package com.mobile.group.tlu_contact_be.controller;

import com.mobile.group.tlu_contact_be.model.Unit;
import com.mobile.group.tlu_contact_be.service.UnitService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/")
@RequiredArgsConstructor
public class UnitController {

    private final UnitService unitService;

    // Tạo đơn vị mới
    @PostMapping("v1/units/create")
    public ResponseEntity<String> createUnit(@RequestBody Unit unit) {
        String unitId = unitService.createUnit(unit);
        return ResponseEntity.ok("Created unit with ID: " + unitId);
    }

    // Lấy thông tin một đơn vị theo ID
    @GetMapping("v1/units/{unitId}")
    public ResponseEntity<Unit> getUnit(@PathVariable String unitId) {
        Unit unit = unitService.getUnit(unitId);
        if (unit != null) {
            return ResponseEntity.ok(unit);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Lấy danh sách tất cả đơn vị
    @GetMapping("v1/units")
    public ResponseEntity<List<Unit>> getAllUnits() {
        List<Unit> units = unitService.getAllUnits();
        return ResponseEntity.ok(units);
    }

    // Cập nhật thông tin một đơn vị
    @PutMapping("v1/units/update/{unitId}")
    public ResponseEntity<String> updateUnit(@PathVariable String unitId, @RequestBody Unit unit) {
        unitService.updateUnit(unitId, unit);
        return ResponseEntity.ok("Updated unit with ID: " + unitId);
    }

    // Xóa một đơn vị
    @DeleteMapping("v1/units/delete/{unitId}")
    public ResponseEntity<String> deleteUnit(@PathVariable String unitId) {
        unitService.deleteUnit(unitId);
        return ResponseEntity.ok("Deleted unit with ID: " + unitId);
    }
}
