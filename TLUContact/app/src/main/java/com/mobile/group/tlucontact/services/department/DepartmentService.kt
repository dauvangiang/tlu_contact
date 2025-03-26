package com.mobile.group.tlucontact.services.department

import com.mobile.group.tlucontact.models.Department

interface DepartmentService {
    suspend fun getDepartments(token: String, page: Int, size: Int): MutableList<Department>?
}