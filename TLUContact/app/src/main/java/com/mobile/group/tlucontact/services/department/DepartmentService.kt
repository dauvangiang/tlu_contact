package com.mobile.group.tlucontact.services.department

import com.mobile.group.tlucontact.models.Department

interface DepartmentService {
    fun getDepartments(token: String, page: Int, size: Int): List<Department>
}