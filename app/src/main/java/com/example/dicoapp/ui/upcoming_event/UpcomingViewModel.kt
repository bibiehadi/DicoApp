package com.example.dicoapp.ui.upcoming_event

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dicoapp.data.remote.ApiConfig
import com.example.dicoapp.data.response.ListEventsItem
import kotlinx.coroutines.launch

class UpcomingViewModel : ViewModel() {

    private val _listEvents = MutableLiveData<List<ListEventsItem>>()
    val listEvents: LiveData<List<ListEventsItem>> = _listEvents

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _snackbarText = MutableLiveData<String>()
    val snackbarText: LiveData<String> = _snackbarText

    init {
        getUpcomingEvents()
    }

    fun getUpcomingEvents() {
        _isLoading.value = true

        viewModelScope.launch {
            try {
                val response = ApiConfig.getApiService().getEvents(active = 1)
                _listEvents.value = response.listEvents ?: emptyList()
            } catch (e: Exception) {
                _snackbarText.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }
}