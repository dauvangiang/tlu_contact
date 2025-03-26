package com.mobile.group.tlucontact.models

import android.os.Parcel
import android.os.Parcelable

data class Student(
    val studentId: String,
    val fullName: String,
    val photoURL: String,
    val phone: String,
    val email: String,
    val address: String,
    val className: String,
    val unit: String,
    val userID: String,
    val avatarResId: Int
) : Parcelable {
    val displayInfo: String
        get() = "$studentId - $className"

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
        parcel.readString().toString(),
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(studentId)
        parcel.writeString(fullName)
        parcel.writeString(photoURL)
        parcel.writeString(phone)
        parcel.writeString(email)
        parcel.writeString(address)
        parcel.writeString(className)
        parcel.writeString(unit)
        parcel.writeString(userID)
        parcel.writeInt(avatarResId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Student> {
        override fun createFromParcel(parcel: Parcel): Student {
            return Student(parcel)
        }

        override fun newArray(size: Int): Array<Student?> {
            return arrayOfNulls(size)
        }
    }
}