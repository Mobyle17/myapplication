package com.example.myapplication.data

import androidx.room.TypeConverter
import com.example.myapplication.data.db.entities.ProductUnit

class RoomConverters {

    @TypeConverter
    fun fromProductUnit(value: ProductUnit): String = value.name

    @TypeConverter
    fun toProductUnit(value: String): ProductUnit = ProductUnit.valueOf(value)
}
