package com.mobile.group.tlucontact.models

import android.os.Parcelable
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import kotlinx.parcelize.Parcelize

@Parcelize
data class Student @JsonCreator constructor(
    @JsonProperty("studentId") val code: String,
    @JsonProperty("fullName") val fullName: String,
    @JsonProperty("photoUrl") val photoUrl: String? = null,
    @JsonProperty("phone") val phone: String,
    @JsonProperty("email") val email: String,
    @JsonProperty("address") val address: String? = null,
//    @JsonProperty("studentId") val className: String,
    @JsonProperty("department") val department: Department,
    @JsonProperty("userID") val userID: String? = null
) : Parcelable