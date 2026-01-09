package com.example.myapplication.data.db

import com.example.myapplication.data.db.entities.CategoryEntity

object SeedData {

    fun defaultCategories(): List<CategoryEntity> = listOf(
        CategoryEntity(name = "Овощи", order = 1),
        CategoryEntity(name = "Фрукты", order = 2),
        CategoryEntity(name = "Крупы и злаки", order = 3),
        CategoryEntity(name = "Мясо", order = 4),
        CategoryEntity(name = "Субпродукты", order = 5),
        CategoryEntity(name = "Рыба", order = 6),
        CategoryEntity(name = "Морепродукты", order = 7),
        CategoryEntity(name = "Молочные продукты", order = 8),
        CategoryEntity(name = "Яйца", order = 9),
        CategoryEntity(name = "Орехи и семечки", order = 10),
        CategoryEntity(name = "Масла и жиры", order = 11),
        CategoryEntity(name = "Напитки", order = 12),
        CategoryEntity(name = "Соусы и специи", order = 13),
        CategoryEntity(name = "Сладкое и перекусы", order = 14)
    )
}
