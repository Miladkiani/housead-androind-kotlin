package com.mili.housead.network.model

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize



@Parcelize
data class HouseProperty(
    @Json(name = "id")
    val id: Int,
    @Json(name = "title")
    val title: String,
    @Json(name = "description")
    val description: String?,
    @Json(name = "category")
    val category: String,
    @Json(name = "city")
    val city: String,
    @Json(name = "neighborhood")
    val neighborhood:String,
    @Json(name = "size")
    val size: Int,
    @Json(name = "room")
    val room: Int,
    @Json(name = "furniture")
    val furniture: Int,
    @Json(name = "features")
    val features:List<FeatureProperty>?,
    @Json(name = "lease_type")
    val lease_type: Int,
    @Json(name = "sell")
    val sell:Long?,
    @Json(name = "prepayment")
    val prepayment:Long?,
    @Json(name = "rent")
    val rent:Long?,
    @Json(name = "created_at")
    val created_at: Int,
    @Json(name = "updated_at")
    val updated_at:Int,
    @Json(name="author_name")
    val author_name:String,
    @Json(name="author_phone")
    val author_phone:String,
    @Json(name = "images")
    val images:List<HouseImageProperty>
):Parcelable{
}



data class Meta(
    @Json(name = "totalCount")
    val totalCount:Int,
    @Json(name = "pageCount")
    val pageCount:Int,
    @Json(name = "currentPage")
    var currentPage:Int,
    @Json(name = "perPage")
    val perPage:Int
)

data class HouseItems(
    @Json(name = "items")
    val items: List<HouseProperty>,
    @Json(name = "_meta")
    val meta: Meta
)