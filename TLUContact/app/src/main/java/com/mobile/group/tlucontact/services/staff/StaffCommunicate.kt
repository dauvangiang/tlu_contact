package com.mobile.group.tlucontact.services.staff

import com.mobile.group.tlucontact.models.Staff
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface StaffCommunicate {
    @GET("/api/v1/staffs")
    fun getStaffs(@Header("Authorization") token: String, @Query("page") page: Int, @Query("size") size: Int) : Call<List<Staff>>
}