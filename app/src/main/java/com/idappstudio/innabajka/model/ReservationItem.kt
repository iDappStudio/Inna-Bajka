@file:Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

package com.idappstudio.innabajka.model

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.Timestamp

data class ReservationItem(val imie: String, val data: Timestamp, val miejsc: String, val telefon: String, val uwagi: String, val status: String, val id: String)  : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readParcelable(Timestamp::class.java.classLoader),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(imie)
        parcel.writeParcelable(data, flags)
        parcel.writeString(miejsc)
        parcel.writeString(telefon)
        parcel.writeString(uwagi)
        parcel.writeString(status)
        parcel.writeString(id)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ReservationItem> {
        override fun createFromParcel(parcel: Parcel): ReservationItem {
            return ReservationItem(parcel)
        }

        override fun newArray(size: Int): Array<ReservationItem?> {
            return arrayOfNulls(size)
        }
    }


}