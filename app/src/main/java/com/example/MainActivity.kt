package com.example

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsWalk
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.service.StepTrackerService
import com.example.ui.components.ConfettiCelebrationDialog
import com.example.ui.components.FloatingGlassBottomNav
import com.example.ui.components.LiquidGlassCard
import com.example.ui.screens.achievements.AchievementsScreen
import com.example.ui.screens.activity.ActivityScreen
import com.example.ui.screens.bmi.BmiCalculatorScreen
import com.example.ui.screens.diet.DietPlanScreen
import com.example.ui.screens.exercise.ExerciseGuideScreen
import com.example.ui.screens.health.HealthCenterScreen
import com.example.ui.screens.history.HistoryScreen
import com.example.ui.screens.home.HomeScreen
import com.example.ui.screens.motivation.DailyMotivationScreen
import com.example.ui.screens.notifications.NotificationSettingsScreen
import com.example.ui.screens.onboarding.OnboardingScreen
import com.example.ui.screens.profile.ProfileScreen
import com.example.ui.screens.report.WellnessReportScreen
import com.example.ui.screens.settings.SettingsScreen
import com.example.ui.screens.splash.SplashScreen
import com.example.ui.screens.water.WaterTrackerScreen
import com.example.ui.screens.weight.WeightTrackerScreen
import com.example.ui.theme.AmberAccent
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.MintAccent
import com.example.ui.theme.VitalStepTheme
import com.example.ui.viewmodel.Screen
import com.example.ui.viewmodel.VitalStepViewModel
import com.example.ui.viewmodel.VitalStepViewModelFactory
import com.example.ui.viewmodel.WalkSessionSummary
import java.text.NumberFormat
import java.util.Locale

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val factory = VitalStepViewModelFactory(application)
            val viewModel: VitalStepViewModel = viewModel(factory = factory)

            val userProfile by viewModel.userProfile.collectAsStateWithLifecycle()
            val currentScreen by viewModel.screen.collectAsStateWithLifecycle()
            val todayActivity by viewModel.todayActivity.collectAsStateWithLifecycle()
            val historyList by viewModel.activityHistory.collectAsStateWithLifecycle()
            val achievements by viewModel.achievements.collectAsStateWithLifecycle()
            val celebrationItem by viewModel.celebrationAchievement.collectAsStateWithLifecycle()
            val currentWater by viewModel.todayHydration.collectAsStateWithLifecycle()
            val weightRecords by viewModel.weightRecords.collectAsStateWithLifecycle()
            val favoriteQuoteIds by viewModel.favoriteQuoteIds.collectAsStateWithLifecycle()
            val notificationSettings by viewModel.notificationSettings.collectAsStateWithLifecycle()

            // Live Walk Session states
            val isWalkSessionActive by viewModel.isWalkSessionActive.collectAsStateWithLifecycle()
            val isWalkSessionPaused by viewModel.isWalkSessionPaused.collectAsStateWithLifecycle()
            val sessionSeconds by viewModel.sessionSeconds.collectAsStateWithLifecycle()
            val sessionSteps by viewModel.sessionSteps.collectAsStateWithLifecycle()
            val sessionDistanceKm by viewModel.sessionDistanceKm.collectAsStateWithLifecycle()
            val sessionCalories by viewModel.sessionCalories.collectAsStateWithLifecycle()
            val isAutoWalking by viewModel.isAutoWalking.collectAsStateWithLifecycle()
            val walkSummary by viewModel.walkSummary.collectAsStateWithLifecycle()

            val isBengali = userProfile.preferredLanguage == "bn"

            // Runtime Permissions Request
            val permissionLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.RequestMultiplePermissions()
            ) { _ ->
                StepTrackerService.startService(this)
            }

            LaunchedEffect(Unit) {
                val permissionsToRequest = mutableListOf<String>()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    if (ContextCompat.checkSelfPermission(this@MainActivity, Manifest.permission.ACTIVITY_RECOGNITION)
                        != PackageManager.PERMISSION_GRANTED) {
                        permissionsToRequest.add(Manifest.permission.ACTIVITY_RECOGNITION)
                    }
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    if (ContextCompat.checkSelfPermission(this@MainActivity, Manifest.permission.POST_NOTIFICATIONS)
                        != PackageManager.PERMISSION_GRANTED) {
                        permissionsToRequest.add(Manifest.permission.POST_NOTIFICATIONS)
                    }
                }
                if (permissionsToRequest.isNotEmpty()) {
                    permissionLauncher.launch(permissionsToRequest.toTypedArray())
                } else {
                    StepTrackerService.startService(this@MainActivity)
                }
            }

            VitalStepTheme(darkTheme = userProfile.isDarkMode) {
                // BackHandler for custom screen stack
                BackHandler(enabled = currentScreen != Screen.HOME && currentScreen != Screen.SPLASH) {
                    val handled = viewModel.navigateBack()
                    if (!handled) {
                        viewModel.navigateTo(Screen.HOME)
                    }
                }

                val showBottomNav = currentScreen in listOf(
                    Screen.HOME,
                    Screen.ACTIVITY,
                    Screen.HEALTH,
                    Screen.PROFILE
                )

                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        when (currentScreen) {
                            Screen.SPLASH -> {
                                SplashScreen(
                                    hasCompletedOnboarding = userProfile.hasCompletedOnboarding,
                                    onNavigateNext = { hasOnboarded ->
                                        if (hasOnboarded) {
                                            viewModel.navigateTo(Screen.HOME)
                                        } else {
                                            viewModel.navigateTo(Screen.ONBOARDING)
                                        }
                                    }
                                )
                            }
                            Screen.ONBOARDING -> {
                                OnboardingScreen(
                                    currentLanguage = userProfile.preferredLanguage,
                                    onLanguageSelected = { lang -> viewModel.setLanguage(lang) },
                                    onFinishOnboarding = {
                                        viewModel.updateProfile(userProfile.copy(hasCompletedOnboarding = true))
                                        viewModel.navigateTo(Screen.HOME)
                                    }
                                )
                            }
                            Screen.HOME -> {
                                HomeScreen(
                                    activityRecord = todayActivity,
                                    userProfile = userProfile,
                                    isWalkSessionActive = isWalkSessionActive,
                                    isWalkSessionPaused = isWalkSessionPaused,
                                    sessionSeconds = sessionSeconds,
                                    sessionSteps = sessionSteps,
                                    sessionDistanceKm = sessionDistanceKm,
                                    sessionCalories = sessionCalories,
                                    isAutoWalking = isAutoWalking,
                                    onStartWalk = { viewModel.startWalkSession() },
                                    onPauseWalk = { viewModel.pauseWalkSession() },
                                    onResumeWalk = { viewModel.resumeWalkSession() },
                                    onStopWalk = { viewModel.stopWalkSession() },
                                    onToggleAutoWalk = { viewModel.toggleAutoWalking() },
                                    onSimulateSteps = { count -> viewModel.simulateSteps(count) },
                                    onResetTodayActivity = { viewModel.resetTodayActivity() },
                                    onStepGoalChange = { goal -> viewModel.updateDailyGoal(goal) },
                                    onNavigate = { screen -> viewModel.navigateTo(screen) }
                                )
                            }
                            Screen.ACTIVITY -> {
                                ActivityScreen(
                                    todayRecord = todayActivity,
                                    historyList = historyList,
                                    userProfile = userProfile,
                                    isBengali = isBengali,
                                    onStepGoalChange = { goal -> viewModel.updateDailyGoal(goal) }
                                )
                            }
                            Screen.ACHIEVEMENTS -> {
                                AchievementsScreen(
                                    achievements = achievements,
                                    isBengali = isBengali,
                                    onSelectAchievement = { item -> viewModel.triggerCelebration(item) }
                                )
                            }
                            Screen.HEALTH -> {
                                HealthCenterScreen(
                                    tipsList = viewModel.healthTips,
                                    isBengali = isBengali,
                                    onNavigate = { screen -> viewModel.navigateTo(screen) }
                                )
                            }
                            Screen.DIET_PLAN -> {
                                DietPlanScreen(
                                    foodItems = viewModel.foodLibrary,
                                    isBengali = isBengali,
                                    onBack = { viewModel.navigateBack() }
                                )
                            }
                            Screen.BMI_CALCULATOR -> {
                                BmiCalculatorScreen(
                                    userProfile = userProfile,
                                    isBengali = isBengali,
                                    onBack = { viewModel.navigateBack() }
                                )
                            }
                            Screen.HISTORY -> {
                                HistoryScreen(
                                    historyList = historyList,
                                    isBengali = isBengali,
                                    onBack = { viewModel.navigateBack() }
                                )
                            }
                            Screen.WATER_TRACKER -> {
                                WaterTrackerScreen(
                                    currentWaterMl = currentWater,
                                    dailyTargetMl = userProfile.dailyWaterGoalMl,
                                    isBengali = isBengali,
                                    onAddWater = { amount -> viewModel.addWater(amount) },
                                    onBack = { viewModel.navigateBack() }
                                )
                            }
                            Screen.WEIGHT_TRACKER -> {
                                WeightTrackerScreen(
                                    weightRecords = weightRecords,
                                    userProfile = userProfile,
                                    isBengali = isBengali,
                                    onRecordWeight = { w, note -> viewModel.recordWeight(w, note) },
                                    onDeleteWeight = { id -> viewModel.deleteWeight(id) },
                                    onBack = { viewModel.navigateBack() }
                                )
                            }
                            Screen.EXERCISE_GUIDE -> {
                                ExerciseGuideScreen(
                                    exerciseList = viewModel.exerciseList,
                                    isBengali = isBengali,
                                    onBack = { viewModel.navigateBack() }
                                )
                            }
                            Screen.DAILY_MOTIVATION -> {
                                DailyMotivationScreen(
                                    quotesList = viewModel.motivationQuotes,
                                    favoriteQuoteIds = favoriteQuoteIds,
                                    isBengali = isBengali,
                                    onToggleFavorite = { id, fav -> viewModel.toggleFavoriteQuote(id, fav) },
                                    onBack = { viewModel.navigateBack() }
                                )
                            }
                            Screen.WELLNESS_REPORT -> {
                                WellnessReportScreen(
                                    historyList = historyList,
                                    isBengali = isBengali,
                                    onExportCsv = { onDone -> viewModel.exportCsv(onDone) },
                                    onBack = { viewModel.navigateBack() }
                                )
                            }
                            Screen.NOTIFICATIONS -> {
                                NotificationSettingsScreen(
                                    settings = notificationSettings,
                                    isBengali = isBengali,
                                    onToggle = { type, enabled -> viewModel.updateNotificationToggle(type, enabled) },
                                    onBack = { viewModel.navigateBack() }
                                )
                            }
                            Screen.PROFILE -> {
                                ProfileScreen(
                                    userProfile = userProfile,
                                    isBengali = isBengali,
                                    onUpdateProfile = { updated -> viewModel.updateProfile(updated) },
                                    onLanguageToggle = { lang -> viewModel.setLanguage(lang) },
                                    onStepGoalChange = { goal -> viewModel.updateDailyGoal(goal) },
                                    onToggleGoogleAccount = { viewModel.toggleGoogleSignIn() },
                                    onNavigate = { screen -> viewModel.navigateTo(screen) }
                                )
                            }
                            Screen.SETTINGS -> {
                                SettingsScreen(
                                    userProfile = userProfile,
                                    isBengali = isBengali,
                                    onToggleDarkMode = { dark -> viewModel.setDarkMode(dark) },
                                    onToggleLanguage = { lang -> viewModel.setLanguage(lang) },
                                    onBackupJson = { onDone -> viewModel.backupJson(onDone) },
                                    onRestoreJson = { json, onDone -> viewModel.restoreJson(json, onDone) },
                                    onExportCsv = { onDone -> viewModel.exportCsv(onDone) },
                                    onClearAllData = { viewModel.clearAllUserData() },
                                    onNavigate = { screen -> viewModel.navigateTo(screen) },
                                    onBack = { viewModel.navigateBack() }
                                )
                            }
                        }

                        // Floating Glass Bottom Navigation
                        if (showBottomNav) {
                            FloatingGlassBottomNav(
                                currentScreen = currentScreen,
                                onNavigate = { screen -> viewModel.navigateTo(screen) },
                                isBengali = isBengali,
                                modifier = Modifier.align(Alignment.BottomCenter)
                            )
                        }

                        // Confetti & Celebration Dialog
                        ConfettiCelebrationDialog(
                            achievement = celebrationItem,
                            isBengali = isBengali,
                            onDismiss = { viewModel.dismissCelebration() }
                        )

                        // Walk Session Completed Summary Dialog
                        walkSummary?.let { summary ->
                            WalkSummaryDialog(
                                summary = summary,
                                isBengali = isBengali,
                                onDismiss = { viewModel.dismissWalkSummary() }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun WalkSummaryDialog(
    summary: WalkSessionSummary,
    isBengali: Boolean,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(MintAccent.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.DirectionsWalk,
                        contentDescription = null,
                        tint = MintAccent,
                        modifier = Modifier.size(22.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = if (isBengali) "হাঁটা সফলভাবে সম্পন্ন!" else "Walk Completed!",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
            }
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = if (isBengali) "আপনার এই সেশনের ফলাফল সংরক্ষিত হয়েছে:" else "Your walking session has been saved to today's activity:",
                    style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = NumberFormat.getNumberInstance(Locale.US).format(summary.steps),
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = CyanAccent
                            )
                        )
                        Text(
                            text = if (isBengali) "কদম" else "Steps",
                            style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                        )
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "${String.format(Locale.US, "%.2f", summary.distanceKm)} km",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = MintAccent
                            )
                        )
                        Text(
                            text = if (isBengali) "দূরত্ব" else "Distance",
                            style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                        )
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = summary.durationFormatted,
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = AmberAccent
                            )
                        )
                        Text(
                            text = if (isBengali) "সময়" else "Duration",
                            style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = CyanAccent),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = if (isBengali) "ধন্যবাদ ও এগিয়ে যান" else "Continue",
                    color = Color(0xFF00363A),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    )
}
