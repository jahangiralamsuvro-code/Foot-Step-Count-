package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "unlocked_achievements")
data class AchievementUnlockEntity(
    @PrimaryKey
    val key: String,
    val unlockedAt: Long = System.currentTimeMillis()
)

enum class AchievementCategory {
    STEPS,
    STREAKS,
    SPECIAL
}

data class AchievementItem(
    val key: String,
    val targetThreshold: Int,
    val category: AchievementCategory,
    val titleEn: String,
    val titleBn: String,
    val descEn: String,
    val descBn: String,
    val isUnlocked: Boolean = false,
    val unlockedAt: Long? = null,
    val iconName: String = "directions_walk"
) {
    val progressLabel: String
        get() = when (category) {
            AchievementCategory.STEPS -> "${targetThreshold / 1000}K Steps"
            AchievementCategory.STREAKS -> "$targetThreshold Days"
            AchievementCategory.SPECIAL -> "Special"
        }
}
