package com.example

import com.example.data.model.DailyActivityRecord
import com.example.data.model.DailyStepGoalProgress
import com.example.data.model.StepGoalPreset
import com.example.data.model.UserProfile
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class DailyStepGoalProgressTest {

    @Test
    fun testProgressCalculations() {
        val model = DailyStepGoalProgress(
            currentSteps = 6000,
            targetGoal = 10000,
            strideLengthMeters = 0.70f
        )

        assertEquals(0.60f, model.progressFraction, 0.001f)
        assertEquals(60, model.progressPercentage)
        assertEquals(4000, model.remainingSteps)
        assertEquals(0, model.excessSteps)
        assertFalse(model.isGoalAchieved)
        assertEquals(listOf(25, 50), model.reachedMilestones)
        assertEquals(4.2f, model.distanceCoveredKm, 0.01f)
        assertEquals(7.0f, model.targetDistanceKm, 0.01f)
    }

    @Test
    fun testGoalAchievedAndExcessSteps() {
        val model = DailyStepGoalProgress(
            currentSteps = 12500,
            targetGoal = 10000,
            strideLengthMeters = 0.70f
        )

        assertTrue(model.isGoalAchieved)
        assertEquals(1.0f, model.progressFraction, 0.001f)
        assertEquals(1.25f, model.rawProgressFraction, 0.001f)
        assertEquals(100, model.progressPercentage)
        assertEquals(0, model.remainingSteps)
        assertEquals(2500, model.excessSteps)
        assertEquals(listOf(25, 50, 75, 100), model.reachedMilestones)
        assertTrue(model.statusMessageEn.contains("Incredible! Goal smashed"))
        assertTrue(model.statusMessageBn.contains("দুর্দান্ত! লক্ষ্য ছাড়িয়ে"))
    }

    @Test
    fun testFromActivityAndProfile() {
        val activity = DailyActivityRecord(
            date = "2026-10-04",
            steps = 7500,
            goal = 8000
        )
        val profile = UserProfile(
            heightCm = 175f,
            dailyStepGoal = 8000
        )

        val progress = DailyStepGoalProgress.fromActivityAndProfile(activity, profile)
        assertEquals(7500, progress.currentSteps)
        assertEquals(8000, progress.targetGoal)
        assertEquals(500, progress.remainingSteps)
        assertFalse(progress.isGoalAchieved)
        assertEquals(listOf(25, 50, 75), progress.reachedMilestones)
    }

    @Test
    fun testPresetTiers() {
        assertEquals(5, StepGoalPreset.values().size)
        assertEquals(5000, StepGoalPreset.LIGHT.steps)
        assertEquals(8000, StepGoalPreset.ACTIVE.steps)
        assertEquals(10000, StepGoalPreset.STANDARD.steps)
        assertEquals(12000, StepGoalPreset.CHALLENGE.steps)
        assertEquals(15000, StepGoalPreset.ATHLETE.steps)
    }
}
