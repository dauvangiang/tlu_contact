package com.mobile.group.tlucontact.services.department

import com.mobile.group.tlucontact.models.Department

interface DepartmentService {
    suspend fun getDepartments(
        token: String,
        page: Int? = null,
        size: Int? = null,
        sort: Boolean? = null,
        search: String? = null,
        deleted: Boolean? = null
    ): MutableList<Department>?

    suspend fun getDepartment(
        token: String,
        code: String
    ): Department?
}