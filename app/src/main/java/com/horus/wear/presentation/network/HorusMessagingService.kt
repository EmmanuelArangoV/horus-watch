package com.horus.wear.presentation.network

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HorusMessagingService : FirebaseMessagingService() {

    override fun onCreate() {
        super.onCreate()
        Log.d("HorusMessaging", "Servicio de Mensajería Creado")
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "horus_notifications"
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val channel = NotificationChannel(
                channelId,
                "Alertas Horus",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notificaciones de consulta de perfil"
                enableVibration(true)
                vibrationPattern = longArrayOf(0, 500, 200, 500)
            }
            notificationManager.createNotificationChannel(channel)
            Log.d("HorusMessaging", "Canal de notificaciones verificado/creado")
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("HorusMessaging", "Nuevo Token generado: $token")
        CoroutineScope(Dispatchers.IO).launch {
            updatePushToken(applicationContext, token)
        }
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.d("HorusMessaging", "¡MENSAJE RECIBIDO! De: ${remoteMessage.from}")

        // VIBRAR SIEMPRE (Incluso si no hay permiso de notificación visual)
        try {
            vibrateDevice()
            Log.d("HorusMessaging", "Vibración ejecutada")
        } catch (e: Exception) {
            Log.e("HorusMessaging", "Error al vibrar", e)
        }

        val notification = remoteMessage.notification
        val title = notification?.title ?: remoteMessage.data["title"] ?: "Horus"
        val body = notification?.body ?: remoteMessage.data["body"] ?: "Tu perfil fue consultado"

        Log.d("HorusMessaging", "Datos del mensaje: Title=$title, Body=$body")
        showNotification(title, body)
    }

    private fun vibrateDevice() {
        val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }
        vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE))
    }

    private fun showNotification(title: String, message: String) {
        val channelId = "horus_notifications"
        val builder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVibrate(longArrayOf(0, 500, 200, 500))
            .setAutoCancel(true)

        // Intentar mostrar la notificación
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            NotificationManagerCompat.from(this).notify(System.currentTimeMillis().toInt(), builder.build())
            Log.d("HorusMessaging", "Notificación visual enviada al sistema")
        } else {
            Log.w("HorusMessaging", "No hay permiso POST_NOTIFICATIONS, la notificación no será visual")
        }
    }
}
