package com.dicoding.dicodingevent.ui.completed

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.dicodingevent.data.response.EventResponse
import com.dicoding.dicodingevent.data.response.ListEventsItem
import com.dicoding.dicodingevent.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CompletedViewModel : ViewModel() {
    private val _completedEvent = MutableLiveData<List<ListEventsItem>>()
    val completedEvent: LiveData<List<ListEventsItem>> = _completedEvent

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getCompletedEvent() {
        _isLoading.value = true
        ApiConfig.getApiService().getAllCompletedEvent().enqueue(object : Callback<EventResponse> {
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _completedEvent.value = response.body()?.listEvents
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
}
