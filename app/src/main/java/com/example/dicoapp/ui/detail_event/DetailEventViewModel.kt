package com.example.dicoapp.ui.detail_event

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dicoapp.data.remote.ApiConfig
import com.example.dicoapp.data.response.Event
import com.example.dicoapp.data.response.GetDetailEventResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailEventViewModel : ViewModel() {
    private val _dataEvent = MutableLiveData<Event>()
    val dataEvent: LiveData<Event> = _dataEvent

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _snackBarText = MutableLiveData<String>()
    val snackBarText: LiveData<String> = _snackBarText

    companion object {
        private const val TAG = "EventDetailViewModel"
    }

    fun getDetailEvent(id: String) {
        _isLoading.value = true
        val response = ApiConfig.getApiService().getDetailEvent(id)
        response.enqueue(object : Callback<GetDetailEventResponse> {
            override fun onResponse(
                call: Call<GetDetailEventResponse?>,
                response: Response<GetDetailEventResponse?>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _dataEvent.value = response.body()?.event
                } else {
                    _snackBarText.value = response.message()
                }
            }

            override fun onFailure(call: Call<GetDetailEventResponse?>, t: Throwable) {
                _isLoading.value = false
                _snackBarText.value = t.message.toString()
            }
        })

    }
}