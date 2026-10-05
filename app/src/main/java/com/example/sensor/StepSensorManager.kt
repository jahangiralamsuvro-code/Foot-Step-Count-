package com.example.sensor

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.math.sqrt

enum class SensorStatus {
    AVAILABLE_HARDWARE,
    AVAILABLE_FALLBACK,
    NOT_AVAILABLE,
    PERMISSION_REQUIRED
}

class StepSensorManager(private val context: Context) : SensorEventListener {

    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as? SensorManager
    private val stepCounterSensor: Sensor? = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
    private val stepDetectorSensor: Sensor? = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR)
    private val accelerometerSensor: Sensor? = sensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

    private val _sensorStatus = MutableStateFlow(determineInitialStatus())
    val sensorStatus: StateFlow<SensorStatus> = _sensorStatus.asStateFlow()

    private val _stepEvents = MutableStateFlow(0)
    val stepEvents: StateFlow<Int> = _stepEvents.asStateFlow()

    private var onStepCallback: ((Int) -> Unit)? = null

    private var initialCounterValue: Float? = null
    private var isRegistered = false

    // Accelerometer peak detection fallback variables
    private var lastMagnitude = 0f
    private var lastPeakTime = 0L
    private val peakThreshold = 11.8f // m/s^2

    private fun determineInitialStatus(): SensorStatus {
        return when {
            stepCounterSensor != null -> SensorStatus.AVAILABLE_HARDWARE
            stepDetectorSensor != null -> SensorStatus.AVAILABLE_HARDWARE
            accelerometerSensor != null -> SensorStatus.AVAILABLE_FALLBACK
            else -> SensorStatus.NOT_AVAILABLE
        }
    }

    fun startListening(onStepDetected: (Int) -> Unit) {
        onStepCallback = onStepDetected
        if (isRegistered || sensorManager == null) return

        if (stepCounterSensor != null) {
            sensorManager.registerListener(this, stepCounterSensor, SensorManager.SENSOR_DELAY_UI)
            isRegistered = true
            _sensorStatus.value = SensorStatus.AVAILABLE_HARDWARE
        } else if (stepDetectorSensor != null) {
            sensorManager.registerListener(this, stepDetectorSensor, SensorManager.SENSOR_DELAY_UI)
            isRegistered = true
            _sensorStatus.value = SensorStatus.AVAILABLE_HARDWARE
        } else if (accelerometerSensor != null) {
            sensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_GAME)
            isRegistered = true
            _sensorStatus.value = SensorStatus.AVAILABLE_FALLBACK
        } else {
            _sensorStatus.value = SensorStatus.NOT_AVAILABLE
        }
    }

    fun stopListening() {
        if (!isRegistered || sensorManager == null) return
        sensorManager.unregisterListener(this)
        isRegistered = false
        initialCounterValue = null
    }

    fun emitStep(count: Int = 1) {
        _stepEvents.value += count
        onStepCallback?.invoke(count)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event == null) return

        when (event.sensor.type) {
            Sensor.TYPE_STEP_COUNTER -> {
                val totalSensorSteps = event.values[0]
                if (initialCounterValue == null) {
                    initialCounterValue = totalSensorSteps
                } else {
                    val delta = (totalSensorSteps - initialCounterValue!!).toInt()
                    if (delta > 0) {
                        emitStep(delta)
                        initialCounterValue = totalSensorSteps
                    }
                }
            }
            Sensor.TYPE_STEP_DETECTOR -> {
                if (event.values[0] >= 1.0f) {
                    emitStep(1)
                }
            }
            Sensor.TYPE_ACCELEROMETER -> {
                val x = event.values[0]
                val y = event.values[1]
                val z = event.values[2]
                val magnitude = sqrt((x * x + y * y + z * z).toDouble()).toFloat()

                val now = System.currentTimeMillis()
                if (magnitude > peakThreshold && lastMagnitude <= peakThreshold && (now - lastPeakTime) > 320) {
                    lastPeakTime = now
                    emitStep(1)
                }
                lastMagnitude = magnitude
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}
