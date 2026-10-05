package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.VitalStepApplication
import com.example.data.local.NotificationSettings
import com.example.data.model.AchievementItem
import com.example.data.model.DailyActivityRecord
import com.example.data.model.ExerciseItem
import com.example.data.model.FoodItem
import com.example.data.model.HealthTip
import com.example.data.model.HydrationRecord
import com.example.data.model.MotivationQuote
import com.example.data.model.UserProfile
import com.example.data.model.WeightRecord
import com.example.data.repository.HealthDataProvider
import com.example.data.repository.VitalStepRepository
import com.example.sensor.SensorStatus
import com.example.sensor.StepSensorManager
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class Screen {
    SPLASH,
    ONBOARDING,
    HOME,
    ACTIVITY,
    ACHIEVEMENTS,
    HEALTH,
    DIET_PLAN,
    BMI_CALCULATOR,
    HISTORY,
    WATER_TRACKER,
    WEIGHT_TRACKER,
    EXERCISE_GUIDE,
    DAILY_MOTIVATION,
    WELLNESS_REPORT,
    NOTIFICATIONS,
    PROFILE,
    SETTINGS
}

data class WalkSessionSummary(
    val steps: Int,
    val distanceKm: Float,
    val calories: Int,
    val durationSeconds: Int
) {
    val durationFormatted: String
        get() {
            val mins = durationSeconds / 60
            val secs = durationSeconds % 60
            return String.format("%02d:%02d", mins, secs)
        }
}

class VitalStepViewModel(application: Application) : AndroidViewModel(application) {

    private val app = application as VitalStepApplication
    val repository: VitalStepRepository = app.repository
    val sensorManager: StepSensorManager = app.stepSensorManager

    // Navigation state stack
    private val _screenStack = MutableStateFlow(listOf(Screen.SPLASH))
    private val _currentScreen = MutableStateFlow(Screen.SPLASH)
    val screen: StateFlow<Screen> = _currentScreen.asStateFlow()

    // Celebration state for unlocked achievements
    private val _celebrationAchievement = MutableStateFlow<AchievementItem?>(null)
    val celebrationAchievement: StateFlow<AchievementItem?> = _celebrationAchievement.asStateFlow()

    // Sub-screen selections
    val selectedHistoryDate = MutableStateFlow(repository.getTodayDateString())
    val selectedExerciseId = MutableStateFlow(1)
    val selectedMealCategory = MutableStateFlow("Breakfast")
    val selectedHealthCategory = MutableStateFlow("Today")

    // Sensor status
    val sensorStatus: StateFlow<SensorStatus> = sensorManager.sensorStatus

    // Live Walking Session State
    private val _isWalkSessionActive = MutableStateFlow(false)
    val isWalkSessionActive: StateFlow<Boolean> = _isWalkSessionActive.asStateFlow()

    private val _isWalkSessionPaused = MutableStateFlow(false)
    val isWalkSessionPaused: StateFlow<Boolean> = _isWalkSessionPaused.asStateFlow()

    private val _sessionSeconds = MutableStateFlow(0)
    val sessionSeconds: StateFlow<Int> = _sessionSeconds.asStateFlow()

    private val _sessionSteps = MutableStateFlow(0)
    val sessionSteps: StateFlow<Int> = _sessionSteps.asStateFlow()

    private val _sessionDistanceKm = MutableStateFlow(0f)
    val sessionDistanceKm: StateFlow<Float> = _sessionDistanceKm.asStateFlow()

    private val _sessionCalories = MutableStateFlow(0)
    val sessionCalories: StateFlow<Int> = _sessionCalories.asStateFlow()

    private val _isAutoWalking = MutableStateFlow(false)
    val isAutoWalking: StateFlow<Boolean> = _isAutoWalking.asStateFlow()

    private val _walkSummary = MutableStateFlow<WalkSessionSummary?>(null)
    val walkSummary: StateFlow<WalkSessionSummary?> = _walkSummary.asStateFlow()

    private var sessionTimerJob: Job? = null
    private var autoWalkJob: Job? = null

