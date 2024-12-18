package com.example.gpstrackerexample.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TrackingItemDao {

    @Query("SELECT * FROM TrackingItem")
    fun getAll(): LiveData<List<TrackingItem>>

    @Insert
    suspend fun insert (trackingItem: TrackingItem)

}