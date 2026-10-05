package com.example.ui.screens.splash

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.MintAccent
import com.example.ui.theme.MidnightDarkBg
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    hasCompletedOnboarding: Boolean,
    onNavigateNext: (Boolean) -> Unit
) {
    var isReady by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(600)
        isReady = true
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MidnightDarkBg)
            .testTag("splash_screen")
    ) {
        // Hero Background Artwork
        Image(
            painter = painterResource(id = R.drawable.img_hero_splash),
            contentDescription = "VitalStep Hero",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Gradient overlay for contrast
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xCC090E1A),
                            Color(0x66090E1A),
                            Color(0xE6090E1A)
                        )
                    )
                )
        )

        // Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 28.dp, vertical = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            // App Icon
            Image(
                painter = painterResource(id = R.drawable.vitalstep_app_icon),
                contentDescription = "VitalStep Icon",
                modifier = Modifier
                    .size(96.dp)
                    .clip(RoundedCornerShape(24.dp))
            )

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = "VITALSTEP",
                style = MaterialTheme.typography.displayLarge.copy(
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White,
                    letterSpacing = 2.sp
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Every Step. Every Day. A Healthier You.",
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = CyanAccent,
                    fontWeight = FontWeight.Medium
                ),
                textAlign = TextAlign.Center
            )

            Text(
                text = "YOUR DAILY WELLNESS COMPANION",
                style = MaterialTheme.typography.labelSmall.copy(
                    color = Color(0xFFB0BEC5),
                    letterSpacing = 1.2.sp
                ),
                modifier = Modifier.padding(top = 4.dp)
            )

            Spacer(modifier = Modifier.weight(1f))

            AnimatedVisibility(
                visible = isReady,
                enter = fadeIn() + slideInVertically(initialOffsetY = { 60 })
            ) {
                Button(
                    onClick = { onNavigateNext(hasCompletedOnboarding) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .testTag("splash_continue_button"),
                    shape = RoundedCornerShape(28.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CyanAccent
                    )
                ) {
                    Text(
                        text = "Enter VitalStep ➔",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF00363A)
                        )
                    )
                }
            }
        }
    }
}
