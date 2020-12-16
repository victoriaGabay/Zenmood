package com.example.zenmood.classes

import android.os.Parcel
import android.os.Parcelable

class userFavs(name: String?, type: String?) : Parcelable {
    var name: String? = name

    var type: String? = type

    constructor() : this("", "")

    constructor(source: Parcel) : this(
        source.readString(),
        source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(name)
        writeString(type)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<userFavs> = object : Parcelable.Creator<userFavs> {
            override fun createFromParcel(source: Parcel): userFavs = userFavs(source)
            override fun newArray(size: Int): Array<userFavs?> = arrayOfNulls(size)
        }
    }
}