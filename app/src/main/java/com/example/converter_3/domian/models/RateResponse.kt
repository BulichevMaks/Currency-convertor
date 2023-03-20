package com.example.converter_3.domian.models

data class RateResponse(
    val date: String? = null,
    val info: Info? = null,
    val query: Query? = null,
    var result: Double? = null,
    val success: Boolean? = null
)