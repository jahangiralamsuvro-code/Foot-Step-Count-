package com.example.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.data.model.UserProfile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "vitalstep_preferences")

data class NotificationSettings(
    val morningTipEnabled: Boolean = true,
    val morningTipTime: String = "07:00 AM",
    val waterReminderEnabled: Boolean = true,
    val waterReminderTime: String = "10:00 AM",
    val walkingReminderEnabled: Boolean = true,
    val walkingReminderTime: String = "04:00 PM",
    val goalAchievementEnabled: Boolean = true,
    val eveningWellnessEnabled: Boolean = true,
    val eveningWellnessTime: String = "07:00 PM",
    val sleepReminderEnabled: Boolean = true,
    val sleepReminderTime: String = "10:00 PM"
)

class PreferencesManager(private val context: Context) {

    companion object {
        val KEY_NAME = stringPreferencesKey("user_name")
        val KEY_EMAIL = stringPreferencesKey("user_email")
        val KEY_AGE = intPreferencesKey("user_age")
        val KEY_HEIGHT = floatPreferencesKey("user_height")
        val KEY_CURRENT_WEIGHT = floatPreferencesKey("user_current_weight")
        val KEY_TARGET_WEIGHT = floatPreferencesKey("user_target_weight")
        val KEY_GENDER = stringPreferencesKey("user_gender")
        val KEY_ACTIVITY_LEVEL = stringPreferencesKey("user_activity_level")
        val KEY_STEP_GOAL = intPreferencesKey("daily_step_goal")
        val KEY_WATER_GOAL = intPreferencesKey("daily_water_goal")
        val KEY_LANGUAGE = stringPreferencesKey("preferred_language")
        val KEY_DARK_MODE = booleanPreferencesKey("is_dark_mode")
        val KEY_METRIC = booleanPreferencesKey("is_metric")
        val KEY_ONBOARDING_DONE = booleanPreferencesKey("onboarding_completed")
        val KEY_GOOGLE_SIGNED_IN = booleanPreferencesKey("is_google_signed_in")
        val KEY_LAST_BACKUP = longPreferencesKey("last_backup_time")

        // Notifications
        val KEY_NOTIF_MORNING = booleanPreferencesKey("notif_morning")
        val KEY_NOTIF_WATER = booleanPreferencesKey("notif_water")
        val KEY_NOTIF_WALK = booleanPreferencesKey("notif_walk")
        val KEY_NOTIF_GOAL = booleanPreferencesKey("notif_goal")
        val KEY_NOTIF_EVENING = booleanPreferencesKey("notif_evening")
        val KEY_NOTIF_SLEEP = booleanPreferencesKey("notif_sleep")

        // Sensor baseline offset
        val KEY_SENSOR_OFFSET = intPreferencesKey("sensor_cumulative_offset")
        val KEY_LAST_RECORDED_DATE = stringPreferencesKey("last_recorded_date")
    }

    val userProfile: Flow<UserProfile> = context.dataStore.data.map { prefs ->
        UserProfile(
            name = prefs[KEY_NAME] ?: "Rahul Das",
            email = prefs[KEY_EMAIL] ?: "rahul@example.com",
            age = prefs[KEY_AGE] ?: 28,
            heightCm = prefs[KEY_HEIGHT] ?: 170f,
            currentWeightKg = prefs[KEY_CURRENT_WEIGHT] ?: 65f,
            targetWeightKg = prefs[KEY_TARGET_WEIGHT] ?: 60f,
            gender = prefs[KEY_GENDER] ?: "Male",
            activityLevel = prefs[KEY_ACTIVITY_LEVEL] ?: "Moderately Active",
            dailyStepGoal = prefs[KEY_STEP_GOAL] ?: 10000,
            dailyWaterGoalMl = prefs[KEY_WATER_GOAL] ?: 2500,
            preferredLanguage = prefs[KEY_LANGUAGE] ?: "en",
            isMetric = prefs[KEY_METRIC] ?: true,
            isDarkMode = prefs[KEY_DARK_MODE] ?: true,
            hasCompletedOnboarding = prefs[KEY_ONBOARDING_DONE] ?: false,
            isGoogleSignedIn = prefs[KEY_GOOGLE_SIGNED_IN] ?: false,
            lastBackupTimestamp = prefs[KEY_LAST_BACKUP]
        )
    }

