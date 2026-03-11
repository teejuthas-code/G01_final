package com.example.heartnote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object HeartnoteClient {
    private const val BASE_URL = "http://10.0.2.2:3000"

    val heartnoteAPI: HeartnoteAPI by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(HeartnoteAPI::class.java)
    }
}