    // Flows from repository
    val userProfile: StateFlow<UserProfile> = repository.userProfile
        .stateIn(viewModelScope, SharingStarted.Eagerly, UserProfile())

    val notificationSettings: StateFlow<NotificationSettings> = repository.notificationSettings
        .stateIn(viewModelScope, SharingStarted.Eagerly, NotificationSettings())

    val todayActivity: StateFlow<DailyActivityRecord?> = repository.getTodayActivity()
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    val activityHistory: StateFlow<List<DailyActivityRecord>> = repository.getAllActivityHistory()
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    val last7DaysActivity: StateFlow<List<DailyActivityRecord>> = repository.getLast7DaysActivity()
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    val achievements: StateFlow<List<AchievementItem>> = repository.getAchievements()
        .stateIn(viewModelScope, SharingStarted.Eagerly, HealthDataProvider.achievementsList)

    val todayHydration: StateFlow<Int> = repository.getTodayHydration()
        .stateIn(viewModelScope, SharingStarted.Eagerly, 0)

    val todayHydrationList: StateFlow<List<HydrationRecord>> = repository.getTodayHydrationList()
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    val weightRecords: StateFlow<List<WeightRecord>> = repository.getAllWeightRecords()
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    val favoriteQuoteIds: StateFlow<List<Int>> = repository.getFavoriteQuoteIds()
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    // Static content lists
    val healthTips: List<HealthTip> = HealthDataProvider.healthTips
    val foodLibrary: List<FoodItem> = HealthDataProvider.foodLibrary
    val exerciseList: List<ExerciseItem> = HealthDataProvider.exerciseList
    val motivationQuotes: List<MotivationQuote> = HealthDataProvider.motivationQuotes

    init {
        // Sensor listening
        sensorManager.startListening { delta ->
            if (delta > 0) {
                handleIncomingSteps(delta)
            }
        }

        viewModelScope.launch {
            sensorManager.stepEvents.collect { delta ->
                // Steps are handled via onStepDetected
            }
        }
    }

    private fun handleIncomingSteps(stepsCount: Int) {
        if (_isWalkSessionActive.value && !_isWalkSessionPaused.value) {
            _sessionSteps.value += stepsCount
            val profile = userProfile.value
            val strideMeters = (profile.heightCm * 0.415f) / 100f
            _sessionDistanceKm.value = (_sessionSteps.value * strideMeters) / 1000f
            _sessionCalories.value = (_sessionSteps.value * 0.043f).toInt()
        }

        viewModelScope.launch {
            repository.recordSteps(stepsCount, isIncremental = true)
        }
    }

    fun startWalkSession() {
        _isWalkSessionActive.value = true
        _isWalkSessionPaused.value = false
        _sessionSeconds.value = 0
        _sessionSteps.value = 0
        _sessionDistanceKm.value = 0f
        _sessionCalories.value = 0

        startTimer()
    }

    fun pauseWalkSession() {
        _isWalkSessionPaused.value = true
        sessionTimerJob?.cancel()
        if (_isAutoWalking.value) {
            toggleAutoWalking()
        }
    }

    fun resumeWalkSession() {
        _isWalkSessionPaused.value = false
        startTimer()
    }

    fun stopWalkSession() {
        sessionTimerJob?.cancel()
        autoWalkJob?.cancel()
        _isAutoWalking.value = false

        val summary = WalkSessionSummary(
            steps = _sessionSteps.value,
            distanceKm = _sessionDistanceKm.value,
            calories = _sessionCalories.value,
            durationSeconds = _sessionSeconds.value
        )
        _walkSummary.value = summary

        _isWalkSessionActive.value = false
        _isWalkSessionPaused.value = false
    }

    fun dismissWalkSummary() {
        _walkSummary.value = null
    }

    private fun startTimer() {
        sessionTimerJob?.cancel()
        sessionTimerJob = viewModelScope.launch {
            while (_isWalkSessionActive.value && !_isWalkSessionPaused.value) {
                delay(1000)
                _sessionSeconds.value += 1
            }
        }
    }

