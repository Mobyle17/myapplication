package com.example.myapplication.data.db.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "products",
    foreignKeys = [
        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.RESTRICT
        )
    ],
    indices = [
        Index("categoryId"),
        Index(value = ["name"], unique = true)
    ]
)
data class ProductEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,

    val categoryId: Long,

    val name: String,          // "Куриная грудка", "Рис", "Молоко 2.5%"
    val brand: String? = null, // опционально

    // Пищевая ценность НА 100 г (или 100 мл для жидкостей — так проще в MVP)
    val kcalPer100: Double,
    val proteinPer100: Double,
    val fatPer100: Double,
    val carbsPer100: Double,

    // Для удобства: единица учета
    val unit: ProductUnit = ProductUnit.GRAM,

    val isActive: Boolean = true
)

enum class ProductUnit { GRAM, ML }
