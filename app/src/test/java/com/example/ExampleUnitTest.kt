package com.example

import com.example.data.model.UserProfile
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun testBmiCalculation() {
        // Height 170cm, Weight 65kg -> BMI should be ~ 22.49 (Normal)
        val profile = UserProfile(
            heightCm = 170f,
            currentWeightKg = 65f
        )
        val bmi = profile.bmi
        assertTrue(bmi in 22.4f..22.6f)
        assertEquals("Normal", profile.bmiCategoryEn)
        assertEquals("স্বাভাবিক", profile.bmiCategoryBn)
    }

    @Test
    fun testWeightTargetDifference() {
        val profile = UserProfile(
            currentWeightKg = 65f,
            targetWeightKg = 60f
        )
        assertEquals(5f, profile.weightDiffToTargetKg, 0.001f)
    }
}
