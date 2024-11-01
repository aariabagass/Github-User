package com.ariabagas.githubuserapp.utils.services

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object API {
    private const val URL = "https://api.github.com/"
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
        redactHeader("Authorization")
    }
    private var client: OkHttpClient =
        OkHttpClient.Builder().addInterceptor(loggingInterceptor).build()

    val retrofit = Retrofit.Builder()
        .baseUrl(URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiRequest = retrofit.create(ApiServices::class.java)
}