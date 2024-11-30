package com.dicoding.dicodingeventapp.ui.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dicoding.dicodingeventapp.data.EventRepository
import com.dicoding.dicodingeventapp.data.entity.FavoriteEvent

class FavoriteEventViewModel(application: Application) : ViewModel() {
    private val mEventRepository: EventRepository = EventRepository(application)

    fun getFavoriteEvents(): LiveData<List<FavoriteEvent>> {
        return mEventRepository.getFavoriteEvents()


    }
}