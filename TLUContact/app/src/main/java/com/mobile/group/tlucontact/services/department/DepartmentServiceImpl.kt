package com.mobile.group.tlucontact.services.department

import com.mobile.group.tlucontact.models.Department
import com.mobile.group.tlucontact.services.RetrofitCommunication
import retrofit2.Call

class DepartmentServiceImpl() : DepartmentService {
    private val communicate = RetrofitCommunication.build(DepartmentCommunicate::class.java, "contact_api_url")

    override fun getDepartments(token: String, page: Int, size: Int): List<Department> {
        val call: Call<List<Department>> = communicate.getDepartments(token, page, size)
        return handleResponse(call) ?: emptyList()
    }

    private fun <T> handleResponse(call: Call<T>) : T? {
        return try {
            val response = call.execute()
            if (response.isSuccessful) {
                response.body()
            } else {
                throw Exception(response.message())
            }
        } catch (e: Exception) {
            throw Exception(e)
        }
    }
}