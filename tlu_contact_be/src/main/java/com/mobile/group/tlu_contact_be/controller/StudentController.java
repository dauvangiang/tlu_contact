package com.mobile.group.tlu_contact_be.controller;

import com.mobile.group.tlu_contact_be.dto.request.IdsReq;
import com.mobile.group.tlu_contact_be.dto.response.BaseResponse;
import com.mobile.group.tlu_contact_be.model.Student;
import com.mobile.group.tlu_contact_be.service.StudentService;
import com.mobile.group.tlu_contact_be.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @GetMapping("v1/students")
    public ResponseEntity<Object> getAllStudents(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) Boolean sort,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Boolean deleted
    ) {
        return ResponseEntity.ok(new BaseResponse<>(studentService.getAllStudents(page, size, sort, search, deleted)));
    }

    @GetMapping("v1/student/{studentId}")
    public ResponseEntity<Object> getStudentByID(@PathVariable String studentId) {
        return ResponseEntity.ok(new BaseResponse<>(studentService.getStudent(studentId)));
    }

    @PostMapping("v1/student/create")
    public ResponseEntity<Object> createStudent(@RequestBody Student request) {
        return new ResponseEntity<>(new BaseResponse<>(studentService.createStudent(request)), HttpStatus.CREATED );
    }

    @PostMapping("v1/student/update/{studentId}")
    public ResponseEntity<Object> updateStudent(@PathVariable String studentId, @RequestBody Student request) {
        return ResponseEntity.ok(new BaseResponse<>(studentService.updateStudent(studentId, request)));
    }

    @GetMapping("v1/student/delete")
    public ResponseEntity<Object> deleteStudent(@RequestBody IdsReq ids) {
        studentService.deleteStudent(ids);
        return ResponseEntity.ok(new BaseResponse<>("Đã xóa thành công"));
    }
}
