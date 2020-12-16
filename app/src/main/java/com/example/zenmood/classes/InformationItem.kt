package com.example.zenmood.classes

import android.os.Parcel
import android.os.Parcelable

class InformationItem(name: String?, imageUrl: String?, text: String?) : Parcelable {
    var name: String = ""

    var imageUrl: String = ""

    var text: String = ""

    init {
        this.name = name!!
        this.imageUrl = imageUrl!!
        this.text = text!!
    }

    constructor() : this("", "", "")

    constructor(source: Parcel) : this(
        source.readString(),
        source.readString(),
        source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(name)
        writeString(imageUrl)
        writeString(text)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<InformationItem> =
            object : Parcelable.Creator<InformationItem> {
                override fun createFromParcel(source: Parcel): InformationItem =
                    InformationItem(source)

                override fun newArray(size: Int): Array<InformationItem?> = arrayOfNulls(size)
            }
    }
}