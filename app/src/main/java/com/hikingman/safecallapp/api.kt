package com.hikingman.safecallapp

import com.github.boybeak.safecall.SafeCallAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private val apiService: ApiService = Retrofit.Builder()
    .apply {
        baseUrl("https://api.unsplash.com/")
        addConverterFactory(GsonConverterFactory.create())
        addCallAdapterFactory(SafeCallAdapterFactory())
        client(
            OkHttpClient.Builder()
                .addInterceptor {
                    it.proceed(it.request().newBuilder().addHeader("authorization", "Client-ID 2UmADJIJPl0KKahuQBH4CsFI4i4DK6s3taPeFi1i1tA")
                        .build())
                }
                .build()
        )
    }
    .build().create(ApiService::class.java)
fun api(): ApiService {
    return apiService
}