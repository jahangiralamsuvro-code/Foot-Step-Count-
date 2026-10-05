package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DirectionsWalk
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.DailyStepGoalProgress
import com.example.data.model.StepGoalPreset
import com.example.ui.theme.AmberAccent
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.MintAccent
import com.example.ui.theme.PurpleAccent
import com.example.ui.theme.RoseAccent
import java.text.NumberFormat
import java.util.Locale

/**
 * Reusable animated linear progress bar indicator with milestone ticks and gradient fill.
 */
@Composable
fun StepGoalProgressBar(
    progressFraction: Float,
    modifier: Modifier = Modifier,
    barHeight: Dp = 16.dp,
    showMilestones: Boolean = true,
    testTag: String = "step_goal_progress_bar"
) {
    val clampedProgress = progressFraction.coerceIn(0f, 1f)
    val animatedProgress by animateFloatAsState(
        targetValue = clampedProgress,
        animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing),
        label = "step_progress_animation"
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .testTag(testTag)
    ) {
        // The Progress Bar Track
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(barHeight)
        ) {
            Canvas(modifier = Modifier.matchParentSize()) {
                val cornerRadius = CornerRadius(size.height / 2, size.height / 2)

                // 1. Background Track
                drawRoundRect(
                    color = Color(0x1F00E5FF),
                    size = size,
                    cornerRadius = cornerRadius
                )

                // 2. Active Gradient Fill
                val fillWidth = size.width * animatedProgress
                if (fillWidth > 0f) {
                    drawRoundRect(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                CyanAccent,
                                MintAccent,
                                if (animatedProgress >= 1f) AmberAccent else MintAccent
                            ),
                            startX = 0f,
                            endX = size.width
                        ),
                        size = Size(fillWidth, size.height),
                        cornerRadius = cornerRadius
                    )
                }

                // 3. Milestone tick lines inside the bar
                val milestoneFractions = listOf(0.25f, 0.50f, 0.75f)
                milestoneFractions.forEach { frac ->
                    val tickX = size.width * frac
                    val isPassed = animatedProgress >= frac
                    drawCircle(
                        color = if (isPassed) Color.White.copy(alpha = 0.8f) else Color(0x4D00E5FF),
                        radius = (size.height / 5f).coerceAtLeast(2.5f),
                        center = Offset(tickX, size.height / 2)
                    )
                }
            }
        }

        // Milestone Labels Below Track
        if (showMilestones) {
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                val milestones = listOf(
                    "0%" to 0.0f,
                    "25%" to 0.25f,
                    "50%" to 0.50f,
                    "75%" to 0.75f,
                    "100%" to 1.0f
                )

                milestones.forEach { (label, frac) ->
                    val isReached = animatedProgress >= frac
                    Text(
                        text = label,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 11.sp,
                            fontWeight = if (isReached) FontWeight.Bold else FontWeight.Normal,
                            color = if (isReached) MintAccent else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                        )
                    )
                }
            }
        }
    }
}

/**
 * Comprehensive Step Goal Tracker UI Component allowing users to view real-time daily
 * step goal progress and adjust/set custom goals directly.
 */
