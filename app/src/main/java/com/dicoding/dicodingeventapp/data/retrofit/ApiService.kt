package com.dicoding.dicodingeventapp.data.retrofit

import com.dicoding.dicodingeventapp.data.response.DetailEventResponse
import com.dicoding.dicodingeventapp.data.response.DicodingEventResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("events")
    fun getEvents(@Query("active") active: Int): Call<DicodingEventResponse>

    @GET("events")
    fun getSearchEvents(
        @Query("active") active: Int = 0,
        @Query("q") keyword: String
    ): Call<DicodingEventResponse>

    @GET("events/{id}")
    fun getDetailEvents(@Path("id") id: Int): Call<DetailEventResponse>

    @GET("events")
    suspend fun getNearestActiveEvent(
        @Query("active")active: Int = -1,
        @Query("limit")limit: Int = 1
    ) : DicodingEventResponse
}