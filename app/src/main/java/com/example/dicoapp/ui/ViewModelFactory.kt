package com.example.dicoapp.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.dicoapp.data.repositories.EventRepository
import com.example.dicoapp.di.Injection
import com.example.dicoapp.ui.detail_event.DetailEventViewModel
import com.example.dicoapp.ui.finished_event.FinishedViewModel
import com.example.dicoapp.ui.upcoming_event.UpcomingViewModel

class ViewModelFactory private constructor(
    private val repository: EventRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(UpcomingViewModel::class.java) -> UpcomingViewModel(
                repository
            ) as T

            modelClass.isAssignableFrom(FinishedViewModel::class.java) -> FinishedViewModel(
                repository
            ) as T

            modelClass.isAssignableFrom(DetailEventViewModel::class.java) -> DetailEventViewModel(
                repository
            ) as T

            else -> throw IllegalArgumentException("Unknow ViewModel class: ${modelClass.name}")
        }
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null

        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(Injection.provideRepository(context))
                    .also { instance = it }
            }
    }
}