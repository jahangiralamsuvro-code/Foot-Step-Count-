package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "daily_activity")
data class DailyActivityRecord(
    @PrimaryKey
    val date: String, // Format: YYYY-MM-DD
    val steps: Int = 0,
    val goal: Int = 10000,
    val distanceKm: Float = 0f,
    val caloriesBurned: Int = 0,
    val activeMinutes: Int = 0,
    val updatedAt: Long = System.currentTimeMillis()
) {
    val completionPercentage: Float
        get() = if (goal > 0) (steps.toFloat() / goal.toFloat()).coerceIn(0f, 1f) else 0f

    val isGoalCompleted: Boolean
        get() = steps >= goal
}
