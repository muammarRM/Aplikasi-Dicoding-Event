package com.dicoding.dicodingevent.data

import android.util.Log
import androidx.lifecycle.LiveData
import com.dicoding.dicodingevent.data.local.entity.EventEntity
import com.dicoding.dicodingevent.data.local.room.EventDao
import com.dicoding.dicodingevent.data.remote.response.Event
import com.dicoding.dicodingevent.data.remote.response.ListEventsItem
import com.dicoding.dicodingevent.data.remote.retrofit.ApiService

class EventRepository private constructor(
    private val apiService: ApiService,
    private val eventDao: EventDao
) {

    // Mendapatkan acara yang akan datang dengan LiveData
    suspend fun getUpcomingEvent(): kotlin.Result<List<ListEventsItem>> {
        return try {
            val response = apiService.getAllActiveEvent()
            if (response.isSuccessful) {
                kotlin.Result.success(response.body()?.listEvents ?: emptyList())
            } else {
                kotlin.Result.failure(Exception("Failed to load data from API, Status code: ${response.code()}"))
            }
        } catch (e: Exception) {
            kotlin.Result.failure(e)
        }
    }
    suspend fun getCompletedEvent(): kotlin.Result<List<ListEventsItem>> {
        return try {
            val response = apiService.getAllCompletedEvent()
            if (response.isSuccessful) {
                kotlin.Result.success(response.body()?.listEvents ?: emptyList())
            } else {
                kotlin.Result.failure(Exception("Failed to load data from API, Status code: ${response.code()}"))
            }
        } catch (e: Exception) {
            kotlin.Result.failure(e)
        }
    }

    suspend fun getDetailEvent(id: Int): kotlin.Result<Event> {
        return try {
            val response = apiService.getEventDetail(id)
            if (response.isSuccessful) {
                val event = response.body()?.event ?: throw Exception("Event not found")
                kotlin.Result.success(event)
            } else {
                kotlin.Result.failure(Exception("Failed to load data from API, Status code: ${response.code()}"))
            }
        } catch (e: Exception) {
            kotlin.Result.failure(e)
        }
    }

    suspend fun searchEvent(keyword: String): kotlin.Result<List<ListEventsItem>> {
        return try {
            val response = apiService.searchEvent(keyword)
            if (response.isSuccessful) {
                kotlin.Result.success(response.body()?.listEvents ?: emptyList())
            } else {
                kotlin.Result.failure(Exception("Failed to load data from API, Status code: ${response.code()}"))
            }
        } catch (e: Exception) {
            kotlin.Result.failure(e)
        }
    }

    suspend fun insertEvent(event: EventEntity): Boolean {
        return try {
            eventDao.insertEvents(event)
            Log.d("Insert EventRepository", "Event inserted successfully: $event")
            true
        } catch (e: Exception) {
            Log.e("Insert EventRepository", "Failed to insert event: ${e.message}")
            false
        }
    }


    suspend fun deleteEvent(event: EventEntity): Boolean {
        return try {
            eventDao.deleteEvent(event)
            Log.d("Delete EventRepository", "Event deleted successfully: $event")
            true
        } catch (e: Exception) {
            Log.e("Delete EventRepository", "Failed to delete event: ${e.message}")
            false
        }
    }

    fun getEvent(): LiveData<List<EventEntity>> {
        return eventDao.getEvents()
    }

    fun getEventById(eventId: Int): LiveData<EventEntity> {
        return eventDao.getEventById(eventId)
    }

    companion object {
        @Volatile
        private var instance: EventRepository? = null

        fun getInstance(apiService: ApiService, eventDao: EventDao): EventRepository =
            instance ?: synchronized(this) {
                instance ?: EventRepository(apiService, eventDao)
            }.also { instance = it }
    }
}
