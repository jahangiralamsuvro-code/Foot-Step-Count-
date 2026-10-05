package com.example.ui.screens.home

import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColor
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.DirectionsWalk
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material.icons.filled.Scale
import androidx.compose.material.icons.filled.Sensors
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.DailyActivityRecord
import com.example.data.model.DailyStepGoalProgress
import com.example.data.model.UserProfile
import com.example.ui.components.ActivityRing
import com.example.ui.components.DailyMetricsRow
import com.example.ui.components.LiquidGlassCard
import com.example.ui.components.StepGoalTrackerCard
import com.example.ui.theme.AmberAccent
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.MintAccent
import com.example.ui.theme.PurpleAccent
import com.example.ui.theme.RoseAccent
import com.example.ui.viewmodel.Screen
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun HomeScreen(
    activityRecord: DailyActivityRecord?,
    userProfile: UserProfile,
    isWalkSessionActive: Boolean,
    isWalkSessionPaused: Boolean,
    sessionSeconds: Int,
    sessionSteps: Int,
    sessionDistanceKm: Float,
    sessionCalories: Int,
    isAutoWalking: Boolean,
    onStartWalk: () -> Unit,
    onPauseWalk: () -> Unit,
    onResumeWalk: () -> Unit,
    onStopWalk: () -> Unit,
    onToggleAutoWalk: () -> Unit,
    onSimulateSteps: (Int) -> Unit,
    onResetTodayActivity: () -> Unit,
    onStepGoalChange: (Int) -> Unit = {},
    onNavigate: (Screen) -> Unit
) {
    val context = LocalContext.current
    val isBengali = userProfile.preferredLanguage == "bn"

    val steps = activityRecord?.steps ?: 0
    val goal = activityRecord?.goal ?: userProfile.dailyStepGoal
    val distance = activityRecord?.distanceKm ?: 0f
    val calories = activityRecord?.caloriesBurned ?: 0
    val activeMinutes = activityRecord?.activeMinutes ?: 0

    val currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
    val (greetingEn, greetingBn) = when (currentHour) {
        in 5..11 -> "Good Morning!" to "শুভ সকাল!"
        in 12..16 -> "Good Afternoon!" to "শুভ অপরাহ্ন!"
        in 17..20 -> "Good Evening!" to "শুভ সন্ধ্যা!"
        else -> "Good Night!" to "শুভ রাত্রি!"
    }

    val dateFormatted = SimpleDateFormat("EEE, d MMM yyyy", Locale.US).format(Date())
    var isQuoteFav by remember { mutableStateOf(false) }

    // Pulsing indicator for active walk
    val infiniteTransition = rememberInfiniteTransition(label = "pulse_trans")
    val pulseColor by infiniteTransition.animateColor(
        initialValue = MintAccent,
        targetValue = CyanAccent,
        animationSpec = infiniteRepeatable(
            animation = tween(1000),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_color"
    )

    val sessionMins = sessionSeconds / 60
    val sessionSecs = sessionSeconds % 60
    val durationFormatted = String.format(Locale.US, "%02d:%02d", sessionMins, sessionSecs)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 20.dp)
            .testTag("home_screen")
    ) {
        // Top Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(Color(0x33FFB300)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.WbSunny,
                        contentDescription = "Greeting",
                        tint = AmberAccent,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = if (isBengali) greetingBn else greetingEn,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    )
                    Text(
                        text = if (isBengali) "$greetingEn • $dateFormatted" else "$greetingBn • $dateFormatted",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = onResetTodayActivity,
                    modifier = Modifier.testTag("home_reset_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Reset Today",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(20.dp)
                    )
                }

                IconButton(
                    onClick = { onNavigate(Screen.NOTIFICATIONS) },
                    modifier = Modifier.testTag("home_notif_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = "Notifications",
                        tint = CyanAccent
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // LIVE WALK CONTROLLER CARD
        LiquidGlassCard(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("live_walk_session_card"),
            shape = RoundedCornerShape(26.dp),
            borderColor = if (isWalkSessionActive) pulseColor else CyanAccent.copy(alpha = 0.4f),
            borderWidth = if (isWalkSessionActive) 2.dp else 1.dp
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                if (!isWalkSessionActive) {
                    // Start Walk Prompt
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .clip(CircleShape)
                                    .background(MintAccent)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (isBengali) "সেন্সর সক্রিয় • হাঁটা শুরু করতে প্রস্তুত" else "Sensor Ready • Ready to Walk",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = MintAccent,
                                    fontWeight = FontWeight.SemiBold
                                )
                            )
                        }

                        Text(
                            text = if (isBengali) "আজকের কদম: $steps" else "Today: $steps steps",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Big Start Walk Button
                    Button(
                        onClick = onStartWalk,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp)
                            .testTag("start_walk_button"),
                        shape = RoundedCornerShape(27.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = CyanAccent)
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "Start",
                            tint = Color(0xFF00363A),
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isBengali) "হাঁটা শুরু করুন (Start Walk)" else "START WALKING NOW",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = Color(0xFF00363A),
                                letterSpacing = 0.5.sp
                            )
                        )
                    }
                } else {
                    // ACTIVE LIVE WALK DASHBOARD
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(12.dp)
                                    .clip(CircleShape)
                                    .background(if (isWalkSessionPaused) AmberAccent else pulseColor)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (isWalkSessionPaused) {
                                    if (isBengali) "হাঁটা সাময়িক বিরতি (PAUSED)" else "WALK PAUSED"
                                } else {
                                    if (isBengali) "হাঁটা রেকর্ড হচ্ছে (LIVE)..." else "LIVE TRACKING IN PROGRESS..."
                                },
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = if (isWalkSessionPaused) AmberAccent else MintAccent,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }

                        // Live Duration Clock
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Timer,
                                contentDescription = null,
                                tint = CyanAccent,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = durationFormatted,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Live Metrics in Session
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = NumberFormat.getNumberInstance(Locale.US).format(sessionSteps),
                                style = MaterialTheme.typography.headlineMedium.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    color = CyanAccent,
                                    fontSize = 28.sp
                                )
                            )
                            Text(
                                text = if (isBengali) "সেশনের কদম" else "Live Steps",
                                style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                            )
                        }

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "${String.format(Locale.US, "%.2f", sessionDistanceKm)} km",
                                style = MaterialTheme.typography.headlineMedium.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    color = MintAccent,
                                    fontSize = 28.sp
                                )
                            )
                            Text(
                                text = if (isBengali) "দূরত্ব" else "Distance",
                                style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                            )
                        }

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "$sessionCalories",
                                style = MaterialTheme.typography.headlineMedium.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    color = AmberAccent,
                                    fontSize = 28.sp
                                )
                            )
                            Text(
                                text = if (isBengali) "ক্যালোরি" else "Calories",
                                style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // Session Controls: Pause/Resume and Stop
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Button(
                            onClick = if (isWalkSessionPaused) onResumeWalk else onPauseWalk,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isWalkSessionPaused) MintAccent else Color(0x33FFB300),
                                contentColor = if (isWalkSessionPaused) Color(0xFF00363A) else AmberAccent
                            ),
                            shape = RoundedCornerShape(20.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(46.dp)
                                .testTag("pause_resume_walk_button")
                        ) {
                            Icon(
                                imageVector = if (isWalkSessionPaused) Icons.Default.PlayArrow else Icons.Default.Pause,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (isWalkSessionPaused) {
                                    if (isBengali) "চালিয়ে যান" else "Resume"
                                } else {
                                    if (isBengali) "বিরতি" else "Pause"
                                },
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                            )
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        Button(
                            onClick = onStopWalk,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFFF5252),
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(20.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(46.dp)
                                .testTag("stop_walk_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Stop,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (isBengali) "হাঁটা শেষ করুন" else "Stop & Save",
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Live Step Tester & Auto-Walk Simulator row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            onClick = onToggleAutoWalk,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isAutoWalking) CyanAccent else Color(0x2200E5FF),
                                contentColor = if (isAutoWalking) Color(0xFF00363A) else CyanAccent
                            ),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(38.dp)
                                .testTag("auto_walk_toggle_button")
                        ) {
                            Text(
                                text = if (isAutoWalking) {
                                    if (isBengali) "অটো-ওয়াক বন্ধ" else "Stop Auto-Walk"
                                } else {
                                    if (isBengali) "অটো-ওয়াক টেস্ট" else "Auto-Walk Test"
                                },
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Button(
                            onClick = { onSimulateSteps(1) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0x2200E5FF),
                                contentColor = CyanAccent
                            ),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier
                                .weight(0.7f)
                                .height(38.dp)
                                .testTag("step_one_button")
                        ) {
                            Text(
                                text = "+১ কদম",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Button(
                            onClick = { onSimulateSteps(10) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0x2200E5FF),
                                contentColor = CyanAccent
                            ),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier
                                .weight(0.8f)
                                .height(38.dp)
                                .testTag("step_ten_button")
                        ) {
                            Text(
                                text = "+১০ কদম",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Large Activity Ring
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            ActivityRing(
                steps = steps,
                goal = goal,
                size = 260.dp,
                strokeWidth = 18.dp
            )
        }

        Spacer(modifier = Modifier.height(22.dp))

        // Metrics Row (Distance, Calories, Active Time)
        DailyMetricsRow(
            distanceKm = distance,
            caloriesKcal = calories,
            activeMinutes = activeMinutes,
            isBengali = isBengali
        )

        Spacer(modifier = Modifier.height(18.dp))

        // Step Goal Tracker Card with Linear Progress Bar Indicator & Goal Setting
        val stepGoalProgress = remember(steps, goal, userProfile.heightCm, userProfile.currentWeightKg) {
            DailyStepGoalProgress.fromActivityAndProfile(activityRecord, userProfile)
        }
        StepGoalTrackerCard(
            goalProgress = stepGoalProgress,
            onGoalChange = onStepGoalChange,
            isBengali = isBengali
        )

        Spacer(modifier = Modifier.height(18.dp))

        // Current Achievement Card (matching mockup)
        LiquidGlassCard(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("current_achievement_card"),
            shape = RoundedCornerShape(22.dp),
            onClick = { onNavigate(Screen.ACHIEVEMENTS) }
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(Color(0x33FFB300)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.EmojiEvents,
                        contentDescription = "Badge",
                        tint = AmberAccent,
                        modifier = Modifier.size(26.dp)
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = if (isBengali) "বর্তমান অর্জন ও লক্ষ্য" else "Current Achievement Goal",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = AmberAccent,
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                    Text(
                        text = if (isBengali) "কদম চ্যাম্পিয়ন (Step Champion)" else "Step Champion (10,000 steps)",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    )
                    Text(
                        text = if (isBengali) "দৈনিক লক্ষ্য পূরণে আর ${goal - steps} কদম বাকি" else "${(goal - steps).coerceAtLeast(0)} steps left to reach daily goal",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Daily Motivation Card
        LiquidGlassCard(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("daily_motivation_card"),
            shape = RoundedCornerShape(22.dp),
            onClick = { onNavigate(Screen.DAILY_MOTIVATION) }
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isBengali) "দৈনিক অনুপ্রেরণা" else "Daily Motivation",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = MintAccent,
                            fontWeight = FontWeight.Bold
                        )
                    )

                    Row {
                        IconButton(
                            onClick = { isQuoteFav = !isQuoteFav },
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(
                                imageVector = if (isQuoteFav) Icons.Default.Favorite else Icons.Outlined.FavoriteBorder,
                                contentDescription = "Favorite",
                                tint = if (isQuoteFav) RoseAccent else MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        IconButton(
                            onClick = {
                                val quoteText = "Small steps today, a healthier tomorrow. - VitalStep"
                                val sendIntent = Intent().apply {
                                    action = Intent.ACTION_SEND
                                    putExtra(Intent.EXTRA_TEXT, quoteText)
                                    type = "text/plain"
                                }
                                context.startActivity(Intent.createChooser(sendIntent, "Share Motivation"))
                            },
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Share,
                                contentDescription = "Share",
                                tint = CyanAccent,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "“Small steps today, a healthier tomorrow.”",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                )

                Text(
                    text = "“আজ একটু হাঁটেন, আগামীকাল আরো সুস্থ থাকুন!”",
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Quick Feature Shortcuts Section
        Text(
            text = if (isBengali) "সুস্বাস্থ্য সেবা সমূহ" else "Quick Features",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            QuickActionTile(
                icon = Icons.Default.WaterDrop,
                title = if (isBengali) "পানি ট্র্যাকার" else "Water Tracker",
                subtitle = "Hydration Log",
                color = CyanAccent,
                onClick = { onNavigate(Screen.WATER_TRACKER) },
                modifier = Modifier.weight(1f),
                tag = "quick_water"
            )
            Spacer(modifier = Modifier.width(10.dp))
            QuickActionTile(
                icon = Icons.Default.Scale,
                title = if (isBengali) "ওজন ও বিএমআই" else "Weight & BMI",
                subtitle = "Health Calculator",
                color = MintAccent,
                onClick = { onNavigate(Screen.BMI_CALCULATOR) },
                modifier = Modifier.weight(1f),
                tag = "quick_bmi"
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            QuickActionTile(
                icon = Icons.Default.FitnessCenter,
                title = if (isBengali) "ব্যায়াম গাইড" else "Exercise Guide",
                subtitle = "Brisk Walking",
                color = RoseAccent,
                onClick = { onNavigate(Screen.EXERCISE_GUIDE) },
                modifier = Modifier.weight(1f),
                tag = "quick_exercise"
            )
            Spacer(modifier = Modifier.width(10.dp))
            QuickActionTile(
                icon = Icons.Default.RestaurantMenu,
                title = if (isBengali) "খাদ্য তালিকা" else "Diet Plan",
                subtitle = "Oats & Fruits",
                color = PurpleAccent,
                onClick = { onNavigate(Screen.DIET_PLAN) },
                modifier = Modifier.weight(1f),
                tag = "quick_diet"
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            QuickActionTile(
                icon = Icons.Default.CalendarMonth,
                title = if (isBengali) "ইতিহাস" else "History",
                subtitle = "Calendar View",
                color = AmberAccent,
                onClick = { onNavigate(Screen.HISTORY) },
                modifier = Modifier.weight(1f),
                tag = "quick_history"
            )
            Spacer(modifier = Modifier.width(10.dp))
            QuickActionTile(
                icon = Icons.Default.Assessment,
                title = if (isBengali) "রিপোর্ট কার্ড" else "Wellness Report",
                subtitle = "Weekly & CSV",
                color = CyanAccent,
                onClick = { onNavigate(Screen.WELLNESS_REPORT) },
                modifier = Modifier.weight(1f),
                tag = "quick_report"
            )
        }

        Spacer(modifier = Modifier.height(80.dp)) // padding for floating bottom nav
    }
}

@Composable
private fun QuickActionTile(
    icon: ImageVector,
    title: String,
    subtitle: String,
    color: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    tag: String
) {
    LiquidGlassCard(
        modifier = modifier
            .height(96.dp)
            .testTag(tag),
        shape = RoundedCornerShape(20.dp),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(color.copy(alpha = 0.18f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = color,
                        modifier = Modifier.size(18.dp)
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 13.sp
                    ),
                    maxLines = 1
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = subtitle,
                style = MaterialTheme.typography.labelSmall.copy(
                    color = color,
                    fontWeight = FontWeight.Medium
                ),
                modifier = Modifier.padding(start = 2.dp)
            )
        }
    }
}
