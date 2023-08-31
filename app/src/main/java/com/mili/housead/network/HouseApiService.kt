package com.mili.housead.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.mili.housead.network.model.*
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*

private const val BASE_URL = "https://miladkiani.ir/api/web/v1/"

private val moshi= Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(BASE_URL)
    .build()

interface HouseApiService{
    @GET("houses")
    fun getPropertiesAsync(@Query("page") page:Int,
                           @Query("word") word:String):
        Deferred<HouseItems>
    @GET("features")
    fun getFeaturesAsync():
            Deferred<List<FeatureProperty>>
    @GET("categories")
    fun getCategoriesAsync():
            Deferred<List<CategoryProperty>>
    @GET("houses/range")
    fun getRangeAsync():
            Deferred<RangeProperty>
    @POST("houses/search")
    fun getFilterPropertiesAsync(
        @Body filterProperty: FilterProperty,
        @Query ("page") page: Int):
            Deferred<HouseItems>
    @GET("cities")
    fun getCitiesAsync():
            Deferred<List<CityProperty>>
    @GET("neighborhoods")
    fun getNeighborhoodByCityAsync(@Query("cityId") cityId:Int):
            Deferred<List<NeighborhoodProperty>>
}

object HouseApi{
    val retrofitService : HouseApiService by lazy {
        retrofit.create(HouseApiService::class.java)
    }
}

