package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AchievementItem
import com.example.ui.theme.AmberAccent
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.MidnightDarkSurface
import com.example.ui.theme.liquidGlass

@Composable
fun ConfettiCelebrationDialog(
    achievement: AchievementItem?,
    isBengali: Boolean,
    onDismiss: () -> Unit
) {
    AnimatedVisibility(
        visible = achievement != null,
        enter = fadeIn() + scaleIn(),
        exit = fadeOut()
    ) {
        if (achievement == null) return@AnimatedVisibility

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xCC000000))
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onDismiss
                )
                .testTag("celebration_dialog"),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .padding(32.dp)
                    .liquidGlass(
                        shape = RoundedCornerShape(28.dp),
                        backgroundColor = MidnightDarkSurface.copy(alpha = 0.95f),
                        borderColor = CyanAccent
                    )
                    .padding(28.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(90.dp)
                        .clip(CircleShape)
                        .background(Color(0x33FFB300)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.EmojiEvents,
                        contentDescription = "Trophy",
                        tint = AmberAccent,
                        modifier = Modifier.size(54.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = if (isBengali) "নতুন অর্জন আনলক!" else "Achievement Unlocked!",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = AmberAccent,
                        fontSize = 22.sp
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = if (isBengali) achievement.titleBn else achievement.titleEn,
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    ),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = if (isBengali) achievement.descBn else achievement.descEn,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = Color(0xFFB0BEC5)
                    ),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(containerColor = CyanAccent),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.testTag("dismiss_celebration_button")
                ) {
                    Text(
                        text = if (isBengali) "এগিয়ে যান ➔" else "Keep Moving ➔",
                        color = Color(0xFF00363A),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
