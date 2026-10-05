package com.example

import android.app.Application
import com.example.data.local.PreferencesManager
import com.example.data.local.VitalStepDatabase
import com.example.data.repository.VitalStepRepository
import com.example.receiver.WellnessReminderReceiver
import com.example.sensor.StepSensorManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class VitalStepApplication : Application() {

    val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    lateinit var database: VitalStepDatabase
        private set

    lateinit var preferencesManager: PreferencesManager
        private set

    lateinit var repository: VitalStepRepository
        private set

    lateinit var stepSensorManager: StepSensorManager
        private set

    override fun onCreate() {
        super.onCreate()
        database = VitalStepDatabase.getInstance(this)
        preferencesManager = PreferencesManager(this)
        repository = VitalStepRepository(database.vitalStepDao(), preferencesManager)
        stepSensorManager = StepSensorManager(this)

        applicationScope.launch {
            repository.seedInitialDataIfEmpty()
        }

        // Schedule default reminders
        WellnessReminderReceiver.scheduleReminder(this, WellnessReminderReceiver.TYPE_MORNING, 7, 0)
        WellnessReminderReceiver.scheduleReminder(this, WellnessReminderReceiver.TYPE_WATER, 10, 0)
        WellnessReminderReceiver.scheduleReminder(this, WellnessReminderReceiver.TYPE_WALK, 16, 0)
        WellnessReminderReceiver.scheduleReminder(this, WellnessReminderReceiver.TYPE_EVENING, 19, 0)
        WellnessReminderReceiver.scheduleReminder(this, WellnessReminderReceiver.TYPE_SLEEP, 22, 0)
    }
}
