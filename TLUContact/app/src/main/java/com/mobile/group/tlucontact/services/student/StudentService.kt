package com.mobile.group.tlucontact.services.student

import com.mobile.group.tlucontact.models.Student

interface StudentService {
    suspend fun getStudents(
        token: String,
        page: Int? = null,
        size: Int? = null,
        sort: Boolean? = null,
        search: String? = null,
        deleted: Boolean? = null
    ) : MutableList<Student>?
}