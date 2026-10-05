package com.example.ui.screens.profile

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
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.DirectionsWalk
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.UserProfile
import com.example.ui.components.LiquidGlassCard
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.MintAccent
import com.example.ui.viewmodel.Screen
import java.text.NumberFormat
import java.util.Locale

@Composable
fun ProfileScreen(
    userProfile: UserProfile,
    isBengali: Boolean,
    onUpdateProfile: (UserProfile) -> Unit,
    onLanguageToggle: (String) -> Unit,
    onStepGoalChange: (Int) -> Unit,
    onToggleGoogleAccount: () -> Unit,
    onNavigate: (Screen) -> Unit
) {
    var showEditProfileDialog by remember { mutableStateOf(false) }
    var showGoalDialog by remember { mutableStateOf(false) }

    // Dialog state
    var editName by remember { mutableStateOf(userProfile.name) }
    var editAge by remember { mutableStateOf(userProfile.age.toString()) }
    var editHeight by remember { mutableStateOf(userProfile.heightCm.toString()) }
    var editWeight by remember { mutableStateOf(userProfile.currentWeightKg.toString()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 20.dp)
            .testTag("profile_screen")
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (isBengali) "প্রোফাইল" else "Profile",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            )

            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color(0x2200E5FF))
                    .clickable { onNavigate(Screen.SETTINGS) }
                    .testTag("profile_settings_icon"),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Settings",
                    tint = CyanAccent,
                    modifier = Modifier.size(22.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Avatar Card (Matches mockup screen 13)
        LiquidGlassCard(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("profile_avatar_card"),
            shape = RoundedCornerShape(26.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // User Avatar Circle
                Box(
                    modifier = Modifier
                        .size(86.dp)
                        .clip(CircleShape)
                        .background(Color(0x3300E5FF))
                        .border(2.5.dp, CyanAccent, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Avatar",
                        tint = CyanAccent,
                        modifier = Modifier.size(46.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = userProfile.name,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                )

                Text(
                    text = userProfile.email,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Profile Menu Items (Matches mockup screen 13)
        ProfileMenuItem(
            icon = Icons.Default.Person,
            title = if (isBengali) "আমার প্রোফাইল বিবরণ" else "My Profile",
            trailingText = "${userProfile.age} yrs",
            onClick = { showEditProfileDialog = true },
            tag = "menu_my_profile"
        )

        Spacer(modifier = Modifier.height(10.dp))

        ProfileMenuItem(
            icon = Icons.Default.Sync,
            title = if (isBengali) "গুগল অ্যাকাউন্ট ও ক্লাউড সিঙ্ক" else "Google Account",
            trailingText = if (userProfile.isGoogleSignedIn) "Connected" else "Connect",
            onClick = onToggleGoogleAccount,
            tag = "menu_google_account"
        )

        Spacer(modifier = Modifier.height(10.dp))

        ProfileMenuItem(
            icon = Icons.Default.Language,
            title = if (isBengali) "ভাষা পরিবর্তন (Language)" else "Language",
            trailingText = if (isBengali) "বাংলা" else "English",
            onClick = {
                val nextLang = if (isBengali) "en" else "bn"
                onLanguageToggle(nextLang)
            },
            tag = "menu_language"
        )

        Spacer(modifier = Modifier.height(10.dp))

        ProfileMenuItem(
            icon = Icons.Default.DirectionsWalk,
            title = if (isBengali) "দৈনিক কদম লক্ষ্য" else "Daily Step Goal",
            trailingText = "${NumberFormat.getNumberInstance(Locale.US).format(userProfile.dailyStepGoal)} >",
            onClick = { showGoalDialog = true },
            tag = "menu_step_goal"
        )

        Spacer(modifier = Modifier.height(10.dp))

        ProfileMenuItem(
            icon = Icons.Default.Spa,
            title = if (isBengali) "সুস্বাস্থ্য ও শারীরিক বিবরণ" else "Health Preferences",
            trailingText = "${userProfile.heightCm.toInt()}cm / ${userProfile.currentWeightKg.toInt()}kg",
            onClick = { onNavigate(Screen.BMI_CALCULATOR) },
            tag = "menu_health_pref"
        )

        Spacer(modifier = Modifier.height(80.dp))
    }

    // Edit Profile Dialog
    if (showEditProfileDialog) {
        AlertDialog(
            onDismissRequest = { showEditProfileDialog = false },
            title = { Text(if (isBengali) "প্রোফাইল সম্পাদনা" else "Edit Profile") },
            text = {
                Column {
                    OutlinedTextField(
                        value = editName,
                        onValueChange = { editName = it },
                        label = { Text("Name") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = editAge,
                        onValueChange = { editAge = it },
                        label = { Text("Age") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = editHeight,
                        onValueChange = { editHeight = it },
                        label = { Text("Height (cm)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = editWeight,
                        onValueChange = { editWeight = it },
                        label = { Text("Current Weight (kg)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val updated = userProfile.copy(
                            name = editName,
                            age = editAge.toIntOrNull() ?: userProfile.age,
                            heightCm = editHeight.toFloatOrNull() ?: userProfile.heightCm,
                            currentWeightKg = editWeight.toFloatOrNull() ?: userProfile.currentWeightKg
                        )
                        onUpdateProfile(updated)
                        showEditProfileDialog = false
                    }
                ) {
                    Text("Save")
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditProfileDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Step Goal Dialog
    if (showGoalDialog) {
        val goals = listOf(3000, 5000, 6000, 8000, 10000, 12000, 15000)
        AlertDialog(
            onDismissRequest = { showGoalDialog = false },
            title = { Text(if (isBengali) "দৈনিক কদম লক্ষ্য নির্বাচন করুন" else "Select Daily Step Goal") },
            text = {
                Column {
                    goals.forEach { g ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onStepGoalChange(g)
                                    showGoalDialog = false
                                }
                                .padding(vertical = 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "${NumberFormat.getNumberInstance(Locale.US).format(g)} steps",
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontWeight = if (g == userProfile.dailyStepGoal) FontWeight.Bold else FontWeight.Normal,
                                    color = if (g == userProfile.dailyStepGoal) CyanAccent else MaterialTheme.colorScheme.onBackground
                                )
                            )
                            if (g == userProfile.dailyStepGoal) {
                                Text("Selected", color = MintAccent, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showGoalDialog = false }) {
                    Text("Close")
                }
            }
        )
    }
}

@Composable
private fun ProfileMenuItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    trailingText: String,
    onClick: () -> Unit,
    tag: String
) {
    LiquidGlassCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .testTag(tag),
        shape = RoundedCornerShape(18.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0x2200E5FF)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = CyanAccent,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = trailingText,
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}
