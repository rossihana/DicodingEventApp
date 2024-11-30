package com.dicoding.dicodingeventapp.ui.viewmodel
import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.dicodingeventapp.data.EventRepository
import com.dicoding.dicodingeventapp.data.entity.FavoriteEvent
import kotlinx.coroutines.launch

class EventDatabaseViewModel(application: Application) : ViewModel() {
    private val mEventRepository: EventRepository = EventRepository(application)

    fun insert(event: FavoriteEvent) {
        viewModelScope.launch {
            mEventRepository.insert(event)
        }
    }

    fun delete(event: FavoriteEvent) {
        viewModelScope.launch {
            mEventRepository.delete(event)
        }
    }

    fun getEventById(id: Int): LiveData<FavoriteEvent> {
        return mEventRepository.getEventById(id)
    }
}