@Composable
fun StepGoalTrackerCard(
    goalProgress: DailyStepGoalProgress,
    onGoalChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    isBengali: Boolean = false,
    testTag: String = "step_goal_tracker_card"
) {
    var showGoalDialog by remember { mutableStateOf(false) }

    LiquidGlassCard(
        modifier = modifier
            .fillMaxWidth()
            .testTag(testTag),
        shape = RoundedCornerShape(26.dp),
        borderColor = if (goalProgress.isGoalAchieved) MintAccent.copy(alpha = 0.6f) else CyanAccent.copy(alpha = 0.35f),
        borderWidth = if (goalProgress.isGoalAchieved) 1.5.dp else 1.dp
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Header Row: Title & Adjust Goal Action
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(
                                if (goalProgress.isGoalAchieved) Color(0x3300E676) else Color(0x2600E5FF)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (goalProgress.isGoalAchieved) Icons.Default.EmojiEvents else Icons.Default.Flag,
                            contentDescription = if (isBengali) "লক্ষ্য আইকন" else "Goal Icon",
                            tint = if (goalProgress.isGoalAchieved) MintAccent else CyanAccent,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column {
                        Text(
                            text = if (isBengali) "দৈনিক কদম লক্ষ্য" else "Daily Step Goal",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        )
                        Text(
                            text = if (isBengali) "অগ্রগতি ও লক্ষ্য নির্ধারণ" else "Track & Set Daily Target",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                    }
                }

                // Adjust Goal Button
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0x1F00E5FF))
                        .clickable { showGoalDialog = true }
                        .padding(horizontal = 12.dp, vertical = 7.dp)
                        .testTag("edit_step_goal_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = if (isBengali) "লক্ষ্য পরিবর্তন" else "Adjust Goal",
                            tint = CyanAccent,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = if (isBengali) "লক্ষ্য বদলান" else "Set Goal",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = CyanAccent
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Main Metrics Header: Steps Counter + Percentage Pill
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Column {
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            text = goalProgress.formattedCurrentSteps,
                            style = MaterialTheme.typography.headlineLarge.copy(
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 34.sp,
                                color = if (goalProgress.isGoalAchieved) MintAccent else CyanAccent
                            ),
                            modifier = Modifier.testTag("current_steps_text")
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "/ ${goalProgress.formattedTargetGoal}",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            ),
                            modifier = Modifier.testTag("target_goal_text")
                        )
                    }

                    Text(
                        text = if (isBengali) "আজকের কদম সম্পন্ন" else "Steps completed today",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                }

                // Completion Badge
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(
                            if (goalProgress.isGoalAchieved) Color(0x3300E676) else Color(0x2200E5FF)
                        )
                        .border(
                            1.dp,
                            if (goalProgress.isGoalAchieved) MintAccent else CyanAccent.copy(alpha = 0.5f),
                            RoundedCornerShape(20.dp)
                        )
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                        .testTag("step_goal_percentage_badge"),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (goalProgress.isGoalAchieved) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = MintAccent,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                        }
                        Text(
                            text = if (goalProgress.isGoalAchieved) {
                                if (isBengali) "১০০% সম্পন্ন 🎉" else "100% Done 🎉"
                            } else {
                                "${goalProgress.progressPercentage}%"
                            },
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = if (goalProgress.isGoalAchieved) MintAccent else CyanAccent
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Animated Linear Progress Bar Indicator
            StepGoalProgressBar(
                progressFraction = goalProgress.progressFraction,
                barHeight = 14.dp,
                showMilestones = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Key Statistics Triplet: Remaining Steps, Target Distance, Calories
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Remaining / Excess Steps
                StatMiniBox(
                    icon = Icons.Default.DirectionsWalk,
                    title = if (goalProgress.isGoalAchieved) {
                        if (isBengali) "অতিরিক্ত কদম" else "Bonus Steps"
                    } else {
                        if (isBengali) "বাকি কদম" else "Remaining"
                    },
                    value = if (goalProgress.isGoalAchieved) {
                        "+${goalProgress.formattedExcessSteps}"
                    } else {
                        goalProgress.formattedRemainingSteps
                    },
                    accentColor = if (goalProgress.isGoalAchieved) MintAccent else CyanAccent,
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(8.dp))

                // Distance
                StatMiniBox(
                    icon = Icons.Default.NearMe,
                    title = if (isBengali) "দূরত্ব লক্ষ্য" else "Distance",
                    value = "${String.format(Locale.US, "%.1f", goalProgress.distanceCoveredKm)}/${String.format(Locale.US, "%.1f", goalProgress.targetDistanceKm)} km",
                    accentColor = PurpleAccent,
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(8.dp))

                // Calories
                StatMiniBox(
                    icon = Icons.Default.LocalFireDepartment,
                    title = if (isBengali) "ক্যালোরি" else "Calories",
                    value = "${goalProgress.estimatedCaloriesBurned}/${goalProgress.targetCaloriesBurned} kcal",
                    accentColor = AmberAccent,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Dynamic Motivation / Status Banner
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0x1400E5FF))
                    .border(1.dp, Color(0x2600E5FF), RoundedCornerShape(14.dp))
                    .padding(horizontal = 14.dp, vertical = 10.dp)
            ) {
                Text(
                    text = if (isBengali) goalProgress.statusMessageBn else goalProgress.statusMessageEn,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.Medium
                    ),
                    textAlign = TextAlign.Start
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Quick Preset Selection Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (isBengali) "দ্রুত লক্ষ্য:" else "Quick Goals:",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )

                Spacer(modifier = Modifier.width(8.dp))

                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    val presets = listOf(5000 to "5K", 8000 to "8K", 10000 to "10K", 12000 to "12K", 15000 to "15K")
                    presets.forEach { (goalValue, label) ->
                        val isSelected = goalProgress.targetGoal == goalValue
                        val animatedBg by animateColorAsState(
                            targetValue = if (isSelected) CyanAccent else Color(0x1F00E5FF),
                            label = "preset_bg"
                        )
                        val animatedTextColor by animateColorAsState(
                            targetValue = if (isSelected) Color(0xFF00363A) else CyanAccent,
                            label = "preset_text"
                        )

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(animatedBg)
                                .clickable { onGoalChange(goalValue) }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                                .testTag("goal_preset_$label"),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = label,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = animatedTextColor
                                )
                            )
                        }
                    }
                }
            }
        }
    }

    // Modal Goal Setting Dialog
    if (showGoalDialog) {
        StepGoalSettingDialog(
            currentGoal = goalProgress.targetGoal,
            strideLengthMeters = goalProgress.strideLengthMeters,
            isBengali = isBengali,
            onDismiss = { showGoalDialog = false },
            onSaveGoal = { newGoal ->
                onGoalChange(newGoal)
                showGoalDialog = false
            }
        )
    }
}

