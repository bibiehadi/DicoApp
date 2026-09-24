package com.example.dicoapp.ui.finished_event

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dicoapp.data.remote.ApiConfig
import com.example.dicoapp.data.response.GetEventResponse
import com.example.dicoapp.data.response.ListEventsItem
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.time.Duration.Companion.milliseconds

class FinishedViewModel : ViewModel() {

    private val _listEvents = MutableLiveData<List<ListEventsItem>>()
    val listEvents: LiveData<List<ListEventsItem>> = _listEvents

    private val _searchedEvents = MutableLiveData<List<ListEventsItem>>()
    val searchedEvents: LiveData<List<ListEventsItem>> = _searchedEvents


    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _snackBarText = MutableLiveData<String>()
    val snackBarText: LiveData<String> = _snackBarText

    private var searchJob: Job? = null


    companion object {
        private const val TAG = "FinishedViewModel"
    }

    init {
        getFinishedEvents()
    }

    fun onSearchQuery(query: String) {
        searchJob?.cancel()
        if (query.isEmpty()) {
            _listEvents.value = emptyList()
            return
        }

        searchJob = viewModelScope.launch {
            delay(500L.milliseconds)
            getFinishedEvents(query)
        }
    }


    fun getFinishedEvents(query: String? = null) {
        _isLoading.value = true
        Log.d(TAG, "init hit API")
        val response = ApiConfig.getApiService().getEvents(active = 0, query)
        response.enqueue(object : Callback<GetEventResponse> {
            override fun onResponse(
                call: Call<GetEventResponse?>,
                response: Response<GetEventResponse?>
            ) {
                _isLoading.value = false
                if (query == null) {
                    _isLoading.value = false
                    if (response.isSuccessful) {
                        _listEvents.value = response.body()?.listEvents
                    } else {
                        _snackBarText.value = response.message()
                    }
                } else {
                    _isLoading.value = false
                    if (response.isSuccessful) {
                        _searchedEvents.value = response.body()?.listEvents
                    } else {
                        _snackBarText.value = response.message()
                    }
                }
            }

            override fun onFailure(call: Call<GetEventResponse?>, t: Throwable) {
                _isLoading.value = false
                Log.d(TAG, t.message.toString())
                Log.d(TAG, "ERROR")
                _snackBarText.value = t.message.toString()
            }
        })

    }
}