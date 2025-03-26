package com.mobile.group.tlucontact.models

data class Department(
    val id: String,
    val code: String,
    val name: String,
    val address: String,
    val logoURL: String,
    val phone: String,
    val email: String,
    val fax: String,
    val parentUnit: String,
    val type: String,
    val avatarResId: Int // For local testing with drawable resources
) {
    val firstLetter: String
        get() = name.first().uppercase()
}