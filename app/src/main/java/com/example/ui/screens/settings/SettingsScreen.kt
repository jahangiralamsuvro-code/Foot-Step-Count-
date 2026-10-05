package com.example.ui.screens.settings

import android.content.Intent
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.SettingsBackupRestore
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.model.UserProfile
import com.example.ui.components.LiquidGlassCard
import com.example.ui.theme.AmberAccent
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.MintAccent
import com.example.ui.viewmodel.Screen

@Composable
fun SettingsScreen(
    userProfile: UserProfile,
    isBengali: Boolean,
    onToggleDarkMode: (Boolean) -> Unit,
    onToggleLanguage: (String) -> Unit,
    onBackupJson: ((String) -> Unit) -> Unit,
    onRestoreJson: (String, (Boolean) -> Unit) -> Unit,
    onExportCsv: ((String) -> Unit) -> Unit,
    onClearAllData: () -> Unit,
    onNavigate: (Screen) -> Unit,
    onBack: () -> Unit
) {
    BackHandler { onBack() }

    val context = LocalContext.current
    var showDeleteConfirmDialog by remember { mutableStateOf(false) }
    var showRestoreDialog by remember { mutableStateOf(false) }
    var restoreJsonInput by remember { mutableStateOf("") }
    var showAboutDialog by remember { mutableStateOf(false) }
    var showPrivacyDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 20.dp)
            .testTag("settings_screen")
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier.testTag("settings_back_button")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = if (isBengali) "সেটিংস ও নিয়ন্ত্রণ" else "Settings",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Theme Toggle Row
        SettingsActionRow(
            icon = Icons.Default.WbSunny,
            tint = AmberAccent,
            title = if (isBengali) "ডার্ক মোড (Theme)" else "Dark Theme",
            trailingContent = {
                Switch(
                    checked = userProfile.isDarkMode,
                    onCheckedChange = onToggleDarkMode,
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = CyanAccent
                    ),
                    modifier = Modifier.testTag("theme_switch")
                )
            },
            onClick = { onToggleDarkMode(!userProfile.isDarkMode) }
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Language Row
        SettingsActionRow(
            icon = Icons.Default.Language,
            tint = CyanAccent,
            title = if (isBengali) "ভাষা (Language)" else "Language",
            trailingText = if (isBengali) "বাংলা" else "English",
            onClick = {
                val next = if (isBengali) "en" else "bn"
                onToggleLanguage(next)
            }
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Notifications Row
        SettingsActionRow(
            icon = Icons.Default.Notifications,
            tint = MintAccent,
            title = if (isBengali) "নোটিফিকেশন ও অ্যালার্ম" else "Notifications",
            trailingText = ">",
            onClick = { onNavigate(Screen.NOTIFICATIONS) }
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Data Backup Row
        SettingsActionRow(
            icon = Icons.Default.CloudUpload,
            tint = CyanAccent,
            title = if (isBengali) "ডাটা ব্যাকআপ (JSON)" else "Data Backup",
            trailingText = "Export JSON",
            onClick = {
                onBackupJson { jsonString ->
                    val sendIntent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, jsonString)
                        type = "application/json"
                    }
                    context.startActivity(Intent.createChooser(sendIntent, "Backup VitalStep JSON"))
                }
            }
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Restore Data Row
        SettingsActionRow(
            icon = Icons.Default.SettingsBackupRestore,
            tint = MintAccent,
            title = if (isBengali) "ডাটা রিস্টোর (JSON)" else "Restore Data",
            trailingText = "Import",
            onClick = { showRestoreDialog = true }
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Export Data Row
        SettingsActionRow(
            icon = Icons.Default.FileDownload,
            tint = AmberAccent,
            title = if (isBengali) "এক্সপোর্ট ডাটা (CSV)" else "Export Data",
            trailingText = "CSV File",
            onClick = {
                onExportCsv { csvContent ->
                    val sendIntent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, csvContent)
                        type = "text/csv"
                    }
                    context.startActivity(Intent.createChooser(sendIntent, "Export VitalStep Records"))
                }
            }
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Privacy Row
        SettingsActionRow(
            icon = Icons.Default.Security,
            tint = CyanAccent,
            title = if (isBengali) "গোপনীয়তা ও নিরাপত্তা" else "Privacy",
            trailingText = ">",
            onClick = { showPrivacyDialog = true }
        )

        Spacer(modifier = Modifier.height(10.dp))

        // About App Row
        SettingsActionRow(
            icon = Icons.Default.Info,
            tint = Color(0xFF64B5F6),
            title = if (isBengali) "অ্যাপ পরিচিতি" else "About App",
            trailingText = "v1.0 Free",
            onClick = { showAboutDialog = true }
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Clear Cache Row
        SettingsActionRow(
            icon = Icons.Default.Refresh,
            tint = AmberAccent,
            title = if (isBengali) "ক্যাশ পরিষ্কার করুন" else "Clear Cache",
            trailingText = "Clean",
            onClick = {
                context.cacheDir.deleteRecursively()
            }
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Delete My Data Row
        SettingsActionRow(
            icon = Icons.Default.Delete,
            tint = Color(0xFFFF5252),
            title = if (isBengali) "সকল তথ্য মুছে ফেলুন" else "Delete My Data",
            trailingText = "Reset",
            onClick = { showDeleteConfirmDialog = true }
        )

        Spacer(modifier = Modifier.height(40.dp))
    }

    // Delete Confirmation Dialog
    if (showDeleteConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirmDialog = false },
            title = { Text(if (isBengali) "আপনি কি নিশ্চিত?" else "Delete All Data?") },
            text = {
                Text(
                    if (isBengali)
                        "আপনার সকল হাঁটার ইতিহাস, ওজন রেকর্ড ও পদক মুছে ফেলা হবে। এটি অপরিবর্তনযোগ্য।"
                    else
                        "This will permanently erase all local walking history, weight records, and achievements. This cannot be undone."
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        onClearAllData()
                        showDeleteConfirmDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF5252))
                ) {
                    Text("Delete Everything")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirmDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Restore Dialog
    if (showRestoreDialog) {
        AlertDialog(
            onDismissRequest = { showRestoreDialog = false },
            title = { Text(if (isBengali) "ব্যাকআপ রিস্টোর করুন" else "Restore From Backup JSON") },
            text = {
                Column {
                    Text(if (isBengali) "ব্যাকআপ JSON কোডটি পেস্ট করুন:" else "Paste your backup JSON content below:")
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = restoreJsonInput,
                        onValueChange = { restoreJsonInput = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp),
                        placeholder = { Text("{\"app\": \"VitalStep\", ...}") }
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onRestoreJson(restoreJsonInput) { success ->
                            showRestoreDialog = false
                        }
                    }
                ) {
                    Text("Restore")
                }
            },
            dismissButton = {
                TextButton(onClick = { showRestoreDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    // About App Dialog
    if (showAboutDialog) {
        AlertDialog(
            onDismissRequest = { showAboutDialog = false },
            title = { Text("VitalStep v1.0") },
            text = {
                Text(
                    "Smart Walking, Fitness & Personal Wellness.\n\n" +
                            "• 100% Free Application\n" +
                            "• Zero Paid Subscriptions or Advertisements\n" +
                            "• Full Offline Storage & Sensors Support\n" +
                            "• English & Bengali Language Engine\n\n" +
                            "Every Step. Every Day. A Healthier You."
                )
            },
            confirmButton = {
                Button(onClick = { showAboutDialog = false }) {
                    Text("OK")
                }
            }
        )
    }

    // Privacy Dialog
    if (showPrivacyDialog) {
        AlertDialog(
            onDismissRequest = { showPrivacyDialog = false },
            title = { Text("Privacy by Design") },
            text = {
                Text(
                    "VitalStep respects your absolute privacy:\n\n" +
                            "• All step data and physical logs are stored on your local device.\n" +
                            "• No personal health records are sold or used for advertising.\n" +
                            "• Physical step sensor is used only to compute active steps.\n" +
                            "• Cloud backup is 100% voluntary and user-controlled."
                )
            },
            confirmButton = {
                Button(onClick = { showPrivacyDialog = false }) {
                    Text("Close")
                }
            }
        )
    }
}

@Composable
private fun SettingsActionRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    tint: Color,
    title: String,
    trailingText: String? = null,
    trailingContent: (@Composable () -> Unit)? = null,
    onClick: () -> Unit
) {
    LiquidGlassCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
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
                        .background(tint.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = tint,
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

            if (trailingContent != null) {
                trailingContent()
            } else if (trailingText != null) {
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
}
