package com.example.zenmood.classes

import android.os.Parcel
import android.os.Parcelable

class MeditationItem(name: String?, tipo: String?, nivel: String?, urlVideo: String?, imageUrl: String?) : Parcelable {
    var name: String = ""
    var tipo: String = ""
    var level: String = ""
    var urlVideo: String = ""
    var imageUrl: String = ""

    init {
        this.name = name!!
        this.tipo = tipo!!
        this.level = nivel!!
        this.urlVideo = urlVideo!!
        this.imageUrl = imageUrl!!
    }

    constructor() : this("", "", "", "", "")

    constructor(source: Parcel) : this(
        source.readString(),
        source.readString(),
        source.readString(),
        source.readString(),
        source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(name)
        writeString(tipo)
        writeString(level)
        writeString(urlVideo)
        writeString(imageUrl)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<MeditationItem> =
            object : Parcelable.Creator<MeditationItem> {
                override fun createFromParcel(source: Parcel): MeditationItem =
                    MeditationItem(source)

                override fun newArray(size: Int): Array<MeditationItem?> = arrayOfNulls(size)
            }
    }
}