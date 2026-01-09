package com.example.myapplication.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.myapplication.data.db.entities.CategoryEntity
import com.example.myapplication.data.db.entities.ProductEntity
import com.example.myapplication.data.db.entities.RecipeEntity
import com.example.myapplication.data.db.entities.RecipeItemEntity

@Database(
    entities = [
        CategoryEntity::class,
        ProductEntity::class,
        RecipeEntity::class,
        RecipeItemEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(RoomConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun categoryDao(): com.example.myapplication.data.db.dao.CategoryDao
    abstract fun productDao(): com.example.myapplication.data.db.dao.ProductDao
    abstract fun recipeDao(): com.example.myapplication.data.db.dao.RecipeDao
}
