package com.mobile.group.tlucontact.models

import android.os.Parcel
import android.os.Parcelable

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
) : Parcelable {
    val displayPosition: String
        get() = "$position - $unit"

    val firstLetter: String
        get() = fullName.first().uppercase()

    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(staffId)
        parcel.writeString(fullName)
        parcel.writeString(position)
        parcel.writeString(phone)
        parcel.writeString(email)
        parcel.writeString(photoURL)
        parcel.writeString(unit)
        parcel.writeString(userID)
        parcel.writeInt(avatarResId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Staff> {
        override fun createFromParcel(parcel: Parcel): Staff {
            return Staff(parcel)
        }

        override fun newArray(size: Int): Array<Staff?> {
            return arrayOfNulls(size)
        }
    }
}