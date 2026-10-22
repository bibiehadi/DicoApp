package com.example.dicoapp.di

import android.content.Context
import com.example.dicoapp.data.local.room.EventDatabase
import com.example.dicoapp.data.remote.retrofit.ApiConfig
import com.example.dicoapp.data.repositories.EventRepository

object Injection {
    fun provideRepository(context: Context): EventRepository {
        val apiService = ApiConfig.getApiService()
        val dao = EventDatabase.getInstance(context).favoriteEventDao()

        return EventRepository.getInstance(apiService, dao)
    }
}