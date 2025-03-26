import android.os.Parcel
import android.os.Parcelable

data class Contact(
    val id: String,
    val name: String,
    val phone: String,
    val email: String,
    val position: String,
    val department: String,
    val avatarResId: Int // Thay đổi từ avatarUrl thành avatarResId
) : Parcelable {
    val displayPosition: String
        get() = "$position - $department"

    val firstLetter: String
        get() = name.first().uppercase()

    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeString(phone)
        parcel.writeString(email)
        parcel.writeString(position)
        parcel.writeString(department)
        parcel.writeInt(avatarResId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Contact> {
        override fun createFromParcel(parcel: Parcel): Contact {
            return Contact(parcel)
        }

        override fun newArray(size: Int): Array<Contact?> {
            return arrayOfNulls(size)
        }
    }
}