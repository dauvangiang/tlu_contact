package com.mobile.group.tlu_contact_be.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.FirebaseApp;
import com.google.firebase.cloud.FirestoreClient;
import com.mobile.group.tlu_contact_be.exceptions.CustomException;
import com.mobile.group.tlu_contact_be.model.Student;
import com.mobile.group.tlu_contact_be.model.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class StudentService {

    private final Firestore db;

    public StudentService(FirebaseApp firebaseApp) {
        this.db = FirestoreClient.getFirestore();
    }

    public String createStudent(Student student) {
        try {
            ApiFuture<WriteResult> future = db.collection("students").document(student.getStudentId()).set(student);
            future.get();
            return student.getStudentId();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Failed to create student", e);
        }
    }

    public Student getStudent(String studentId) {
        try {
            DocumentReference docRef = db.collection("students").document(studentId);
            ApiFuture<DocumentSnapshot> future = docRef.get();
            DocumentSnapshot document = future.get();
            if (document.exists()) {
                return document.toObject(Student.class);
            }
            return null;
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Failed to get student", e);
        }
    }

    public List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();
        try {
            ApiFuture<QuerySnapshot> future = db.collection("students").get();
            List<QueryDocumentSnapshot> documents = future.get().getDocuments();
            for (QueryDocumentSnapshot document : documents) {
                students.add(document.toObject(Student.class));
            }
            return students;
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Failed to get all students", e);
        }
    }

    public Student updateStudent(String studentId, Student student) {
        try {
            Student exitingStudent = getStudent(studentId);
            if (exitingStudent == null) {
                throw new CustomException("Student not found");
            }
            if(student.getFullName() == null) {
                student.setFullName(exitingStudent.getFullName());
            }
            if(student.getPhotoURL() == null) {
                student.setPhotoURL(exitingStudent.getPhotoURL());
            }
            if(student.getPhone() == null) {
                student.setPhone(exitingStudent.getPhone());
            }
            if(student.getEmail() == null) {
                student.setEmail(exitingStudent.getEmail());
            }
            if(student.getAddress() == null) {
                student.setAddress(exitingStudent.getAddress());
            }
            if(student.getClassName() == null) {
                student.setClassName(exitingStudent.getClassName());
            }
            if(student.getUnit() == null) {
                student.setUnit(exitingStudent.getUnit());
            }
            if(student.getUserID() == null) {
                student.setUserID(exitingStudent.getUserID());
            }
            db.collection("students").document(studentId).set(student).get();
            return student;
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Failed to update student", e);
        }
    }

    public void deleteStudent(String studentId) {
        try {
            db.collection("students").document(studentId).delete().get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Failed to delete student", e);
        }
    }
}