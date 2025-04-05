package com.mobile.group.tlu_contact_be.dto.response.student;

import com.mobile.group.tlu_contact_be.dto.response.department.DepartmentRes;
import com.mobile.group.tlu_contact_be.model.Department;
import com.mobile.group.tlu_contact_be.model.Student;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StudentRes {
    String studentId;
    String fullName;
    String photoBase64;
    String phone;
    String email;
    String address;
    String className;
    Department department;
    String userID;

    public StudentRes(Student student, Department department) {
        this.studentId = student.getStudentId();
        this.fullName = student.getFullName();
        this.photoBase64 = student.getPhotoBase64();
        this.phone = student.getPhone();
        this.email = student.getEmail();
        this.address = student.getAddress();
        this.className = student.getClassName();
        this.department = department;
        this.userID = student.getUserID();
    }
}
