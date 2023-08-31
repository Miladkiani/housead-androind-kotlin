package com.mili.housead.network.model

import com.mili.housead.overview.filter.model.OptionProperty
import com.squareup.moshi.Json


data class NeighborhoodProperty(
    @Json(name="id")
    override val id:Int,
    @Json(name="name")
    override val title:String
):OptionProperty()
