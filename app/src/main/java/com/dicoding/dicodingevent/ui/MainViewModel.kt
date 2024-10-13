package com.dicoding.dicodingevent.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.dicodingevent.data.response.EventResponse
import com.dicoding.dicodingevent.data.response.ListEventsItem
import com.dicoding.dicodingevent.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {

    private val _upcomingEvent = MutableLiveData<List<ListEventsItem>>()
    val upcomingEvent: LiveData<List<ListEventsItem>> = _upcomingEvent

    private val _completedEvent = MutableLiveData<List<ListEventsItem>>()
    val completedEvent: LiveData<List<ListEventsItem>> = _completedEvent

    private val _detailEvent = MutableLiveData<List<ListEventsItem>>()
    val detailEvent: LiveData<List<ListEventsItem>> = _detailEvent

    private val _searchEvent = MutableLiveData<List<ListEventsItem>>()
    val searchEvent: LiveData<List<ListEventsItem>> = _searchEvent

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    companion object{
        private const val TAG = "MainViewModel"
    }
    init{
        getUpcomingEvent()
        getCompletedEvent()
    }

    private fun getUpcomingEvent() {
        _isLoading.value = true
        val getUpcomingEvent = ApiConfig.getApiService().getAllActiveEvent()
        getUpcomingEvent.enqueue(object : Callback<EventResponse> {
            override fun onResponse(
                call: Call<EventResponse>,
                response: Response<EventResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _upcomingEvent.value = response.body()?.listEvents
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun getCompletedEvent() {
        _isLoading.value = true
        val getCompletedEvent = ApiConfig.getApiService().getAllCompletedEvent()
        getCompletedEvent.enqueue(object : Callback<EventResponse> {
            override fun onResponse(
                call: Call<EventResponse>,
                response: Response<EventResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _completedEvent.value = response.body()?.listEvents
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }
}