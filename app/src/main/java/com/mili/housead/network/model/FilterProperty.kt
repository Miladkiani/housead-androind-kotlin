package com.mili.housead.network.model

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize


data class FilterProperty (@Json(name = "category")
                            val category:MutableList<Int> = arrayListOf(),
                           @Json(name = "feature")
                            val feature:MutableList<Int> = arrayListOf(),
                           @Json(name = "room")
                            val room:MutableList<Int> = arrayListOf(),
                           @Json(name = "neighborhood")
                           val neighborhood:MutableList<Int> = arrayListOf(),
                           @Json(name = "sell")
                            var sell: RangeFilterProperty = RangeFilterProperty(),
                           @Json(name = "prepayment")
                            var prepayment: RangeFilterProperty = RangeFilterProperty(),
                           @Json(name = "rent")
                            var rent: RangeFilterProperty = RangeFilterProperty(),
                           @Json(name = "word")
                            var word: String=""
)

data class RangeFilterProperty(
    @Json(name = "gt")
    var min:Long?=null,
    @Json(name = "lt")
    var max:Long?=null
)

