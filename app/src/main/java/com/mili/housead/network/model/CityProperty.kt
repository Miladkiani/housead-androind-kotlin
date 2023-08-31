package com.mili.housead.network.model

import com.squareup.moshi.Json

data class CityProperty(
    @Json(name="id")
    val id:Int,
    @Json(name="name")
    val name:String )