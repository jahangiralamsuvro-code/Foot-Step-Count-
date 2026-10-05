package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_quotes")
data class FavoriteQuoteEntity(
    @PrimaryKey
    val quoteId: Int
)

data class HealthTip(
    val id: Int,
    val titleEn: String,
    val titleBn: String,
    val contentEn: String,
    val contentBn: String,
    val category: String, // Nutrition, Walking, Sleep, Mind, Hydration, Heart
    val timeOfDay: String, // Morning, Afternoon, Evening, Night
    val imageResName: String? = null
)

data class FoodItem(
    val id: Int,
    val nameEn: String,
    val nameBn: String,
    val category: String, // Breakfast, Lunch, Dinner, Snacks
    val calories: Int,
    val proteinG: Float,
    val carbsG: Float,
    val fatG: Float,
    val servingEn: String,
    val servingBn: String,
    val tipsEn: List<String>,
    val tipsBn: List<String>,
    val imageResName: String? = null
)

data class ExerciseItem(
    val id: Int,
    val titleEn: String,
    val titleBn: String,
    val category: String, // Beginner, Walking, Stretching, Mobility
    val durationMin: Int,
    val difficulty: String, // Easy, Medium, High
    val caloriesEstimate: Int,
    val summaryEn: String,
    val summaryBn: String,
    val stepsListEn: List<String>,
    val stepsListBn: List<String>,
    val safetyTipsEn: String,
    val safetyTipsBn: String,
    val imageResName: String? = null
)

data class MotivationQuote(
    val id: Int,
    val quoteEn: String,
    val quoteBn: String,
    val author: String = "VitalStep Wellness",
    val isFavorite: Boolean = false
)
