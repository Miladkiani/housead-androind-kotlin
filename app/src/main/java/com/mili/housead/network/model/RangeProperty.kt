package com.mili.housead.network.model

import com.squareup.moshi.Json

data class RangeProperty(
    @Json(name = "min_rent")
    var minRangeRent:Long?=0,
    @Json(name = "max_rent")
    var maxRangeRent:Long?=0,
    @Json(name = "min_prepayment")
    var minRangePrepayment:Long?=0,
    @Json(name = "max_prepayment")
    var maxRangePrepayment:Long?=0,
    @Json(name = "min_sell")
    var minRangeSell:Long?=0,
    @Json(name = "max_sell")
    var maxRangeSell:Long?=0
)
