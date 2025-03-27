package com.mobile.group.tlucontact.models

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Staff(
    val staffId: String,
    val fullName: String,
    val position: String,
    val phone: String,
    val email: String,
    val photoURL: String,
    val unit: String,
    val userID: String,
    val avatarResId: Int
) : Parcelable