package com.mobile.group.tlucontact.services.student

import com.mobile.group.tlucontact.models.Student
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface StudentCommunicate {
    @GET("api/v1/students")
    fun getStudents(@Header("Authorization") token: String, @Query("page") page: Int, @Query("size") size: Int) : Call<List<Student>>
}