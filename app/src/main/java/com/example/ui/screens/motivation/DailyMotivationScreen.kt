package com.example.ui.screens.motivation

import android.content.Intent
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.MotivationQuote
import com.example.ui.components.LiquidGlassCard
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.RoseAccent

@Composable
fun DailyMotivationScreen(
    quotesList: List<MotivationQuote>,
    favoriteQuoteIds: List<Int>,
    isBengali: Boolean,
    onToggleFavorite: (Int, Boolean) -> Unit,
    onBack: () -> Unit
) {
    BackHandler { onBack() }

    val context = LocalContext.current
    var currentIndex by remember { mutableIntStateOf(0) }
    val currentQuote = quotesList.getOrElse(currentIndex) { quotesList.first() }
    val isFav = favoriteQuoteIds.contains(currentQuote.id)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 20.dp)
            .testTag("daily_motivation_screen")
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier.testTag("motivation_back_button")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = if (isBengali) "দৈনিক অনুপ্রেরণা" else "Daily Motivation",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Large Scenic Hero Quote Card (Matches mockup screen 15)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(440.dp)
                .clip(RoundedCornerShape(32.dp))
                .testTag("motivation_hero_card")
        ) {
            // Mountain sunrise artwork
            Image(
                painter = painterResource(id = R.drawable.img_hero_splash),
                contentDescription = "Sunrise Landscape",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // Dramatic gradient overlay
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color(0x33000000),
                                Color(0x66000000),
                                Color(0xE6090E1A)
                            )
                        )
                    )
            )

            // Quote text overlay
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(28.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom
            ) {
                Text(
                    text = "“${currentQuote.quoteEn}”",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 24.sp
                    ),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "“${currentQuote.quoteBn}”",
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = CyanAccent,
                        fontWeight = FontWeight.SemiBold
                    ),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "— ${currentQuote.author}",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = Color(0xFFB0BEC5)
                    )
                )

                Spacer(modifier = Modifier.height(28.dp))

                // Actions: Favorite and Share buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape)
                            .background(Color(0x33FFFFFF)),
                        contentAlignment = Alignment.Center
                    ) {
                        IconButton(onClick = { onToggleFavorite(currentQuote.id, isFav) }) {
                            Icon(
                                imageVector = if (isFav) Icons.Default.Favorite else Icons.Outlined.FavoriteBorder,
                                contentDescription = "Favorite",
                                tint = if (isFav) RoseAccent else Color.White
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(20.dp))

                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape)
                            .background(Color(0x33FFFFFF)),
                        contentAlignment = Alignment.Center
                    ) {
                        IconButton(
                            onClick = {
                                val sendIntent = Intent().apply {
                                    action = Intent.ACTION_SEND
                                    putExtra(Intent.EXTRA_TEXT, "“${currentQuote.quoteEn}”\n\n“${currentQuote.quoteBn}”\n— VitalStep Wellness")
                                    type = "text/plain"
                                }
                                context.startActivity(Intent.createChooser(sendIntent, "Share Motivation"))
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Share,
                                contentDescription = "Share",
                                tint = Color.White
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Next Quote Button
        Button(
            onClick = {
                currentIndex = (currentIndex + 1) % quotesList.size
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("next_quote_button"),
            shape = RoundedCornerShape(26.dp),
            colors = ButtonDefaults.buttonColors(containerColor = CyanAccent)
        ) {
            Text(
                text = if (isBengali) "পরবর্তী বাণী ➔" else "Next Inspiration ➔",
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF00363A)
                )
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Previous Quotes List
        Text(
            text = if (isBengali) "অনুপ্রেরণামূলক বাণী সংগ্রহ" else "Wisdom Collection",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
        )

        Spacer(modifier = Modifier.height(10.dp))

        quotesList.forEachIndexed { idx, quote ->
            val isSelected = idx == currentIndex
            LiquidGlassCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp),
                shape = RoundedCornerShape(18.dp),
                borderColor = if (isSelected) CyanAccent.copy(alpha = 0.6f) else null,
                onClick = { currentIndex = idx }
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (isBengali) quote.quoteBn else quote.quoteEn,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) CyanAccent else MaterialTheme.colorScheme.onBackground
                            )
                        )
                        Text(
                            text = quote.author,
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                    }

                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(40.dp))
    }
}
