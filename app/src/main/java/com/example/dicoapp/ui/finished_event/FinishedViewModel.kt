package com.example.dicoapp.ui.finished_event

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dicoapp.data.remote.response.GetEventResponse
import com.example.dicoapp.data.remote.response.ListEventsItem
import com.example.dicoapp.data.repositories.EventRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.time.Duration.Companion.milliseconds

class FinishedViewModel(private val repository: EventRepository) : ViewModel() {

    private val _listEvents = MutableLiveData<List<ListEventsItem>>()
    val listEvents: LiveData<List<ListEventsItem>> = _listEvents

    private val _searchedEvents = MutableLiveData<List<ListEventsItem>>()
    val searchedEvents: LiveData<List<ListEventsItem>> = _searchedEvents


    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _snackBarText = MutableLiveData<String>()
    val snackBarText: LiveData<String> = _snackBarText

    private var searchJob: Job? = null

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
        val response = repository.getEvents(active = 0, query)
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
                _snackBarText.value = t.message.toString()
            }
        })

    }
}