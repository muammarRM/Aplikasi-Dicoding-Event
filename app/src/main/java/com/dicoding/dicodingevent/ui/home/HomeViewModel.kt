package com.dicoding.dicodingevent.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.dicodingevent.data.response.EventResponse
import com.dicoding.dicodingevent.data.response.ListEventsItem
import com.dicoding.dicodingevent.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel : ViewModel() {

    private val _upcomingEvent = MutableLiveData<List<ListEventsItem>>()
    val upcomingEvent: LiveData<List<ListEventsItem>> = _upcomingEvent

    private val _completedEvents = MutableLiveData<List<ListEventsItem>>()
    val completedEvents: LiveData<List<ListEventsItem>> = _completedEvents

    private val _searchResults = MutableLiveData<List<ListEventsItem>>()
    val searchResults: LiveData<List<ListEventsItem>> = _searchResults

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun getUpcomingEvent() {
        _isLoading.value = true
        val getUpcomingEvent = ApiConfig.getApiService().getAllActiveEvent()
        getUpcomingEvent.enqueue(object : Callback<EventResponse> {
            override fun onResponse(
                call: Call<EventResponse>,
                response: Response<EventResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _upcomingEvent.value = response.body()?.listEvents?.take(5)
                } else {
                    _errorMessage.value = "Failed to load completed events"
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                _isLoading.value = false
                _errorMessage.value = t.message.toString()
            }
        })
    }

    fun getCompletedEvents() {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getAllCompletedEvent()
        client.enqueue(object : Callback<EventResponse> {
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _completedEvents.value = response.body()?.listEvents?.take(5)
                } else {
                    _errorMessage.value = "Failed to load completed events"
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                _isLoading.value = false
                _errorMessage.value = t.message.toString()
            }
        })
    }

    fun searchEvents(keyword: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().searchEvent(keyword)
        client.enqueue(object : Callback<EventResponse> {
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _searchResults.value = response.body()?.listEvents
                } else {
                    _errorMessage.value = "Failed to search events"
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                _isLoading.value = false
                _errorMessage.value = t.message.toString()
            }
        })
    }

}
