package com.example.converter_3.domian.models

import android.util.Log
import com.example.converter_3.domian.repository.ApiLayerApi
import com.google.gson.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type
import java.util.concurrent.TimeUnit



object RatesActual {
    private const val apiLayerBaseUrl = "https://api.apilayer.com/"
    private var token = "your token:)"

    private val retrofit: Retrofit by lazy {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .build()
        val gsonBuilder = GsonBuilder()
            .registerTypeAdapter(RateResponse::class.java, RateResponseJsonAdapter())
        val gson = gsonBuilder.create()

        Retrofit.Builder().client(okHttpClient)
            .baseUrl(apiLayerBaseUrl)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    private val apiLayerService: ApiLayerApi by lazy {
        retrofit.create(ApiLayerApi::class.java)
    }
    val rateFromRUB by lazy { runBlocking { search("RUB") } }
    val rateFromEUR by lazy { runBlocking { search("EUR") } }
    val rateFromRSD by lazy { runBlocking { search("RSD") } }
    val rateFromUSD by lazy { runBlocking { search("USD") } }

    init {
        GlobalScope.launch {
            rateFromRUB
             rateFromEUR
             rateFromRSD
             rateFromUSD
        }
    }
    private suspend fun search(base: String): List<Pair<String, Double>>? {
        val symbols = "AED, AFN, ALL, AMD, ANG, AOA, ARS, AUD, AWG, AZN, BAM, BBD, BDT, BGN, BHD, BIF, BMD, BND, BOB, BRL, BSD, BTC, BTN, BWP, BYN, BYR, BZD, CAD, CDF, CHF, CLF, CLP, CNY, COP, CRC, CUC, CUP, CVE, CZK, DJF, DKK, DOP, DZD, EGP, ERN, ETB, EUR, FJD, FKP, GBP, GEL, GGP, GHS, GIP, GMD, GNF, GTQ, GYD, HKD, HNL, HRK, HTG, HUF, IDR, ILS, IMP, INR, IQD, IRR, ISK, JEP, JMD, JOD, JPY, KES, KGS, KHR, KMF, KPW, KRW, KWD, KYD, KZT, LAK, LBP, LKR, LRD, LSL, LTL, LVL, LYD, MAD, MDL, MGA, MKD, MMK, MNT, MOP, MRO, MUR, MVR, MWK, MXN, MYR, MZN, NAD, NGN, NIO, NOK, NPR, NZD, OMR, PAB, PEN, PGK, PHP, PKR, PLN, PYG, QAR, RON, RSD, RUB, RWF, SAR, SBD, SCR, SDG, SEK, SGD, SHP, SLE, SLL, SOS, SRD, STD, SVC, SYP, SZL, THB, TJS, TMT, TND, TOP, TRY, TTD, TWD, TZS, UAH, UGX, USD, UYU, UZS, VEF, VES, VND, VUV, WST, XAF, XAG, XAU, XCD, XDR, XOF, XPF, YER, ZAR, ZMK, ZMW, ZWL"
        return try {
            val response = apiLayerService.getRateFromUSD(token, symbols, base)
            if (response.isSuccessful) {
                val body = response.body()
                body?.let {
                    it.rates.map { entry ->
                        entry.key to entry.value
                    }
                }
            } else {
                // Handle error
                null
            }
        } catch (e: Exception) {
            // Handle error
            null
        }
    }

    private fun showMessage(text: String, event: Event) {
        when (event) {
            Event.SUCCESS -> {
                Log.d("MY_LOG", text)
            }
            Event.NOTHING_FOUND -> {
                Log.d("MY_LOG", text)
            }
            Event.ERROR -> {
                Log.d("MY_LOG", text)
            }
            Event.SERVER_ERROR -> {
                Log.d("MY_LOG", text)
            }
            else -> {
                Log.d("MY_LOG", text)
            }
        }
    }
}
class RateResponseJsonAdapter : JsonSerializer<RateResponse>, JsonDeserializer<RateResponse> {
    override fun serialize(src: RateResponse?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
        val jsonObject = JsonObject()
        jsonObject.addProperty("success", src?.success)
        jsonObject.addProperty("timestamp", src?.timestamp)
        jsonObject.addProperty("base", src?.base)
        jsonObject.addProperty("date", src?.date)

        val ratesJsonObject = JsonObject()
        src?.rates?.forEach { (currencyCode, rate) ->
            ratesJsonObject.addProperty(currencyCode, rate)
        }
        jsonObject.add("rates", ratesJsonObject)

        return jsonObject
    }

    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): RateResponse {
        val jsonObject = json?.asJsonObject ?: JsonObject()

        val success = jsonObject.get("success")?.asBoolean ?: false
        val timestamp = jsonObject.get("timestamp")?.asLong ?: 0L
        val base = jsonObject.get("base")?.asString ?: ""
        val date = jsonObject.get("date")?.asString ?: ""

        val ratesJsonObject = jsonObject.getAsJsonObject("rates")
        val rates = mutableMapOf<String, Double>()
        ratesJsonObject.entrySet().forEach { entry ->
            val currencyCode = entry.key
            val rate = entry.value.asDouble
            rates[currencyCode] = rate
        }
        return RateResponse(success, timestamp, base, date, rates)
    }
}