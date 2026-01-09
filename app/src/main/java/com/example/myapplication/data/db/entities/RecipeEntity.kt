package com.example.myapplication.data.db.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "recipes",
    indices = [Index(value = ["name"], unique = true)]
)
data class RecipeEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,              // "Омлет", "Гречка с курицей"
    val description: String? = null,
    val createdAtMillis: Long = System.currentTimeMillis(),
    val isActive: Boolean = true
)
