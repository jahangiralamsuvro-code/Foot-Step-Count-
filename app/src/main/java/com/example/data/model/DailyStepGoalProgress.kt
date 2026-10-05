package com.example.data.model

import java.text.NumberFormat
import java.util.Locale

/**
 * Data model representing a user's daily step count goal and real-time tracking progress.
 */
data class DailyStepGoalProgress(
    val currentSteps: Int,
    val targetGoal: Int,
    val date: String = "",
    val strideLengthMeters: Float = 0.70f,
    val userWeightKg: Float = 65f
) {
    init {
        require(targetGoal > 0) { "Target goal must be greater than zero." }
        require(currentSteps >= 0) { "Current steps cannot be negative." }
    }

    /**
     * Normalized progress value between 0.0f and 1.0f (capped for progress bar fill).
     */
    val progressFraction: Float
        get() = (currentSteps.toFloat() / targetGoal.toFloat()).coerceIn(0f, 1f)

    /**
     * Absolute progress ratio (can exceed 1.0f if the goal is exceeded).
     */
    val rawProgressFraction: Float
        get() = currentSteps.toFloat() / targetGoal.toFloat()

    /**
     * Progress percentage as an integer (e.g. 68 for 68%).
     */
    val progressPercentage: Int
        get() = (progressFraction * 100f).toInt()

    /**
     * True if the user has reached or exceeded the daily goal.
     */
    val isGoalAchieved: Boolean
        get() = currentSteps >= targetGoal

    /**
     * Number of steps remaining to reach the daily goal (0 if achieved).
     */
    val remainingSteps: Int
        get() = (targetGoal - currentSteps).coerceAtLeast(0)

    /**
     * Steps completed beyond the target goal (0 if not yet achieved).
     */
    val excessSteps: Int
        get() = (currentSteps - targetGoal).coerceAtLeast(0)

    /**
     * Estimated distance covered in kilometers.
     */
    val distanceCoveredKm: Float
        get() = (currentSteps * strideLengthMeters) / 1000f

    /**
     * Target distance corresponding to the daily step goal in kilometers.
     */
    val targetDistanceKm: Float
        get() = (targetGoal * strideLengthMeters) / 1000f

    /**
     * Remaining distance in kilometers to reach the goal.
     */
    val remainingDistanceKm: Float
        get() = (remainingSteps * strideLengthMeters) / 1000f

    /**
     * Estimated calories burned from current steps (approx 0.043 kcal/step).
     */
    val estimatedCaloriesBurned: Int
        get() = (currentSteps * 0.043f).toInt()

    /**
     * Target calories to burn upon completing the daily step goal.
     */
    val targetCaloriesBurned: Int
        get() = (targetGoal * 0.043f).toInt()

    /**
     * Formatted string representations.
     */
    val formattedCurrentSteps: String
        get() = NumberFormat.getNumberInstance(Locale.US).format(currentSteps)

    val formattedTargetGoal: String
        get() = NumberFormat.getNumberInstance(Locale.US).format(targetGoal)

    val formattedRemainingSteps: String
        get() = NumberFormat.getNumberInstance(Locale.US).format(remainingSteps)

    val formattedExcessSteps: String
        get() = NumberFormat.getNumberInstance(Locale.US).format(excessSteps)

    /**
     * Milestone achievements reached today (25%, 50%, 75%, 100%).
     */
    val reachedMilestones: List<Int>
        get() {
            val list = mutableListOf<Int>()
            if (rawProgressFraction >= 0.25f) list.add(25)
            if (rawProgressFraction >= 0.50f) list.add(50)
            if (rawProgressFraction >= 0.75f) list.add(75)
            if (rawProgressFraction >= 1.00f) list.add(100)
            return list
        }

    /**
     * Motivational status message in English.
     */
    val statusMessageEn: String
        get() = when {
            isGoalAchieved && excessSteps > 0 -> "Incredible! Goal smashed by $formattedExcessSteps extra steps! 🚀"
            isGoalAchieved -> "Daily Goal Achieved! Fantastic work today! 🎉"
            progressFraction >= 0.85f -> "Almost there! Only $formattedRemainingSteps steps to hit your goal!"
            progressFraction >= 0.50f -> "Over halfway there! Keep up the momentum! 💪"
            progressFraction >= 0.25f -> "Good pace! $formattedRemainingSteps steps remaining today."
            currentSteps > 0 -> "Off to a good start! Lace up and take a brisk walk."
            else -> "Ready to reach today's step goal? Let's get moving!"
        }

    /**
     * Motivational status message in Bengali.
     */
    val statusMessageBn: String
        get() = when {
            isGoalAchieved && excessSteps > 0 -> "দুর্দান্ত! লক্ষ্য ছাড়িয়ে আরও $formattedExcessSteps কদম হেঁটেছেন! 🚀"
            isGoalAchieved -> "অভিনন্দন! আজকের কদম লক্ষ্য সফলভাবে পূর্ণ হয়েছে! 🎉"
            progressFraction >= 0.85f -> "খুব কাছে! লক্ষ্য পূরণে আর মাত্র $formattedRemainingSteps কদম বাকি!"
            progressFraction >= 0.50f -> "অর্ধেকের বেশি সম্পন্ন! হাঁটার গতি ধরে রাখুন! 💪"
            progressFraction >= 0.25f -> "দারুণ এগোচ্ছেন! বাকি আর $formattedRemainingSteps কদম।"
            currentSteps > 0 -> "সুন্দর শুরু! সুস্থতার লক্ষ্যে পা বাড়িয়ে এগিয়ে চলুন।"
            else -> "আজকের কদম লক্ষ্য পূরণ করতে প্রস্তুত? চলুন হাঁটা শুরু করি!"
        }

    companion object {
        const val DEFAULT_GOAL = 10000
        val PRESET_GOALS = listOf(5000, 8000, 10000, 12000, 15000)

        fun fromActivityAndProfile(
            activityRecord: DailyActivityRecord?,
            profile: UserProfile
        ): DailyStepGoalProgress {
            val steps = activityRecord?.steps ?: 0
            val goal = activityRecord?.goal ?: profile.dailyStepGoal
            val effectiveGoal = if (goal > 0) goal else DEFAULT_GOAL
            val stride = (profile.heightCm * 0.415f) / 100f
            return DailyStepGoalProgress(
                currentSteps = steps,
                targetGoal = effectiveGoal,
                date = activityRecord?.date ?: "",
                strideLengthMeters = if (stride > 0f) stride else 0.70f,
                userWeightKg = profile.currentWeightKg
            )
        }
    }
}

