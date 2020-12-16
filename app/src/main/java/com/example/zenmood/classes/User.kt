package com.example.zenmood.classes

import android.os.Parcel
import android.os.Parcelable

class User(username: String?, email: String?, preferences: Boolean, level: String?, imgUrl: String?, favs: ArrayList<userFavs>) : Parcelable {
    var username: String = username!!

    var email: String = email!!

    var level: String = level!!

    var preferences: Boolean = preferences

    var imgUrl: String? = imgUrl!!

    var favs: ArrayList<userFavs> = favs

    constructor() : this("", "", false, "", "", arrayListOf())

    constructor(source: Parcel) : this(
    source.readString(),
    source.readString(),
    1 == source.readInt(),
    source.readString(),
    source.readString(),
        source.createTypedArrayList(userFavs.CREATOR) as ArrayList<userFavs>
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(username)
        writeString(email)
        writeInt((if (preferences) 1 else 0))
        writeString(level)
        writeString(imgUrl)
        writeTypedList(favs)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<User> = object : Parcelable.Creator<User> {
            override fun createFromParcel(source: Parcel): User = User(source)
            override fun newArray(size: Int): Array<User?> = arrayOfNulls(size)
        }
    }
}