    fun toggleAutoWalking() {
        if (_isAutoWalking.value) {
            _isAutoWalking.value = false
            autoWalkJob?.cancel()
        } else {
            if (!_isWalkSessionActive.value) {
                startWalkSession()
            } else if (_isWalkSessionPaused.value) {
                resumeWalkSession()
            }
            _isAutoWalking.value = true
            autoWalkJob = viewModelScope.launch {
                while (_isAutoWalking.value) {
                    delay(700)
                    simulateSteps(1)
                }
            }
        }
    }

    fun simulateSteps(count: Int) {
        sensorManager.emitStep(count)
        handleIncomingSteps(count)
    }

    fun resetTodayActivity() {
        viewModelScope.launch {
            repository.resetTodayActivity()
            _sessionSteps.value = 0
            _sessionDistanceKm.value = 0f
            _sessionCalories.value = 0
            _sessionSeconds.value = 0
            _isWalkSessionActive.value = false
            _isWalkSessionPaused.value = false
            _isAutoWalking.value = false
            sessionTimerJob?.cancel()
            autoWalkJob?.cancel()
        }
    }

    fun navigateTo(target: Screen) {
        val current = _screenStack.value
        if (current.lastOrNull() != target) {
            _screenStack.value = current + target
            _currentScreen.value = target
        }
    }

    fun navigateBack(): Boolean {
        val current = _screenStack.value
        return if (current.size > 1) {
            val updated = current.dropLast(1)
            _screenStack.value = updated
            _currentScreen.value = updated.last()
            true
        } else {
            false
        }
    }

    fun dismissCelebration() {
        _celebrationAchievement.value = null
    }

    fun triggerCelebration(item: AchievementItem) {
        _celebrationAchievement.value = item
    }

    fun updateDailyGoal(newGoal: Int) {
        viewModelScope.launch {
            repository.setDailyStepGoal(newGoal)
        }
    }

    // Hydration Operations
    fun addWater(amountMl: Int) {
        viewModelScope.launch {
            repository.addWater(amountMl)
        }
    }

    fun clearTodayWater() {
        viewModelScope.launch {
            repository.clearTodayWater()
        }
    }

    // Weight Operations
    fun recordWeight(weightKg: Float, note: String = "") {
        viewModelScope.launch {
            repository.recordWeight(weightKg, note)
        }
    }

    fun deleteWeight(id: Long) {
        viewModelScope.launch {
            repository.deleteWeightRecord(id)
        }
    }

    // Profile & Settings Operations
    fun updateProfile(profile: UserProfile) {
        viewModelScope.launch {
            repository.updateProfile(profile)
        }
    }

    fun setLanguage(lang: String) {
        viewModelScope.launch {
            repository.setLanguage(lang)
        }
    }

    fun setDarkMode(enabled: Boolean) {
        viewModelScope.launch {
            repository.setDarkMode(enabled)
        }
    }

    fun toggleGoogleSignIn() {
        viewModelScope.launch {
            repository.toggleGoogleSignIn()
        }
    }

    fun toggleFavoriteQuote(quoteId: Int, isCurrentlyFavorite: Boolean) {
        viewModelScope.launch {
            repository.toggleFavoriteQuote(quoteId, isCurrentlyFavorite)
        }
    }

    fun updateNotificationToggle(type: String, enabled: Boolean) {
        viewModelScope.launch {
            app.preferencesManager.updateNotificationSetting(type, enabled)
        }
    }

    fun exportCsv(onDone: (String) -> Unit) {
        viewModelScope.launch {
            val csv = repository.exportDataAsCsv()
            onDone(csv)
        }
    }

    fun backupJson(onDone: (String) -> Unit) {
        viewModelScope.launch {
            val json = repository.exportDataAsJson()
            onDone(json)
        }
    }

    fun restoreJson(jsonString: String, onDone: (Boolean) -> Unit) {
        viewModelScope.launch {
            val success = repository.restoreDataFromJson(jsonString)
            onDone(success)
        }
    }

    fun clearAllUserData() {
        viewModelScope.launch {
            repository.clearAllData()
            _screenStack.value = listOf(Screen.ONBOARDING)
            _currentScreen.value = Screen.ONBOARDING
        }
    }
}

class VitalStepViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(VitalStepViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return VitalStepViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
