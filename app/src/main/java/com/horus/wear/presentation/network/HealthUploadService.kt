package com.horus.wear.presentation.network

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.BatteryManager
import android.os.Build
import android.os.IBinder
import android.util.Log
import com.horus.wear.presentation.util.getSavedUserId
import com.horus.wear.presentation.util.simulateSensors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HealthUploadService : Service() {

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    @Volatile private var latestHr: Int? = null
    @Volatile private var sessionSteps: Int? = null
    @Volatile private var activityMinutes: Int? = null
    @Volatile private var stepBase: Int? = null

    private var sensorManager: SensorManager? = null
    private var sensorListener: SensorEventListener? = null

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        startForegroundNotification()
        setupSensors()
        startUploadLoop()
        Log.d("HealthUploadService", "Service started")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int = START_STICKY

    override fun onDestroy() {
        scope.cancel()
        sensorManager?.unregisterListener(sensorListener)
        Log.d("HealthUploadService", "Service stopped")
        super.onDestroy()
    }

    private fun startForegroundNotification() {
        val channelId = "horus_health_bg"
        val nm = getSystemService(NotificationManager::class.java)
        nm.createNotificationChannel(
            NotificationChannel(channelId, "HORUS Monitoreo", NotificationManager.IMPORTANCE_LOW)
        )
        val notification = Notification.Builder(this, channelId)
            .setContentTitle("HORUS")
            .setContentText("Monitoreando tu salud")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setOngoing(true)
            .build()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            startForeground(9001, notification, ServiceInfo.FOREGROUND_SERVICE_TYPE_HEALTH)
        } else {
            startForeground(9001, notification)
        }
    }

    private val isEmulator get() =
        Build.HARDWARE.contains("goldfish", ignoreCase = true) ||
        Build.HARDWARE.contains("ranchu",   ignoreCase = true) ||
        Build.FINGERPRINT.contains("generic") ||
        Build.FINGERPRINT.contains("emulator") ||
        Build.MODEL.contains("Emulator", ignoreCase = true)

    private fun setupSensors() {
        if (isEmulator) {
            scope.launch {
                simulateSensors(
                    onHeartRate       = { latestHr = it },
                    onSteps           = { sessionSteps = it },
                    onActivityMinutes = { activityMinutes = it },
                )
            }
            return
        }

        sensorManager = getSystemService(SensorManager::class.java)
        val hrSensor   = sensorManager?.getDefaultSensor(Sensor.TYPE_HEART_RATE)
        val stepSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

        if (hrSensor == null && stepSensor == null) {
            scope.launch {
                simulateSensors(
                    onHeartRate       = { latestHr = it },
                    onSteps           = { sessionSteps = it },
                    onActivityMinutes = { activityMinutes = it },
                )
            }
            return
        }

        sensorListener = object : SensorEventListener {
            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
            override fun onSensorChanged(event: SensorEvent) {
                when (event.sensor.type) {
                    Sensor.TYPE_HEART_RATE -> latestHr = event.values[0].toInt()
                    Sensor.TYPE_STEP_COUNTER -> {
                        val raw = event.values[0].toInt()
                        if (stepBase == null) stepBase = raw
                        sessionSteps = raw - (stepBase ?: raw)
                    }
                }
            }
        }
        hrSensor?.let   { sensorManager?.registerListener(sensorListener, it, SensorManager.SENSOR_DELAY_NORMAL) }
        stepSensor?.let { sensorManager?.registerListener(sensorListener, it, SensorManager.SENSOR_DELAY_NORMAL) }
    }

    private fun startUploadLoop() {
        scope.launch {
            delay(6_000L)
            while (true) {
                val userId = getSavedUserId(this@HealthUploadService)
                if (userId != null) {
                    try {
                        val bm      = getSystemService(BatteryManager::class.java)
                        val battery = bm?.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
                        val steps   = sessionSteps
                        sendHealthReadings(
                            context         = this@HealthUploadService,
                            heartRate       = latestHr,
                            steps           = steps,
                            calories        = steps?.let { (it * 0.04).toInt() },
                            activityMinutes = activityMinutes,
                            battery         = battery,
                        )
                        Log.d("HealthUploadService", "Uploaded: hr=$latestHr steps=$steps")
                    } catch (e: Exception) {
                        Log.w("HealthUploadService", "Upload failed", e)
                    }
                }
                delay(30 * 60 * 1000L) // 30 minutes
            }
        }
    }
}
