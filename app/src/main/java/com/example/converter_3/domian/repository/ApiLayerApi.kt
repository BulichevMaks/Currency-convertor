package com.example.converter_3.domian.repository


import com.example.converter_3.domian.models.RateResponse
import retrofit2.Call
import retrofit2.Response

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface ApiLayerApi {

    @GET("/exchangerates_data/latest")
    suspend fun getRateFromUSD(
        @Header("apikey") token: String,
        @Query("symbols") symbols: String,
        @Query("base") base: String
    ): Response<RateResponse>

}