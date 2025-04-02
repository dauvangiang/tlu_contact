package com.mobile.group.tlu_contact_be.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.FirebaseApp;
import com.google.firebase.cloud.FirestoreClient;
import com.mobile.group.tlu_contact_be.dto.request.IdsReq;
import com.mobile.group.tlu_contact_be.dto.response.department.DepartmentRes;
import com.mobile.group.tlu_contact_be.dto.response.student.StudentRes;
import com.mobile.group.tlu_contact_be.exceptions.CustomException;
import com.mobile.group.tlu_contact_be.model.Department;
import com.mobile.group.tlu_contact_be.model.Student;
import com.mobile.group.tlu_contact_be.model.User;
import com.mobile.group.tlu_contact_be.repositories.DepartmentRepository;
import com.mobile.group.tlu_contact_be.repositories.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepo;
    private final DepartmentRepository departmentRepo;

    public String createStudent(Student student) {
        try {
            ApiFuture<WriteResult> future = studentRepo.getCollection().document(student.getStudentId()).set(student);
            future.get();
            return student.getStudentId();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Failed to create student", e);
        }
    }

    public StudentRes getStudent(String studentId) {
        try {
            QueryDocumentSnapshot snapshot = studentRepo.getStudent(studentId);
            if (snapshot == null) throw new CustomException("Student not found", HttpStatus.NOT_FOUND);
            return getStudentRes(snapshot.toObject(Student.class));
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Failed to get student", e);
        }
    }

    public List<StudentRes> getAllStudents(Integer page, Integer size, Boolean sort, String search, Boolean deleted) {
        List<StudentRes> studentsRes = new ArrayList<>();
        try {
            List<QueryDocumentSnapshot> snapshots = studentRepo.getStudents(page, size, sort, search, deleted);
            for (QueryDocumentSnapshot snapshot : snapshots) {
                StudentRes studentRes = getStudentRes(snapshot.toObject(Student.class));
                studentsRes.add(studentRes);
            }

            return studentsRes;
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
//            throw new CustomException("Failed to get all students", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private StudentRes getStudentRes(Student student) throws ExecutionException, InterruptedException {
        Department department = null;
        if (student.getDepartmentId() != null) {
            QueryDocumentSnapshot departmentSnapshot = departmentRepo.getDepartment(student.getDepartmentId());
            department = departmentSnapshot == null ? null : departmentSnapshot.toObject(Department.class);
        }
        return new StudentRes(student, department);
    }

    public Student updateStudent(String studentId, Student request) {
        try {
            QueryDocumentSnapshot snapshot = studentRepo.getStudent(studentId);
            if (snapshot == null) throw new CustomException("Student not found", HttpStatus.NOT_FOUND);
            Student student = snapshot.toObject(Student.class);

            if(request.getFullName() != null && !request.getFullName().isBlank()) {
                student.setFullName(request.getFullName());
            }
            if(request.getPhotoBase64() != null && !request.getPhotoBase64().isBlank()) {
                student.setPhotoBase64(request.getPhotoBase64());
            }
            if(request.getPhone() != null && !request.getPhone().isBlank()) {
                student.setPhone(request.getPhone());
            }
            if(request.getEmail() != null && !request.getEmail().isBlank()) {
                student.setEmail(request.getEmail());
            }
            if(request.getAddress() != null && !request.getAddress().isBlank()) {
                student.setAddress(request.getAddress());
            }
            if(request.getClassName() != null && !request.getClassName().isBlank()) {
                student.setClassName(request.getClassName());
            }
            if(request.getDepartmentId() != null && !request.getDepartmentId().isBlank()) {
                student.setDepartmentId(request.getDepartmentId());
            }

            studentRepo.getCollection().document(studentId).set(student).get();
            return student;
        } catch (InterruptedException | ExecutionException e) {
            throw new CustomException("Failed to update student", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public void deleteStudent(IdsReq ids) {
        try {
            Map<String, Object> deleted = Map.of("deleted", true);
            List<String> idsList = ids.getIds();
            for (String id : idsList) {
                studentRepo.getCollection().document(id).update(deleted).get();
            }
        } catch (InterruptedException | ExecutionException e) {
            throw new CustomException("Failed to delete student", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}