package com.example.dicoapp.data.remote

import com.example.dicoapp.data.response.GetDetailEventResponse
import com.example.dicoapp.data.response.GetEventResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("events")
    fun getEvents(
        @Query("active") active: Int? = null,
        @Query("q") query: String? = null,
    ): Call<GetEventResponse>

    @GET("events/{id}")
    fun getDetailEvent(
        @Path("id") id: String
    ): Call<GetDetailEventResponse>
}
