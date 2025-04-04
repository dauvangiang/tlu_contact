package com.mobile.group.tlucontact.services.department

import com.mobile.group.tlucontact.models.Department
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface DepartmentCommunicate {
    @GET("api/v1/departments")
    fun getDepartments(@Header("Authorization") token: String) : Call<MutableList<Department>>
//    fun getDepartments(@Header("Authorization") token: String, @Query("page") page: Int, @Query("size") size: Int) : Call<MutableList<Department>>
}