package com.example.gpstrackerexample.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [TrackingItem::class], version = 1)
abstract class TrackingItemDatabase : RoomDatabase() {
    abstract fun trackingItemDao(): TrackingItemDao

    companion object {
        @Volatile
        private var INSTANCE: TrackingItemDatabase? = null

        fun getDatabase(context: Context): TrackingItemDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TrackingItemDatabase::class.java,
                    "tracking_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}