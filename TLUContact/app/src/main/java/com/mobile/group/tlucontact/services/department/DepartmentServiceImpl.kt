package com.mobile.group.tlucontact.services.department

import android.util.Log
import com.mobile.group.tlucontact.models.Department
import com.mobile.group.tlucontact.services.RetrofitCommunication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DepartmentServiceImpl : DepartmentService {
    private val communicate = RetrofitCommunication.build(DepartmentCommunicate::class.java, "contact_api_url")

    override suspend fun getDepartments(token: String, page: Int, size: Int): MutableList<Department>? {
        val call: Call<MutableList<Department>> = communicate.getDepartments(token)
        return withContext(Dispatchers.IO) {
            try {
                val response = call.execute()
                if (response.isSuccessful) { response.body() }
                else { throw Exception("Error: ${response.message()}") }
            } catch (e: Exception) {
                Log.e("API_ERROR", e.message ?: "Unknown error")
                null
            }
        }
    }

    private fun <T : Any> handleResponse(call: Call<T>, callback: (T?, Throwable?) -> Unit) {
        call.enqueue(object: Callback<T> {
            override fun onFailure(call: Call<T>, throwable: Throwable) {
                callback(null, throwable)
            }

            override fun onResponse(call: Call<T>, response: Response<T>) {
                if (response.isSuccessful) {
                    callback(response.body(), null)
                } else {
                    callback(null, Throwable("Response is not successful"))
                }
            }
        })
    }
}