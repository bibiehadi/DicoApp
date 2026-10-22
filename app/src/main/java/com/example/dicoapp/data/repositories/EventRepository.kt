package com.example.dicoapp.data.repositories

import com.example.dicoapp.data.local.dao.FavoriteEventDao
import com.example.dicoapp.data.local.entities.FavoriteEvent
import com.example.dicoapp.data.remote.retrofit.ApiService

class EventRepository private constructor(
    private val apiService: ApiService,
    private val favoriteEventDao: FavoriteEventDao
) {
    fun getEvents(active: Int, query: String? = null) = apiService.getEvents(active, query)

    fun getDetailEvent(id: String) = apiService.getDetailEvent(id)

    suspend fun insertFavorite(favoriteEvent: FavoriteEvent) =
        favoriteEventDao.insert(favoriteEvent)

    suspend fun deleteFavorite(favoriteEvent: FavoriteEvent) =
        favoriteEventDao.delete(favoriteEvent)

    fun getFavoriteById(id: String) = favoriteEventDao.getFavoriteById(id)

    fun getAllFavorites() = favoriteEventDao.getAllFavorites()

    companion object {
        @Volatile
        private var instance: EventRepository? = null

        fun getInstance(
            apiService: ApiService,
            favoriteEventDao: FavoriteEventDao
        ): EventRepository =
            instance ?: synchronized(this) {
                instance ?: EventRepository(apiService, favoriteEventDao).also { instance = it }
            }
    }
}