/**
 * Standard preset tier definitions for step goals.
 */
enum class StepGoalPreset(
    val steps: Int,
    val titleEn: String,
    val titleBn: String,
    val descriptionEn: String,
    val descriptionBn: String
) {
    LIGHT(
        steps = 5000,
        titleEn = "Light & Steady",
        titleBn = "হালকা ও সহজ",
        descriptionEn = "Great for beginners or active recovery days (~3.5 km)",
        descriptionBn = "নতুনদের বা হালকা হাঁটার জন্য আদর্শ (~৩.৫ কিমি)"
    ),
    ACTIVE(
        steps = 8000,
        titleEn = "Daily Active",
        titleBn = "দৈনিক সক্রিয়",
        descriptionEn = "Promotes steady cardio and calorie burn (~5.6 km)",
        descriptionBn = "নিয়মিত কার্ডিও ও সুস্থতার জন্য উপযোগী (~৫.৬ কিমি)"
    ),
    STANDARD(
        steps = 10000,
        titleEn = "Vital 10K",
        titleBn = "আদর্শ ১০ হাজার",
        descriptionEn = "The recommended standard for fitness & weight health (~7.0 km)",
        descriptionBn = "সর্বজনীন আদর্শ ফিটনেস ও স্বাস্থ্য মাত্রা (~৭.০ কিমি)"
    ),
    CHALLENGE(
        steps = 12000,
        titleEn = "Fitness Pro",
        titleBn = "ফিটনেস প্রো",
        descriptionEn = "Advanced endurance and elevated energy burn (~8.4 km)",
        descriptionBn = "উচ্চ স্ট্যামিনা ও অতিরিক্ত ক্যালোরি ক্ষয়ের জন্য (~৮.৪ কিমি)"
    ),
    ATHLETE(
        steps = 15000,
        titleEn = "Power Walker",
        titleBn = "পাওয়ার ওয়াকার",
        descriptionEn = "Peak athletic lifestyle and maximum stamina (~10.5 km)",
        descriptionBn = "অ্যাথলেটিক সহনশীলতা ও সর্বোচ্চ ফিটনেস স্তর (~১০.৫ কিমি)"
    )
}
