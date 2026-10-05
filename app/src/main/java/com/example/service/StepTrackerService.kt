package com.example.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.MainActivity
import com.example.R
import com.example.VitalStepApplication
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class StepTrackerService : Service() {

    private val serviceScope = CoroutineScope(Dispatchers.Main + Job())
    private lateinit var notificationManager: NotificationManager

    companion object {
        const val CHANNEL_ID = "vitalstep_step_tracking_channel"
        const val NOTIFICATION_ID = 1001

        fun startService(context: Context) {
            val intent = Intent(context, StepTrackerService::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(intent)
            } else {
                context.startService(intent)
            }
        }

        fun stopService(context: Context) {
            val intent = Intent(context, StepTrackerService::class.java)
            context.stopService(intent)
        }
    }

    override fun onCreate() {
        super.onCreate()
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        createNotificationChannel()

        val app = application as? VitalStepApplication
        if (app != null) {
            app.stepSensorManager.startListening { deltaSteps ->
                // Steps reported
            }

            app.stepSensorManager.stepEvents.onEach { delta ->
                if (delta > 0) {
                    app.repository.recordSteps(delta, isIncremental = true)
                }
            }.launchIn(serviceScope)

            app.repository.getTodayActivity().onEach { activity ->
                if (activity != null) {
                    updateNotification(activity.steps, activity.goal, activity.distanceKm)
                }
            }.launchIn(serviceScope)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val initialNotif = buildNotification(6248, 10000, 4.5f)
        startForeground(NOTIFICATION_ID, initialNotif)
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        val app = application as? VitalStepApplication
        app?.stepSensorManager?.stopListening()
        serviceScope.cancel()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "VitalStep Real-Time Tracker",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Shows real-time walking and wellness activity"
                setShowBadge(false)
            }
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun buildNotification(steps: Int, goal: Int, distanceKm: Float): Notification {
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            Intent(this, MainActivity::class.java),
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val pct = if (goal > 0) (steps * 100 / goal).coerceIn(0, 100) else 0

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("VitalStep Active Walking")
            .setContentText("$steps / $goal steps ($pct%) • ${String.format("%.1f", distanceKm)} km")
            .setSmallIcon(R.drawable.vitalstep_app_icon)
            .setOngoing(true)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()
    }

    private fun updateNotification(steps: Int, goal: Int, distanceKm: Float) {
        val notif = buildNotification(steps, goal, distanceKm)
        notificationManager.notify(NOTIFICATION_ID, notif)
    }
}
