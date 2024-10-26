package com.dicoding.dicodingevent.di

import android.content.Context
import com.dicoding.dicodingevent.data.EventRepository
import com.dicoding.dicodingevent.data.local.room.EventDatabase
import com.dicoding.dicodingevent.data.remote.retrofit.ApiConfig
import com.dicoding.dicodingevent.ui.setting.SettingPreferences
import com.dicoding.dicodingevent.ui.setting.dataStore


object Injection {
    fun provideRepository(context: Context): EventRepository {
        val apiService = ApiConfig.getApiService()
        val database = EventDatabase.getInstance(context)
        val dao = database.eventDao()
        return EventRepository.getInstance(apiService, dao)
    }

    fun providePreferences(context: Context): SettingPreferences {
        return SettingPreferences.getInstance(context.dataStore)
    }
}