package com.mobile.group.tlucontact.services.student

import android.util.Log
import com.mobile.group.tlucontact.dto.ApiResponse
import com.mobile.group.tlucontact.models.Student
import com.mobile.group.tlucontact.services.RetrofitCommunication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call

class StudentServiceImpl : StudentService {
    private val communicate = RetrofitCommunication.build(StudentCommunicate::class.java, "contact_api_url")

    override suspend fun getStudents(
        token: String,
        page: Int?,
        size: Int?,
        sort: Boolean?,
        search: String?,
        deleted: Boolean?
    ): MutableList<Student>? {
        val call: Call<ApiResponse<MutableList<Student>>> = communicate.getStudents(token, page, size, sort, search, deleted)
        return withContext(Dispatchers.IO) {
            try {
                val response = call.execute()
                if (response.isSuccessful) { response.body()?.data ?: mutableListOf() }
                else { throw Exception("Error: ${response.body()?.message}") }
            } catch (e: Exception) {
                Log.e("API_ERROR", e.message ?: "Unknown error")
                null
            }
        }
    }
}