package com.jsh.stockii

import retrofit2.Call
import retrofit2.http.GET

interface MaskService {
    @GET("sample.json")
    fun fetchStoreInfo(): Call<StoreInfo>
}