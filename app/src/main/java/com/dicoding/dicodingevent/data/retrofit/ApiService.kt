package com.dicoding.dicodingevent.data.retrofit

import com.dicoding.dicodingevent.data.response.DetailEventResponse
import com.dicoding.dicodingevent.data.response.EventResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("events")
    fun getEvents(
        @Query("active") active: Int = 1, // 1: aktif, 0: selesai, -1: semua
        @Query("q") query: String? = null,
        @Query("limit") limit: Int = 40
    ): Call<EventResponse>

    @GET("events/{id}")
    fun getEventDetail(
        @Path("id") eventId: Int
    ): Call<DetailEventResponse> // Ganti dengan kelas yang sesuai
}