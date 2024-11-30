package com.dicoding.dicodingeventapp.data.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.dicoding.dicodingeventapp.data.entity.FavoriteEvent

@Dao
interface EventDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(event: FavoriteEvent)

    @Update
    suspend fun update(event: FavoriteEvent)

    @Delete
    suspend fun delete(event: FavoriteEvent)

    @Query("SELECT * FROM favoriteevent WHERE id = :id LIMIT 1")
    fun getEventById(id: Int): LiveData<FavoriteEvent>

    @Query("SELECT * from favoriteevent WHERE isFavorite = 1")
    fun getFavoriteEvents(): LiveData<List<FavoriteEvent>>
}