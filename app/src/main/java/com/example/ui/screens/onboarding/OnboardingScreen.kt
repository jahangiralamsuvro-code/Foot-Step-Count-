package com.example.ui.screens.onboarding

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
import androidx.compose.material.icons.filled.DirectionsWalk
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.LiquidGlassCard
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.MintAccent
import com.example.ui.theme.PurpleAccent
import com.example.ui.theme.RoseAccent

@Composable
fun OnboardingScreen(
    currentLanguage: String,
    onLanguageSelected: (String) -> Unit,
    onFinishOnboarding: () -> Unit
) {
    var selectedLang by remember { mutableStateOf(currentLanguage) }
    val isBengali = selectedLang == "bn"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 36.dp)
            .testTag("onboarding_screen"),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = if (isBengali) "স্বাগতম" else "Welcome to",
            style = MaterialTheme.typography.titleMedium.copy(
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )

        Text(
            text = "VITALSTEP",
            style = MaterialTheme.typography.displayMedium.copy(
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onBackground,
                letterSpacing = 1.5.sp
            )
        )

        Text(
            text = if (isBengali) "আপনার প্রতিদিনের সুস্বাস্থ্যের সঙ্গী" else "Your Personal Wellness Companion",
            style = MaterialTheme.typography.bodyMedium.copy(
                color = CyanAccent,
                fontWeight = FontWeight.Medium
            ),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(28.dp))

        // 4 Feature Liquid Glass Cards
        Row(modifier = Modifier.fillMaxWidth()) {
            FeatureCard(
                icon = Icons.Default.DirectionsWalk,
                tint = CyanAccent,
                titleEn = "Track Your Steps",
                titleBn = "কদম গণনা করুন",
                isBengali = isBengali,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(12.dp))
            FeatureCard(
                icon = Icons.Default.Favorite,
                tint = RoseAccent,
                titleEn = "Stay Healthy",
                titleBn = "সুস্থ থাকুন",
                isBengali = isBengali,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            FeatureCard(
                icon = Icons.Default.FitnessCenter,
                tint = PurpleAccent,
                titleEn = "Build Habits",
                titleBn = "অভ্যাস গড়ে তুলুন",
                isBengali = isBengali,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(12.dp))
            FeatureCard(
                icon = Icons.Default.SelfImprovement,
                tint = MintAccent,
                titleEn = "Live Better",
                titleBn = "সুন্দর জীবনযাপন",
                isBengali = isBengali,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Language Selection Section
        Text(
            text = if (isBengali) "ভাষা নির্বাচন করুন" else "Select Language",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onBackground
            )
        )
        Text(
            text = if (isBengali) "Select Language" else "ভাষা নির্বাচন করুন",
            style = MaterialTheme.typography.labelSmall.copy(
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            LanguageToggleButton(
                title = "English",
                isSelected = selectedLang == "en",
                onClick = {
                    selectedLang = "en"
                    onLanguageSelected("en")
                },
                modifier = Modifier
                    .weight(1f)
                    .testTag("onboarding_lang_en")
            )

            Spacer(modifier = Modifier.width(12.dp))

            LanguageToggleButton(
                title = "বাংলা",
                isSelected = selectedLang == "bn",
                onClick = {
                    selectedLang = "bn"
                    onLanguageSelected("bn")
                },
                modifier = Modifier
                    .weight(1f)
                    .testTag("onboarding_lang_bn")
            )
        }

        Spacer(modifier = Modifier.height(40.dp))

        // Finish button
        Button(
            onClick = onFinishOnboarding,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .testTag("onboarding_get_started_button"),
            shape = RoundedCornerShape(28.dp),
            colors = ButtonDefaults.buttonColors(containerColor = CyanAccent)
        ) {
            Text(
                text = if (isBengali) "শুরু করুন ➔" else "Get Started ➔",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF00363A)
                )
            )
        }
    }
}

@Composable
private fun FeatureCard(
    icon: ImageVector,
    tint: Color,
    titleEn: String,
    titleBn: String,
    isBengali: Boolean,
    modifier: Modifier = Modifier
) {
    LiquidGlassCard(
        modifier = modifier.height(115.dp),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
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

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = if (isBengali) titleBn else titleEn,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onBackground
                ),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun LanguageToggleButton(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(50.dp)
            .clip(RoundedCornerShape(25.dp))
            .background(if (isSelected) CyanAccent else Color(0x1F00E5FF))
            .border(
                1.dp,
                if (isSelected) CyanAccent else Color(0x3300E5FF),
                RoundedCornerShape(25.dp)
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall.copy(
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = if (isSelected) Color(0xFF00363A) else MaterialTheme.colorScheme.onBackground
            )
        )
    }
}
