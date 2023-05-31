package com.example.laundrysimply.model.response.layanan

import android.os.Parcel
import android.os.Parcelable

data class LayananItem(val kuantitas: Int, val namaLayanan: String) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(kuantitas)
        parcel.writeString(namaLayanan)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<LayananItem> {
        override fun createFromParcel(parcel: Parcel): LayananItem {
            return LayananItem(parcel)
        }

        override fun newArray(size: Int): Array<LayananItem?> {
            return arrayOfNulls(size)
        }
    }
}

