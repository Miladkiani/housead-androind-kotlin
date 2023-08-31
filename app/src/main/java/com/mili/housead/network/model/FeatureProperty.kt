package com.mili.housead.network.model

import android.os.Parcelable
import com.mili.housead.overview.filter.model.OptionProperty
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FeatureProperty(
    @Json(name = "id")
   override val id:Int,
    @Json(name = "title")
   override val title:String)
    :Parcelable,OptionProperty()