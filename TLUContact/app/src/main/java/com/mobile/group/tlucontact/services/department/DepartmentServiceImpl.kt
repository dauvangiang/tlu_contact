package com.mobile.group.tlucontact.services.department

import android.util.Log
import com.mobile.group.tlucontact.dto.ApiResponse
import com.mobile.group.tlucontact.models.Department
import com.mobile.group.tlucontact.services.RetrofitCommunication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call

class DepartmentServiceImpl : DepartmentService {
    private val communicate = RetrofitCommunication.build(DepartmentCommunicate::class.java, "contact_api_url")

    override suspend fun getDepartments(
        token: String, page: Int?, size: Int?, sort: Boolean?, search: String?, deleted: Boolean?
    ): MutableList<Department>? {
        val call: Call<ApiResponse<MutableList<Department>>> = communicate.getDepartments(token, page, size, sort, search, deleted)
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

    override suspend fun getDepartment(token: String, code: String): Department? {
        val call: Call<ApiResponse<Department>> = communicate.getDepartment(token, code)
        return withContext(Dispatchers.IO) {
            try {
                val response = call.execute()
                if (response.isSuccessful) {
                    response.body()?.data
                } else {
                    throw Exception("Error: ${response.body()?.message}")
                }
            } catch (e: Exception) {
                Log.e("API_ERROR", e.message ?: "Unknown error")
                null
            }
        }
    }
}