    val notificationSettings: Flow<NotificationSettings> = context.dataStore.data.map { prefs ->
        NotificationSettings(
            morningTipEnabled = prefs[KEY_NOTIF_MORNING] ?: true,
            waterReminderEnabled = prefs[KEY_NOTIF_WATER] ?: true,
            walkingReminderEnabled = prefs[KEY_NOTIF_WALK] ?: true,
            goalAchievementEnabled = prefs[KEY_NOTIF_GOAL] ?: true,
            eveningWellnessEnabled = prefs[KEY_NOTIF_EVENING] ?: true,
            sleepReminderEnabled = prefs[KEY_NOTIF_SLEEP] ?: true
        )
    }

    suspend fun updateUserProfile(profile: UserProfile) {
        context.dataStore.edit { prefs ->
            prefs[KEY_NAME] = profile.name
            prefs[KEY_EMAIL] = profile.email
            prefs[KEY_AGE] = profile.age
            prefs[KEY_HEIGHT] = profile.heightCm
            prefs[KEY_CURRENT_WEIGHT] = profile.currentWeightKg
            prefs[KEY_TARGET_WEIGHT] = profile.targetWeightKg
            prefs[KEY_GENDER] = profile.gender
            prefs[KEY_ACTIVITY_LEVEL] = profile.activityLevel
            prefs[KEY_STEP_GOAL] = profile.dailyStepGoal
            prefs[KEY_WATER_GOAL] = profile.dailyWaterGoalMl
            prefs[KEY_LANGUAGE] = profile.preferredLanguage
            prefs[KEY_METRIC] = profile.isMetric
            prefs[KEY_DARK_MODE] = profile.isDarkMode
            prefs[KEY_ONBOARDING_DONE] = profile.hasCompletedOnboarding
            prefs[KEY_GOOGLE_SIGNED_IN] = profile.isGoogleSignedIn
            profile.lastBackupTimestamp?.let { prefs[KEY_LAST_BACKUP] = it }
        }
    }

    suspend fun setOnboardingCompleted(completed: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[KEY_ONBOARDING_DONE] = completed
        }
    }

    suspend fun setLanguage(language: String) {
        context.dataStore.edit { prefs ->
            prefs[KEY_LANGUAGE] = language
        }
    }

    suspend fun setDarkMode(enabled: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[KEY_DARK_MODE] = enabled
        }
    }

    suspend fun setDailyStepGoal(goal: Int) {
        context.dataStore.edit { prefs ->
            prefs[KEY_STEP_GOAL] = goal
        }
    }

    suspend fun setGoogleSignIn(signedIn: Boolean, email: String? = null, name: String? = null) {
        context.dataStore.edit { prefs ->
            prefs[KEY_GOOGLE_SIGNED_IN] = signedIn
            if (email != null) prefs[KEY_EMAIL] = email
            if (name != null) prefs[KEY_NAME] = name
        }
    }

    suspend fun updateLastBackupTime(timestamp: Long) {
        context.dataStore.edit { prefs ->
            prefs[KEY_LAST_BACKUP] = timestamp
        }
    }

    suspend fun updateNotificationSetting(key: String, enabled: Boolean) {
        context.dataStore.edit { prefs ->
            when (key) {
                "morning" -> prefs[KEY_NOTIF_MORNING] = enabled
                "water" -> prefs[KEY_NOTIF_WATER] = enabled
                "walk" -> prefs[KEY_NOTIF_WALK] = enabled
                "goal" -> prefs[KEY_NOTIF_GOAL] = enabled
                "evening" -> prefs[KEY_NOTIF_EVENING] = enabled
                "sleep" -> prefs[KEY_NOTIF_SLEEP] = enabled
            }
        }
    }

    suspend fun clearAllPreferences() {
        context.dataStore.edit { prefs ->
            prefs.clear()
        }
    }
}
