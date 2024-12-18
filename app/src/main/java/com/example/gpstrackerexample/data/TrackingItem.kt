package com.example.gpstrackerexample.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "TrackingItem")
data class TrackingItem (
    @PrimaryKey(autoGenerate = true) val trackingId : Int = 0,
    val startTime : String?,
    val latitude : String?,
    val longitude : String?
)