package com.example.myapplication.data.db

import com.example.myapplication.data.AppDatabase
import com.example.myapplication.data.db.entities.ProductEntity

object SeedData {

    fun defaultProducts(): List<ProductEntity> = listOf(
        ProductEntity(name = "Куриная грудка", caloriesPer100g = 165, proteinPer100g = 31f, fatPer100g = 3.6f, carbsPer100g = 0f),
        ProductEntity(name = "Говядина", caloriesPer100g = 187, proteinPer100g = 18.9f, fatPer100g = 12.4f, carbsPer100g = 0f),
        ProductEntity(name = "Лосось", caloriesPer100g = 208, proteinPer100g = 20f, fatPer100g = 13f, carbsPer100g = 0f),
        ProductEntity(name = "Творог 5%", caloriesPer100g = 121, proteinPer100g = 17f, fatPer100g = 5f, carbsPer100g = 3f),
        ProductEntity(name = "Молоко 2.5%", caloriesPer100g = 52, proteinPer100g = 3f, fatPer100g = 2.5f, carbsPer100g = 4.8f),
        ProductEntity(name = "Овсяные хлопья", caloriesPer100g = 352, proteinPer100g = 12.3f, fatPer100g = 6.2f, carbsPer100g = 61.8f),
        ProductEntity(name = "Гречка", caloriesPer100g = 343, proteinPer100g = 13.3f, fatPer100g = 3.4f, carbsPer100g = 71.5f),
        ProductEntity(name = "Рис белый", caloriesPer100g = 344, proteinPer100g = 6.7f, fatPer100g = 0.7f, carbsPer100g = 78.9f),
        ProductEntity(name = "Яблоко", caloriesPer100g = 52, proteinPer100g = 0.3f, fatPer100g = 0.2f, carbsPer100g = 14f),
        ProductEntity(name = "Банан", caloriesPer100g = 96, proteinPer100g = 1.5f, fatPer100g = 0.5f, carbsPer100g = 21f),
        ProductEntity(name = "Томаты", caloriesPer100g = 18, proteinPer100g = 0.9f, fatPer100g = 0.2f, carbsPer100g = 3.9f),
        ProductEntity(name = "Огурцы", caloriesPer100g = 15, proteinPer100g = 0.8f, fatPer100g = 0.1f, carbsPer100g = 2.8f)
    )

    suspend fun seedIfEmpty(db: AppDatabase) {
        if (db.productDao().count() == 0) {
            db.productDao().insertAll(defaultProducts())
        }
    }
}