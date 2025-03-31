package com.mobile.group.tlucontact.services.staff

import com.mobile.group.tlucontact.models.Staff
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

class StaffServiceImpl : StaffCommunicate {
    override fun getStaffs(token: String, page: Int, size: Int): Call<List<Staff>> {
        TODO("Not yet implemented")
    }
}