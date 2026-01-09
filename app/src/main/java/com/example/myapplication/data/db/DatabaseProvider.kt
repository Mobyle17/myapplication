package com.example.myapplication.data.db

import android.content.Context
import androidx.room.Room
import com.example.myapplication.data.AppDatabase

object DatabaseProvider {

    @Volatile
    private var INSTANCE: AppDatabase? = null

    fun get(context: Context): AppDatabase {
        return INSTANCE ?: synchronized(this) {
            INSTANCE ?: Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "health.db"
            ).build().also { INSTANCE = it }
        }
    }
}
