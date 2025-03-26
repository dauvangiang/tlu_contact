package com.mobile.group.tlucontact.services.department

import com.mobile.group.tlucontact.models.Department
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface DepartmentCommunicate {
    @GET("http://localhost:8080/api/v1/departments")
    fun getDepartments(@Header("Authorization") token: String, @Query("page") page: Int, @Query("size") size: Int) : Call<MutableList<Department>>
}