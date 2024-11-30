package com.dicoding.dicodingeventapp.data

import android.app.Application
import androidx.lifecycle.LiveData
import com.dicoding.dicodingeventapp.data.entity.FavoriteEvent
import com.dicoding.dicodingeventapp.data.room.EventDao
import com.dicoding.dicodingeventapp.data.room.EventDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EventRepository(application: Application) {
    private val mEventDao: EventDao = EventDatabase.getInstance(application).eventDao()


    private val coroutineScope = CoroutineScope(Dispatchers.IO)


    fun insert(event: FavoriteEvent) {
        coroutineScope.launch {
            mEventDao.insert(event)
        }
    }

    fun delete(event: FavoriteEvent) {
        coroutineScope.launch {
            mEventDao.delete(event)
        }
    }

    fun getFavoriteEvents(): LiveData<List<FavoriteEvent>> = mEventDao.getFavoriteEvents()

    fun getEventById(id: Int): LiveData<FavoriteEvent> {
        return mEventDao.getEventById(id)
    }

}