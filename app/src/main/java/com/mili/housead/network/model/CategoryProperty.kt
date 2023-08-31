package com.mili.housead.network.model

import com.mili.housead.overview.filter.model.OptionProperty
import com.squareup.moshi.Json


data class CategoryProperty(
    @Json(name = "id")
    override val id:Int,
    @Json(name = "title")
    override val title:String):OptionProperty()