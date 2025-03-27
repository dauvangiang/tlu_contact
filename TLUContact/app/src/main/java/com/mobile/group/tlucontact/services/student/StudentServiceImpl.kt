package com.mobile.group.tlucontact.services.student

import com.mobile.group.tlucontact.models.Student
import retrofit2.Call

class StudentServiceImpl : StudentCommunicate {
    override fun getStudents(token: String, page: Int, size: Int): Call<List<Student>> {
        TODO("Not yet implemented")
    }
}