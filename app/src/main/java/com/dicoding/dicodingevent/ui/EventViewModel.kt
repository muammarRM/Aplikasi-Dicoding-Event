package com.dicoding.dicodingevent.ui

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dicoding.dicodingevent.data.EventRepository
import com.dicoding.dicodingevent.data.local.entity.EventEntity
import com.dicoding.dicodingevent.data.remote.response.Event
import com.dicoding.dicodingevent.data.remote.response.ListEventsItem
import com.dicoding.dicodingevent.ui.setting.SettingPreferences
import kotlinx.coroutines.launch

class EventViewModel(
    private val eventRepository: EventRepository,
    private val pref: SettingPreferences
) : ViewModel() {

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _upcomingEvent = MutableLiveData<List<ListEventsItem>>()
    val upcomingEvent: LiveData<List<ListEventsItem>> = _upcomingEvent

    private val _completedEvent = MutableLiveData<List<ListEventsItem>>()
    val completedEvent: LiveData<List<ListEventsItem>> = _completedEvent

    private val _detailEvent = MutableLiveData<Event>()
    val detailEvent: LiveData<Event> = _detailEvent

    private val _searchEvent = MutableLiveData<List<ListEventsItem>>()
    val searchEvent: LiveData<List<ListEventsItem>> = _searchEvent

    private val _allEvent = MutableLiveData<List<EventEntity>>()
    val allEvent: LiveData<List<EventEntity>> get() = _allEvent

    fun getUpcomingEvent() {
        _isLoading.value = true
        viewModelScope.launch {
            val result = eventRepository.getUpcomingEvent()
            _isLoading.value = false
            result.onSuccess {
                _upcomingEvent.value = it
                _errorMessage.value = null
            }.onFailure {
                _errorMessage.value = it.message
            }
        }
    }

    fun getCompletedEvent() {
        _isLoading.value = true
        viewModelScope.launch {
            val result = eventRepository.getCompletedEvent()
            _isLoading.value = false
            result.onSuccess {
                _completedEvent.value = it
                _errorMessage.value = null
            }.onFailure {
                _errorMessage.value = it.message
            }
        }
    }

    fun getDetailEvent(id: Int) {
        _isLoading.value = true
        viewModelScope.launch {
            val result = eventRepository.getDetailEvent(id)
            _isLoading.value = false
            result.onSuccess {
                _detailEvent.value = it
                _errorMessage.value = null
            }.onFailure {
                _errorMessage.value = it.message
            }
        }
    }

    fun searchEvent(keyword: String) {
        _isLoading.value = true
        viewModelScope.launch {
            val result = eventRepository.searchEvent(keyword)
            _isLoading.value = false
            result.onSuccess {
                _searchEvent.value = it
                _errorMessage.value = null
            }.onFailure {
                _errorMessage.value = it.message
            }
        }
    }
    fun getAllEvent() {
        _isLoading.value = true
        eventRepository.getEvent().observeForever { favoriteEvents ->
            Log.d("MainViewModel", "Favorite Events: $favoriteEvents")
            _isLoading.value = false
            _allEvent.value = favoriteEvents
            _errorMessage.value = null
        }
    }
    fun insertEvent(event: EventEntity) {
        viewModelScope.launch {
            val success = eventRepository.insertEvent(event)
            if (!success) {
                _errorMessage.value = "Failed to insert favorite event"
            }
        }
    }

    fun deleteEvent(event: EventEntity) {
        viewModelScope.launch {
            val success = eventRepository.deleteEvent(event)
            if (!success) {
                _errorMessage.value = "Failed to delete favorite event"
            }
        }
    }

    fun getEventById(id: Int): LiveData<EventEntity> {
        return eventRepository.getEventById(id)
    }

    fun getThemeSettings(context: Context): LiveData<Boolean> {
        return pref.getThemeSetting(context).asLiveData()
    }
    fun getDailyReminderSetting(): LiveData<Boolean> {
        return pref.getDailyReminderSetting().asLiveData()
    }

    fun saveDailyReminderSetting(isReminderActive: Boolean) {
        viewModelScope.launch {
            pref.saveDailyReminderSetting(isReminderActive)
        }
    }
    fun saveThemeSetting(isDarkModeActive: Boolean) {
        viewModelScope.launch {
            pref.saveThemeSetting(isDarkModeActive)
        }
    }
}
