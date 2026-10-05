package com.example.ui.screens.notifications

import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.DirectionsWalk
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.filled.WbTwilight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.local.NotificationSettings
import com.example.ui.components.LiquidGlassCard
import com.example.ui.theme.AmberAccent
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.MintAccent
import com.example.ui.theme.PurpleAccent
import com.example.ui.theme.RoseAccent

@Composable
fun NotificationSettingsScreen(
    settings: NotificationSettings,
    isBengali: Boolean,
    onToggle: (String, Boolean) -> Unit,
    onBack: () -> Unit
) {
    BackHandler { onBack() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 20.dp)
            .testTag("notifications_screen")
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier.testTag("notif_back_button")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = if (isBengali) "নোটিফিকেশন ও রিমাইন্ডার" else "Notifications",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Notification Cards List (Matching mockup screen 16)
        NotificationItemCard(
            icon = Icons.Default.WbSunny,
            tint = PurpleAccent,
            title = if (isBengali) "সকালের স্বাস্থ্য পরামর্শ" else "Morning Health Tip",
            time = settings.morningTipTime,
            isChecked = settings.morningTipEnabled,
            onCheckedChange = { onToggle("morning", it) },
            tag = "notif_morning_switch"
        )

        Spacer(modifier = Modifier.height(12.dp))

        NotificationItemCard(
            icon = Icons.Default.Notifications,
            tint = CyanAccent,
            title = if (isBengali) "পানি পানের রিমাইন্ডার" else "Water Reminder",
            time = settings.waterReminderTime,
            isChecked = settings.waterReminderEnabled,
            onCheckedChange = { onToggle("water", it) },
            tag = "notif_water_switch"
        )

        Spacer(modifier = Modifier.height(12.dp))

        NotificationItemCard(
            icon = Icons.Default.DirectionsWalk,
            tint = CyanAccent,
            title = if (isBengali) "হাঁটার রিমাইন্ডার" else "Walking Reminder",
            time = settings.walkingReminderTime,
            isChecked = settings.walkingReminderEnabled,
            onCheckedChange = { onToggle("walk", it) },
            tag = "notif_walk_switch"
        )

        Spacer(modifier = Modifier.height(12.dp))

        NotificationItemCard(
            icon = Icons.Default.EmojiEvents,
            tint = AmberAccent,
            title = if (isBengali) "লক্ষ্য অর্জনের বার্তা" else "Goal Achievement",
            time = "Instant",
            isChecked = settings.goalAchievementEnabled,
            onCheckedChange = { onToggle("goal", it) },
            tag = "notif_goal_switch"
        )

        Spacer(modifier = Modifier.height(12.dp))

        NotificationItemCard(
            icon = Icons.Default.WbTwilight,
            tint = RoseAccent,
            title = if (isBengali) "সন্ধ্যার সুস্থতা পরামর্শ" else "Evening Wellness",
            time = settings.eveningWellnessTime,
            isChecked = settings.eveningWellnessEnabled,
            onCheckedChange = { onToggle("evening", it) },
            tag = "notif_evening_switch"
        )

        Spacer(modifier = Modifier.height(12.dp))

        NotificationItemCard(
            icon = Icons.Default.Bedtime,
            tint = MintAccent,
            title = if (isBengali) "ঘুম ও বিশ্রামের সময়" else "Sleep Reminder",
            time = settings.sleepReminderTime,
            isChecked = settings.sleepReminderEnabled,
            onCheckedChange = { onToggle("sleep", it) },
            tag = "notif_sleep_switch"
        )

        Spacer(modifier = Modifier.height(40.dp))
    }
}

@Composable
private fun NotificationItemCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    tint: Color,
    title: String,
    time: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    tag: String
) {
    LiquidGlassCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(tint.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = tint,
                        modifier = Modifier.size(22.dp)
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    )
                    Text(
                        text = time,
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                }
            }

            Switch(
                checked = isChecked,
                onCheckedChange = onCheckedChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = MintAccent,
                    uncheckedThumbColor = Color(0xFF78909C),
                    uncheckedTrackColor = Color(0x3378909C)
                ),
                modifier = Modifier.testTag(tag)
            )
        }
    }
}
