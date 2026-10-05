package com.example.data.model

data class UserProfile(
    val name: String = "Rahul Das",
    val email: String = "rahul@example.com",
    val age: Int = 28,
    val heightCm: Float = 170f,
    val currentWeightKg: Float = 65f,
    val targetWeightKg: Float = 60f,
    val gender: String = "Male",
    val activityLevel: String = "Moderately Active",
    val dailyStepGoal: Int = 10000,
    val dailyWaterGoalMl: Int = 2500,
    val preferredLanguage: String = "en", // "en" or "bn"
    val isMetric: Boolean = true,
    val isDarkMode: Boolean = true,
    val hasCompletedOnboarding: Boolean = false,
    val isGoogleSignedIn: Boolean = false,
    val lastBackupTimestamp: Long? = null
) {
    val bmi: Float
        get() {
            val heightM = heightCm / 100f
            return if (heightM > 0f) currentWeightKg / (heightM * heightM) else 0f
        }

    val bmiCategoryEn: String
        get() = when {
            bmi < 18.5f -> "Underweight"
            bmi < 25.0f -> "Normal"
            bmi < 30.0f -> "Overweight"
            else -> "Obese"
        }

    val bmiCategoryBn: String
        get() = when {
            bmi < 18.5f -> "স্বাভাবিকের চেয়ে কম"
            bmi < 25.0f -> "স্বাভাবিক"
            bmi < 30.0f -> "অতিরিক্ত ওজন"
            else -> "স্থূলতা"
        }

    val healthyWeightRangeMinKg: Float
        get() {
            val heightM = heightCm / 100f
            return 18.5f * (heightM * heightM)
        }

    val healthyWeightRangeMaxKg: Float
        get() {
            val heightM = heightCm / 100f
            return 24.9f * (heightM * heightM)
        }

    val weightDiffToTargetKg: Float
        get() = currentWeightKg - targetWeightKg
}
