package com.example.receiver

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.MainActivity
import com.example.R
import java.util.Calendar

class WellnessReminderReceiver : BroadcastReceiver() {

    companion object {
        const val REMINDER_CHANNEL_ID = "vitalstep_wellness_reminders"
        const val EXTRA_TYPE = "reminder_type"

        const val TYPE_MORNING = "morning"
        const val TYPE_WATER = "water"
        const val TYPE_WALK = "walk"
        const val TYPE_EVENING = "evening"
        const val TYPE_SLEEP = "sleep"

        fun scheduleReminder(context: Context, type: String, hour: Int, minute: Int) {
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager ?: return
            val intent = Intent(context, WellnessReminderReceiver::class.java).apply {
                putExtra(EXTRA_TYPE, type)
            }

            val requestCode = when (type) {
                TYPE_MORNING -> 101
                TYPE_WATER -> 102
                TYPE_WALK -> 103
                TYPE_EVENING -> 104
                TYPE_SLEEP -> 105
                else -> 100
            }

            val pendingIntent = PendingIntent.getBroadcast(
                context,
                requestCode,
                intent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )

            val calendar = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, hour)
                set(Calendar.MINUTE, minute)
                set(Calendar.SECOND, 0)
                if (before(Calendar.getInstance())) {
                    add(Calendar.DAY_OF_YEAR, 1)
                }
            }

            alarmManager.setInexactRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                AlarmManager.INTERVAL_DAY,
                pendingIntent
            )
        }
    }

    override fun onReceive(context: Context, intent: Intent?) {
        val type = intent?.getStringExtra(EXTRA_TYPE) ?: TYPE_MORNING
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager ?: return

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                REMINDER_CHANNEL_ID,
                "VitalStep Health & Wellness Reminders",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Daily reminders for hydration, walking, and wellness tips"
            }
            notificationManager.createNotificationChannel(channel)
        }

        val (title, message, notifId) = when (type) {
            TYPE_MORNING -> Triple(
                "Good Morning! শুভ সকাল!",
                "Start your morning with a glass of warm water & gentle walking.",
                201
            )
            TYPE_WATER -> Triple(
                "Hydration Time • পানি পানের সময়",
                "Drink a fresh glass of water (250 ml) to stay active and energized!",
                202
            )
            TYPE_WALK -> Triple(
                "Step Time • হাঁটার সময়!",
                "Time for an active afternoon brisk walk to hit your daily goal!",
                203
            )
            TYPE_EVENING -> Triple(
                "Evening Wellness • সন্ধ্যার স্বাস্থ্যকথা",
                "Take a 15-minute gentle stroll to regulate digestion and clear your mind.",
                204
            )
            TYPE_SLEEP -> Triple(
                "Rest & Recovery • বিশ্রামের সময়",
                "Dim screen lights, relax, and prepare for 7-8 hours of restorative sleep.",
                205
            )
            else -> Triple("VitalStep Reminder", "Keep moving towards a healthier you!", 200)
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            notifId,
            Intent(context, MainActivity::class.java),
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notification = NotificationCompat.Builder(context, REMINDER_CHANNEL_ID)
            .setSmallIcon(R.drawable.vitalstep_app_icon)
            .setContentTitle(title)
            .setContentText(message)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        notificationManager.notify(notifId, notification)
    }
}
