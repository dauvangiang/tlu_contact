package com.mobile.group.tlu_contact_be.service;

import com.mobile.group.tlu_contact_be.dto.request.IdsReq;
import com.mobile.group.tlu_contact_be.dto.response.PageResponse;
import com.mobile.group.tlu_contact_be.dto.response.staff.StaffRes;
import com.mobile.group.tlu_contact_be.exceptions.CustomException;
import com.mobile.group.tlu_contact_be.model.Department;
import com.mobile.group.tlu_contact_be.model.Staff;
import com.mobile.group.tlu_contact_be.repositories.StaffRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StaffService {

    private final StaffRepository staffRepository;

    public StaffRes createStaff(Staff staff) {
        try {
            if (staffRepository.existsByEmail(staff.getEmail())) {
                throw new RuntimeException("Email đã tồn tại!");
            }

            if (staffRepository.existsByPhone(staff.getPhone())) {
                throw new RuntimeException("Số điện thoại đã tồn tại!");
            }

            staffRepository.save(staff);
            return getStaffRes(staff);
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Failed to create staff", e);
        }
    }

    public StaffRes getStaff(String staffId) {
        try {
            Staff staff = staffRepository.findById(staffId);
            if (staff != null) {
                return getStaffRes(staff);
            }
            throw new CustomException("CBGV không tồn tại.", HttpStatus.NOT_FOUND);
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Failed to get staff", e);
        }
    }

    public PageResponse<StaffRes> getAllStaff(Integer page, Integer size, Boolean sort, String search, Boolean deleted) {
        try {
            // Mặc định page = 1, size = 10, sort = true (ASC), deleted = false
            page = (page == null || page < 1) ? 1 : page;
            size = (size == null || size < 1) ? 10 : size;
            sort = sort == null || sort;
            deleted = deleted != null && deleted;
            
            List<Staff> staffList = staffRepository.findAll(page, size, sort, search, deleted);
            long totalItems = staffRepository.count(deleted);
            long totalPages = (totalItems + size - 1) / size;
            
            List<StaffRes> staffResList = staffList.stream()
                    .map(this::getStaffRes)
                    .collect(Collectors.toList());
            
            return PageResponse.<StaffRes>builder()
                    .content(staffResList)
                    .page(page)
                    .size(size)
                    .totalElements(totalItems)
                    .totalPages(totalPages)
                    .build();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Failed to get all staff", e);
        }
    }

    public StaffRes updateStaff(String staffId, Staff request) {
        try {
            Staff existingStaff = staffRepository.findById(staffId);
            if (existingStaff == null) {
                throw new CustomException("CBGV không tồn tại.", HttpStatus.NOT_FOUND);
            }

            if (request.getFullName() != null && !request.getFullName().isBlank()) {
                existingStaff.setFullName(request.getFullName());
            }
            if (request.getPosition() != null && !request.getPosition().isBlank()) {
                existingStaff.setPosition(request.getPosition());
            }
            if (request.getDepartmentIds() != null) {
                existingStaff.setDepartmentIds(request.getDepartmentIds());
            }
            if (request.getPhotoBase64() != null && !request.getPhotoBase64().isBlank()) {
                existingStaff.setPhotoBase64(request.getPhotoBase64());
            }
            
            // Kiểm tra email và phone
            if (request.getEmail() != null && !request.getEmail().equals(existingStaff.getEmail()) 
                    && staffRepository.existsByEmail(request.getEmail())) {
                throw new RuntimeException("Email đã tồn tại!");
            }
            if (request.getPhone() != null && !request.getPhone().equals(existingStaff.getPhone()) 
                    && staffRepository.existsByPhone(request.getPhone())) {
                throw new RuntimeException("Số điện thoại đã tồn tại!");
            }
            
            // Cập nhật các trường khác nếu có
            if (request.getEmail() != null) {
                existingStaff.setEmail(request.getEmail());
            }
            if (request.getPhone() != null) {
                existingStaff.setPhone(request.getPhone());
            }
            if (request.getDeleted() != null) {
                existingStaff.setDeleted(request.getDeleted());
            }

            staffRepository.save(existingStaff);
            return getStaffRes(existingStaff);
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Failed to update staff", e);
        }
    }

    public void deleteStaff(String staffId) {
        try {
            Staff staff = staffRepository.findById(staffId);
            if (staff == null) {
                throw new CustomException("CBGV không tồn tại.", HttpStatus.NOT_FOUND);
            }
            
            // Soft delete
            staff.setDeleted(true);
            staffRepository.save(staff);
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Failed to delete staff", e);
        }
    }
    
    public void deleteStaffs(IdsReq ids) {
        try {
            List<String> idsList = ids.getIds();
            for (String id : idsList) {
                Staff staff = staffRepository.findById(id);
                if (staff != null) {
                    staff.setDeleted(true);
                    staffRepository.save(staff);
                }
            }
        } catch (InterruptedException | ExecutionException e) {
            throw new CustomException("Failed to delete staffs", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public void hardDeleteStaff(String staffId) {
        try {
            staffRepository.delete(staffId);
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Failed to delete staff", e);
        }
    }

    private StaffRes getStaffRes(Staff staff) {
        try {
            List<Department> departments = new ArrayList<>();
            if (staff.getDepartmentIds() != null && !staff.getDepartmentIds().isEmpty()) {
                departments = staffRepository.findDepartmentsByIds(staff.getDepartmentIds());
            }
            return new StaffRes(staff, departments);
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException("Failed to get staff response", e);
        }
    }

    public void setUid(String code, String uid) {
        staffRepository.getCollection().document(code).update(Map.of("userID", uid));
    }
}
