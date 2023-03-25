package com.example.converter_3.domian.models

data class RateResponse(
    val success: Boolean,
    val timestamp: Long,
    val base: String,
    val date: String,
    val rates: Map<String, Double>
)
//{
//    "success": true,
//    "timestamp": 1679697123,
//    "base": "USD",
//    "date": "2023-03-24",
//    "rates": {
//    "AED": 3.672415,
//    "AFN": 86.751053,
//    "ALL": 106.529226
//    }
//}