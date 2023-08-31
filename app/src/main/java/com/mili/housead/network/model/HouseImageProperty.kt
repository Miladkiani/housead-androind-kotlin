package com.mili.housead.network.model

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class HouseImageProperty(
    @Json(name = "original")
    val original:String,
    @Json(name = "thumbnail")
    val thumbnail:String
):Parcelable