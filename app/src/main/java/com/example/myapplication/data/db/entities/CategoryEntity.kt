package com.example.myapplication.data.db.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "categories",
    indices = [Index(value = ["name"], unique = true)]
)
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,          // "Овощи", "Мясо", "Морепродукты"...
    val order: Int = 0,        // для сортировки в списке
    val isActive: Boolean = true
)
