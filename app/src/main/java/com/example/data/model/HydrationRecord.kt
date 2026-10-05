package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "hydration_records")
data class HydrationRecord(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val date: String, // YYYY-MM-DD
    val amountMl: Int,
    val timestamp: Long = System.currentTimeMillis()
)
