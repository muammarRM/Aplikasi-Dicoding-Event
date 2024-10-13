package com.dicoding.dicodingevent.ui.upcoming

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.dicodingevent.data.response.EventResponse
import com.dicoding.dicodingevent.data.response.ListEventsItem
import com.dicoding.dicodingevent.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpcomingViewModel : ViewModel() {
    private val _upcomingEvent = MutableLiveData<List<ListEventsItem>>()
    val upcomingEvent: LiveData<List<ListEventsItem>> = _upcomingEvent

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getUpcomingEvent() {
        _isLoading.value = true
        ApiConfig.getApiService().getAllActiveEvent().enqueue(object : Callback<EventResponse> {
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _upcomingEvent.value = response.body()?.listEvents
                } else {
                    _errorMessage.value = "Failed to load upcoming events"
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                _isLoading.value = false
                _errorMessage.value = t.message.toString()
            }
        })
    }
}
