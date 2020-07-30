package com.jsh.stockii

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MaskService {
    @GET("sample.json")
    suspend fun fetchStoreInfo(@Query("lat") lat: Double,
                        @Query("lng") lng: Double): StoreInfo
}