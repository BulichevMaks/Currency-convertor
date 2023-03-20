package com.example.converter_3.domian.repository


import com.example.converter_3.domian.models.RateResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface ApiLayerApi {
    @GET("/exchangerates_data/convert")
    fun getRate(
        @Header("apikey") token: String,
        @Query("to") to: String,
        @Query("from") from: String,
        @Query("amount") amount: Int,
    ): Call<RateResponse>
}