package com.example.data.repository

import com.example.data.local.PreferencesManager
import com.example.data.local.VitalStepDao
import com.example.data.model.AchievementCategory
import com.example.data.model.AchievementItem
import com.example.data.model.AchievementUnlockEntity
import com.example.data.model.DailyActivityRecord
import com.example.data.model.FavoriteQuoteEntity
import com.example.data.model.HydrationRecord
import com.example.data.model.UserProfile
import com.example.data.model.WeightRecord
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class VitalStepRepository(
    private val dao: VitalStepDao,
    private val preferencesManager: PreferencesManager
) {
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)

    fun getTodayDateString(): String = dateFormat.format(Date())

    val userProfile: Flow<UserProfile> = preferencesManager.userProfile
    val notificationSettings = preferencesManager.notificationSettings

    fun getTodayActivity(): Flow<DailyActivityRecord?> {
        val today = getTodayDateString()
        return dao.getActivityForDate(today)
    }

    fun getAllActivityHistory(): Flow<List<DailyActivityRecord>> = dao.getAllActivityHistory()

    fun getLast7DaysActivity(): Flow<List<DailyActivityRecord>> = dao.getLast7DaysActivity()

    fun getLast30DaysActivity(): Flow<List<DailyActivityRecord>> = dao.getLast30DaysActivity()

    suspend fun getOrCreateTodayActivity(): DailyActivityRecord {
        val today = getTodayDateString()
        val existing = dao.getActivityForDateSync(today)
        if (existing != null) return existing

        val profile = preferencesManager.userProfile.first()
        val initial = DailyActivityRecord(
            date = today,
            steps = 0,
            goal = profile.dailyStepGoal,
            distanceKm = 0f,
            caloriesBurned = 0,
            activeMinutes = 0
        )
        dao.insertOrUpdateActivity(initial)
        return initial
    }

    suspend fun recordSteps(newStepsCount: Int, isIncremental: Boolean = false) {
        val today = getTodayDateString()
        val profile = preferencesManager.userProfile.first()
        val current = dao.getActivityForDateSync(today) ?: DailyActivityRecord(
            date = today,
            steps = 0,
            goal = profile.dailyStepGoal
        )

        val totalSteps = if (isIncremental) {
            current.steps + newStepsCount
        } else {
            newStepsCount
        }

        // Stride calculation: height in cm * 0.415 for average human stride
        val strideLengthMeters = (profile.heightCm * 0.415f) / 100f
        val distanceKm = (totalSteps * strideLengthMeters) / 1000f
        // Calories estimate: roughly 0.042 kcal per step for 65-70kg adult
        val calories = (totalSteps * 0.043f).toInt()
        // Active minutes: roughly 100 steps per active brisk minute
        val activeMin = (totalSteps / 105).coerceAtLeast(0)

        val updatedRecord = current.copy(
            steps = totalSteps,
            distanceKm = distanceKm,
            caloriesBurned = calories,
            activeMinutes = activeMin,
            updatedAt = System.currentTimeMillis()
        )
        dao.insertOrUpdateActivity(updatedRecord)

        // Evaluate and trigger achievements
        evaluateAchievements(totalSteps)
    }

    suspend fun resetTodayActivity() {
        val today = getTodayDateString()
        val profile = preferencesManager.userProfile.first()
        dao.insertOrUpdateActivity(
            DailyActivityRecord(
                date = today,
                steps = 0,
                goal = profile.dailyStepGoal,
                distanceKm = 0f,
                caloriesBurned = 0,
                activeMinutes = 0,
                updatedAt = System.currentTimeMillis()
            )
        )
    }

    suspend fun seedInitialDataIfEmpty() {
        val today = getTodayDateString()
        val currentToday = dao.getActivityForDateSync(today)
        // If today was previously seeded with 6248 steps, reset it to 0 for a genuine user experience
        if (currentToday != null && currentToday.steps == 6248) {
            resetTodayActivity()
        }

        val existing = dao.getAllActivityHistory().first()
        if (existing.isNotEmpty()) return

        val calendar = Calendar.getInstance()
        val profile = preferencesManager.userProfile.first()

        // Seed last 6 days with realistic active records to give user an immediate rich experience
        val mockData = listOf(
            Triple(7420, 5.2f, 340),
            Triple(8910, 6.3f, 410),
            Triple(10250, 7.4f, 475),
            Triple(6180, 4.4f, 290),
            Triple(9400, 6.7f, 430),
            Triple(8200, 5.9f, 380)
        )

        for (i in mockData.indices) {
            calendar.time = Date()
            calendar.add(Calendar.DAY_OF_YEAR, -(6 - i))
            val dateStr = dateFormat.format(calendar.time)
            val (st, dist, cal) = mockData[i]
            dao.insertOrUpdateActivity(
                DailyActivityRecord(
                    date = dateStr,
                    steps = st,
                    goal = profile.dailyStepGoal,
                    distanceKm = dist,
                    caloriesBurned = cal,
                    activeMinutes = (st / 105)
                )
            )
        }

        // Today starts genuinely at 0 steps!
        dao.insertOrUpdateActivity(
            DailyActivityRecord(
                date = today,
                steps = 0,
                goal = profile.dailyStepGoal,
                distanceKm = 0f,
                caloriesBurned = 0,
                activeMinutes = 0
            )
        )

        // Seed initial weight history
        dao.insertWeightRecord(WeightRecord(date = "2025-07-01", weightKg = 67.5f, note = "Starting weight"))
        dao.insertWeightRecord(WeightRecord(date = "2025-08-01", weightKg = 66.8f, note = "Steady walking"))
        dao.insertWeightRecord(WeightRecord(date = "2025-09-01", weightKg = 65.9f, note = "Feeling active"))
        dao.insertWeightRecord(WeightRecord(date = "2025-10-01", weightKg = 65.0f, note = "Current progress"))

        // Unlock 1K, 3K, 5K, 6K achievements matching the mockup
        dao.unlockAchievement(AchievementUnlockEntity("step_1k"))
        dao.unlockAchievement(AchievementUnlockEntity("step_2k"))
        dao.unlockAchievement(AchievementUnlockEntity("step_3k"))
        dao.unlockAchievement(AchievementUnlockEntity("step_5k"))
        dao.unlockAchievement(AchievementUnlockEntity("step_6k"))
        dao.unlockAchievement(AchievementUnlockEntity("streak_1d"))
        dao.unlockAchievement(AchievementUnlockEntity("streak_3d"))

        // Seed initial hydration today: 1500ml (68% of 2500ml)
        dao.insertHydration(HydrationRecord(date = today, amountMl = 250))
        dao.insertHydration(HydrationRecord(date = today, amountMl = 500))
        dao.insertHydration(HydrationRecord(date = today, amountMl = 250))
        dao.insertHydration(HydrationRecord(date = today, amountMl = 500))
    }

    private suspend fun evaluateAchievements(todaySteps: Int) {
        HealthDataProvider.achievementsList.forEach { achievement ->
            if (achievement.category == AchievementCategory.STEPS && todaySteps >= achievement.targetThreshold) {
                dao.unlockAchievement(AchievementUnlockEntity(achievement.key))
            }
        }
    }

    fun getAchievements(): Flow<List<AchievementItem>> {
        return combine(dao.getAllUnlockedAchievements()) { unlockedList ->
            val unlockedMap = unlockedList.firstOrNull()?.associate { it.key to it.unlockedAt } ?: emptyMap()
            HealthDataProvider.achievementsList.map { item ->
                val isUnlocked = unlockedMap.containsKey(item.key)
                item.copy(
                    isUnlocked = isUnlocked,
                    unlockedAt = unlockedMap[item.key]
                )
            }
        }
    }

    // Hydration
    fun getTodayHydration(): Flow<Int> = dao.getTotalHydrationForDate(getTodayDateString())

    fun getTodayHydrationList(): Flow<List<HydrationRecord>> = dao.getHydrationForDate(getTodayDateString())

    suspend fun addWater(amountMl: Int) {
        val today = getTodayDateString()
        dao.insertHydration(HydrationRecord(date = today, amountMl = amountMl))
        val total = dao.getTotalHydrationForDate(today).first()
        if (total >= 2500) {
            dao.unlockAchievement(AchievementUnlockEntity("water_hero"))
        }
    }

    suspend fun clearTodayWater() {
        dao.clearHydrationForDate(getTodayDateString())
    }

    // Weight
    fun getAllWeightRecords(): Flow<List<WeightRecord>> = dao.getAllWeightRecords()

    suspend fun recordWeight(weightKg: Float, note: String = "") {
        val today = getTodayDateString()
        dao.insertWeightRecord(WeightRecord(date = today, weightKg = weightKg, note = note))
        val profile = preferencesManager.userProfile.first()
        preferencesManager.updateUserProfile(profile.copy(currentWeightKg = weightKg))
    }

    suspend fun deleteWeightRecord(id: Long) {
        dao.deleteWeightRecord(id)
    }

    // Favorites
    fun getFavoriteQuoteIds(): Flow<List<Int>> = dao.getFavoriteQuoteIds()

    suspend fun toggleFavoriteQuote(id: Int, isFav: Boolean) {
        if (isFav) {
            dao.removeFavoriteQuote(id)
        } else {
            dao.addFavoriteQuote(FavoriteQuoteEntity(id))
        }
    }

    // Settings & Profile
    suspend fun updateProfile(profile: UserProfile) {
        preferencesManager.updateUserProfile(profile)
        // Also update today's activity goal
        val today = getTodayDateString()
        val activity = dao.getActivityForDateSync(today)
        if (activity != null) {
            dao.insertOrUpdateActivity(activity.copy(goal = profile.dailyStepGoal))
        }
    }

    suspend fun setLanguage(lang: String) = preferencesManager.setLanguage(lang)

    suspend fun setDarkMode(enabled: Boolean) = preferencesManager.setDarkMode(enabled)

    suspend fun setDailyStepGoal(goal: Int) {
        preferencesManager.setDailyStepGoal(goal)
        val today = getTodayDateString()
        val activity = dao.getActivityForDateSync(today)
        if (activity != null) {
            dao.insertOrUpdateActivity(activity.copy(goal = goal))
        }
    }

    suspend fun toggleGoogleSignIn(email: String? = null, name: String? = null) {
        val profile = preferencesManager.userProfile.first()
        val newState = !profile.isGoogleSignedIn
        preferencesManager.setGoogleSignIn(
            signedIn = newState,
            email = if (newState) (email ?: "rahul@example.com") else "guest@vitalstep.local",
            name = if (newState) (name ?: "Rahul Das") else "Guest User"
        )
        if (newState) {
            preferencesManager.updateLastBackupTime(System.currentTimeMillis())
        }
    }

    // Export Data to CSV
    suspend fun exportDataAsCsv(): String {
        val history = dao.getAllActivityHistory().first()
        val weights = dao.getAllWeightRecords().first()

        val sb = StringBuilder()
        sb.append("=== VITALSTEP WELLNESS REPORT ===\n")
        sb.append("Export Date: ${SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).format(Date())}\n\n")

        sb.append("--- DAILY ACTIVITY HISTORY ---\n")
        sb.append("Date,Steps,Goal,Distance(km),Calories(kcal),ActiveTime(min),Completed\n")
        history.forEach { record ->
            sb.append("${record.date},${record.steps},${record.goal},${String.format(Locale.US, "%.2f", record.distanceKm)},${record.caloriesBurned},${record.activeMinutes},${if (record.isGoalCompleted) "YES" else "NO"}\n")
        }

        sb.append("\n--- WEIGHT RECORDS ---\n")
        sb.append("Date,Weight(kg),Note\n")
        weights.forEach { w ->
            sb.append("${w.date},${w.weightKg},${w.note}\n")
        }

        return sb.toString()
    }

    // Backup to JSON
    suspend fun exportDataAsJson(): String {
        val root = JSONObject()
        val history = dao.getAllActivityHistory().first()
        val weights = dao.getAllWeightRecords().first()
        val profile = preferencesManager.userProfile.first()

        root.put("app", "VitalStep")
        root.put("version", "1.0")
        root.put("timestamp", System.currentTimeMillis())

        val userObj = JSONObject()
        userObj.put("name", profile.name)
        userObj.put("email", profile.email)
        userObj.put("stepGoal", profile.dailyStepGoal)
        userObj.put("heightCm", profile.heightCm)
        userObj.put("weightKg", profile.currentWeightKg)
        root.put("userProfile", userObj)

        val histArray = JSONArray()
        history.forEach {
            val item = JSONObject()
            item.put("date", it.date)
            item.put("steps", it.steps)
            item.put("goal", it.goal)
            item.put("distanceKm", it.distanceKm.toDouble())
            item.put("calories", it.caloriesBurned)
            item.put("activeMin", it.activeMinutes)
            histArray.put(item)
        }
        root.put("activityHistory", histArray)

        val weightArray = JSONArray()
        weights.forEach {
            val item = JSONObject()
            item.put("date", it.date)
            item.put("weightKg", it.weightKg.toDouble())
            item.put("note", it.note)
            weightArray.put(item)
        }
        root.put("weightRecords", weightArray)

        preferencesManager.updateLastBackupTime(System.currentTimeMillis())
        return root.toString(2)
    }

    suspend fun restoreDataFromJson(jsonString: String): Boolean {
        return try {
            val root = JSONObject(jsonString)
            if (!root.has("app") || root.getString("app") != "VitalStep") return false

            if (root.has("activityHistory")) {
                val histArray = root.getJSONArray("activityHistory")
                for (i in 0 until histArray.length()) {
                    val obj = histArray.getJSONObject(i)
                    val rec = DailyActivityRecord(
                        date = obj.getString("date"),
                        steps = obj.getInt("steps"),
                        goal = obj.optInt("goal", 10000),
                        distanceKm = obj.optDouble("distanceKm", 0.0).toFloat(),
                        caloriesBurned = obj.optInt("calories", 0),
                        activeMinutes = obj.optInt("activeMin", 0)
                    )
                    dao.insertOrUpdateActivity(rec)
                }
            }

            if (root.has("weightRecords")) {
                val weightArray = root.getJSONArray("weightRecords")
                for (i in 0 until weightArray.length()) {
                    val obj = weightArray.getJSONObject(i)
                    dao.insertWeightRecord(
                        WeightRecord(
                            date = obj.getString("date"),
                            weightKg = obj.getDouble("weightKg").toFloat(),
                            note = obj.optString("note", "")
                        )
                    )
                }
            }
            preferencesManager.updateLastBackupTime(System.currentTimeMillis())
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun clearAllData() {
        dao.clearAllActivity()
        dao.clearAllHydration()
        dao.clearAllWeight()
        dao.clearAllAchievements()
        dao.clearAllFavorites()
        preferencesManager.clearAllPreferences()
    }
}
