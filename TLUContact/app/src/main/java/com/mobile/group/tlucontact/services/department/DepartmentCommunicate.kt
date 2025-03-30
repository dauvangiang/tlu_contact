package com.mobile.group.tlucontact.services.department

import com.mobile.group.tlucontact.dto.ApiResponse
import com.mobile.group.tlucontact.models.Department
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface DepartmentCommunicate {
    @GET("api/v1/departments")
    fun getDepartments(
        @Header("Authorization") token: String,
        @Query("page") page: Int? = null,
        @Query("size") size: Int? = null,
        @Query("sort") sort: Boolean? = null,
        @Query("search") search: String? = null,
        @Query("deleted") deleted: Boolean? = null
    ) : Call<ApiResponse<MutableList<Department>>>

    @GET("api/v1/departments/{departmentId}")
    fun getDepartment(
        @Header("Authorization") token: String,
        @Path("departmentId") code: String
    ) : Call<ApiResponse<Department>>
}