/**
 * Clean mini stat box for displaying remaining steps, distance, or calories.
 */
@Composable
private fun StatMiniBox(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    value: String,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0x14FFFFFF))
            .border(1.dp, Color(0x1FFFFFFF), RoundedCornerShape(16.dp))
            .padding(horizontal = 10.dp, vertical = 10.dp)
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = accentColor,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    maxLines = 1
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 13.sp
                ),
                maxLines = 1
            )
        }
    }
}

/**
 * Interactive Goal Setting Dialog with Slider, Steppers, Preset Tiers, and Real-Time Calculation Previews.
 */
@Composable
fun StepGoalSettingDialog(
    currentGoal: Int,
    strideLengthMeters: Float,
    isBengali: Boolean,
    onDismiss: () -> Unit,
    onSaveGoal: (Int) -> Unit
) {
    var selectedGoal by remember { mutableIntStateOf(currentGoal) }

    val estDistanceKm = (selectedGoal * strideLengthMeters) / 1000f
    val estCalories = (selectedGoal * 0.043f).toInt()

    AlertDialog(
        onDismissRequest = onDismiss,
        modifier = Modifier.testTag("step_goal_setting_dialog"),
        shape = RoundedCornerShape(28.dp),
        containerColor = MaterialTheme.colorScheme.surface,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color(0x2600E5FF)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Flag,
                        contentDescription = null,
                        tint = CyanAccent,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = if (isBengali) "দৈনিক কদম লক্ষ্য নির্ধারণ" else "Set Daily Step Goal",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                )
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = if (isBengali) "প্রতিদিন কত কদম হাঁটার লক্ষ্য রাখতে চান?" else "How many steps do you aim to walk each day?",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Big Stepper Value Display
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Decrement Button
                    IconButton(
                        onClick = { selectedGoal = (selectedGoal - 500).coerceAtLeast(2000) },
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(Color(0x1F00E5FF))
                            .testTag("decrement_step_goal_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Remove,
                            contentDescription = "Decrease 500 steps",
                            tint = CyanAccent
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = NumberFormat.getNumberInstance(Locale.US).format(selectedGoal),
                            style = MaterialTheme.typography.displaySmall.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = CyanAccent,
                                fontSize = 38.sp
                            ),
                            modifier = Modifier.testTag("dialog_selected_goal_text")
                        )
                        Text(
                            text = if (isBengali) "কদম / দিন" else "steps / day",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    // Increment Button
                    IconButton(
                        onClick = { selectedGoal = (selectedGoal + 500).coerceAtMost(30000) },
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(Color(0x1F00E5FF))
                            .testTag("increment_step_goal_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Increase 500 steps",
                            tint = CyanAccent
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Slider from 2,000 to 25,000
                Slider(
                    value = selectedGoal.toFloat(),
                    onValueChange = { selectedGoal = ((it / 500).toInt() * 500).coerceIn(2000, 25000) },
                    valueRange = 2000f..25000f,
                    steps = 45,
                    colors = SliderDefaults.colors(
                        thumbColor = CyanAccent,
                        activeTrackColor = MintAccent,
                        inactiveTrackColor = Color(0x3300E5FF)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("step_goal_slider")
                )

                // Slider Range Limits
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("2,000", style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant))
                    Text("10,000 (Ideal)", style = MaterialTheme.typography.labelSmall.copy(color = MintAccent, fontWeight = FontWeight.Bold))
                    Text("25,000", style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant))
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Real-time Preview: Target distance & calories
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0x1400E5FF))
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "${String.format(Locale.US, "%.1f", estDistanceKm)} km",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = CyanAccent)
                        )
                        Text(
                            text = if (isBengali) "আনুমানিক দূরত্ব" else "Target Distance",
                            style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                        )
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "$estCalories kcal",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = AmberAccent)
                        )
                        Text(
                            text = if (isBengali) "ক্যালোরি ক্ষয়" else "Estimated Burn",
                            style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Standard Tier Presets
                Text(
                    text = if (isBengali) "জনপ্রিয় লক্ষ্য মাত্রা:" else "Recommended Health Tiers:",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                StepGoalPreset.values().forEach { preset ->
                    val isSelected = selectedGoal == preset.steps
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isSelected) Color(0x2600E5FF) else Color.Transparent)
                            .clickable { selectedGoal = preset.steps }
                            .padding(horizontal = 10.dp, vertical = 8.dp)
                            .testTag("preset_tier_${preset.steps}"),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (isBengali) "${preset.titleBn} (${NumberFormat.getNumberInstance(Locale.US).format(preset.steps)})" else "${preset.titleEn} (${NumberFormat.getNumberInstance(Locale.US).format(preset.steps)} steps)",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) CyanAccent else MaterialTheme.colorScheme.onBackground
                                )
                            )
                            Text(
                                text = if (isBengali) preset.descriptionBn else preset.descriptionEn,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontSize = 11.sp
                                )
                            )
                        }

                        if (isSelected) {
                            Box(
                                modifier = Modifier
                                    .size(22.dp)
                                    .clip(CircleShape)
                                    .background(MintAccent),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = Color(0xFF00363A),
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { onSaveGoal(selectedGoal) },
                colors = ButtonDefaults.buttonColors(containerColor = CyanAccent),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.testTag("save_step_goal_button")
            ) {
                Text(
                    text = if (isBengali) "লক্ষ্য সংরক্ষণ করুন" else "Save Goal",
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF00363A)
                    )
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                modifier = Modifier.testTag("cancel_step_goal_button")
            ) {
                Text(
                    text = if (isBengali) "বাতিল" else "Cancel",
                    style = MaterialTheme.typography.labelLarge.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                )
            }
        }
    )
}
