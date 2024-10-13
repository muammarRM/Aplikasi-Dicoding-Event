package com.dicoding.dicodingevent.data.retrofit

import com.dicoding.dicodingevent.data.response.DetailEventResponse
import com.dicoding.dicodingevent.data.response.EventResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("events")
    fun getAllActiveEvent(
        @Query("active") active: Int = 1,
    ): Call<EventResponse>

    @GET("events")
    fun getAllCompletedEvent(
        @Query("active") active: Int = 0,
    ): Call<EventResponse>

    @GET("events")
    fun searchEvent(
        @Query("q") keyword: String,
        @Query("active") active: Int = -1,
    ): Call<EventResponse>

    @GET("events/{id}")
    fun getEventDetail(
        @Path("id") eventId: Int
    ): Call<DetailEventResponse>
}