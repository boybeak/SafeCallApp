package com.hikingman.safecallapp

import com.github.boybeak.safecall.SafeCall
import okhttp3.internal.http.HttpHeaders
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Query

interface ApiService {
    @GET("photos")
    fun getHotTopics(@Query("page") page: Int, @Query("per_page") perPage: Int): SafeCall<List<Topic>>
}