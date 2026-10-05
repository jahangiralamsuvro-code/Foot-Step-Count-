package com.example.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        if (intent?.action == Intent.ACTION_BOOT_COMPLETED) {
            // Re-schedule daily alarms on boot
            WellnessReminderReceiver.scheduleReminder(context, WellnessReminderReceiver.TYPE_MORNING, 7, 0)
            WellnessReminderReceiver.scheduleReminder(context, WellnessReminderReceiver.TYPE_WATER, 10, 0)
            WellnessReminderReceiver.scheduleReminder(context, WellnessReminderReceiver.TYPE_WALK, 16, 0)
            WellnessReminderReceiver.scheduleReminder(context, WellnessReminderReceiver.TYPE_EVENING, 19, 0)
            WellnessReminderReceiver.scheduleReminder(context, WellnessReminderReceiver.TYPE_SLEEP, 22, 0)
        }
    }
}
