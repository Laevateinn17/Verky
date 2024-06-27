package edu.bluejack23_2.verky.data.model

import android.os.Parcel
import android.os.Parcelable

data class User(
    var id: String? = "",
    var name: String = "",
    var email: String = "",
    var dob: String = "",
    var gender: String = "",
    var religion: String = "",
    var interest: List<String> = emptyList(),
    var incognito_mode: Boolean = false,
    var profile_picture: String = "",
    var gallery_picture: List<String> = emptyList()
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.createStringArrayList() ?: emptyList(),
        parcel.readByte() != 0.toByte(),
        parcel.readString() ?: "",
        parcel.createStringArrayList() ?: emptyList()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeString(email)
        parcel.writeString(dob)
        parcel.writeString(gender)
        parcel.writeString(religion)
        parcel.writeStringList(interest)
        parcel.writeByte(if (incognito_mode) 1 else 0)
        parcel.writeString(profile_picture)
        parcel.writeStringList(gallery_picture)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<User> {
        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }
    }
}