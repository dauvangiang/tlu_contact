data class Contact(
    val id: String,
    val name: String,
    val position: String,
    val department: String,
    val avatarResId: Int // Thay đổi từ avatarUrl thành avatarResId
) {
    val displayPosition: String
        get() = "$position - $department"

    val firstLetter: String
        get() = name.first().uppercase()
}