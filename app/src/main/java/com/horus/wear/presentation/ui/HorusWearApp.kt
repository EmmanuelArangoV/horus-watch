package com.horus.wear.presentation.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.google.firebase.messaging.FirebaseMessaging
import com.horus.wear.presentation.model.MedicalProfile
import com.horus.wear.presentation.network.HealthUploadService
import com.horus.wear.presentation.network.fetchMedicalProfile
import com.horus.wear.presentation.network.updatePushToken
import com.horus.wear.presentation.theme.HorusWearTheme
import com.horus.wear.presentation.ui.components.ErrorScreen
import com.horus.wear.presentation.ui.components.LoadingScreen
import com.horus.wear.presentation.ui.screens.LoginScreen
import com.horus.wear.presentation.ui.screens.ProfileScreen
import com.horus.wear.presentation.util.clearSession
import com.horus.wear.presentation.util.getSavedUserId
import com.horus.wear.presentation.util.saveUserId
import kotlinx.coroutines.launch

@Composable
fun HorusWearApp() {
    val context = LocalContext.current
    var screen by remember { mutableStateOf("splash") }
    var profile by remember { mutableStateOf<MedicalProfile?>(null) }
    var errorMsg by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    var activeUserId by remember { mutableStateOf<String?>(null) }

    // ── Start/stop background health upload service ───────────────────────
    DisposableEffect(activeUserId) {
        if (activeUserId != null) {
            val intent = Intent(context, HealthUploadService::class.java)
            context.startForegroundService(intent)
            Log.d("HorusWear", "HealthUploadService started for $activeUserId")
        }
        onDispose {
            if (activeUserId != null) {
                context.stopService(Intent(context, HealthUploadService::class.java))
            }
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { results ->
        results.forEach { (perm, granted) ->
            Log.d("HorusWear", "Permiso $perm: ${if (granted) "concedido" else "denegado"}")
        }
    }

    LaunchedEffect(Unit) {
        Log.d("HorusWear", "App iniciada, buscando usuario guardado...")
        val permsToRequest = buildList {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                add(Manifest.permission.POST_NOTIFICATIONS)
            }
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.BODY_SENSORS) != PackageManager.PERMISSION_GRANTED) {
                add(Manifest.permission.BODY_SENSORS)
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q &&
                ContextCompat.checkSelfPermission(context, Manifest.permission.ACTIVITY_RECOGNITION) != PackageManager.PERMISSION_GRANTED) {
                add(Manifest.permission.ACTIVITY_RECOGNITION)
            }
        }
        if (permsToRequest.isNotEmpty()) {
            permissionLauncher.launch(permsToRequest.toTypedArray())
        }
        val savedUserId = getSavedUserId(context)
        Log.d("HorusWear", "Usuario guardado: $savedUserId")
        if (savedUserId != null) {
            activeUserId = savedUserId
        } else {
            screen = "login"
        }
    }

    LaunchedEffect(activeUserId) {
        val uid = activeUserId
        Log.d("HorusWear", "Cambio en activeUserId: $uid")
        if (uid != null) {
            screen = "loading"
            Log.d("HorusWear", "Cargando perfil desde el servidor...")
            val result = fetchMedicalProfile(context, uid)
            Log.d("HorusWear", "Resultado de carga de perfil: ${if (result != null) "ÉXITO" else "FALLO"}")
            
            if (result != null) {
                profile = result
                screen = "profile"
            } else {
                errorMsg = "No se pudo cargar el perfil. Revisa tu conexión."
                screen = "error"
            }

            // Registrar Token de Notificaciones
            Log.d("HorusWear", "Obteniendo token de Firebase...")
            FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val token = task.result
                    Log.d("HorusWear", "TOKEN ACTUAL DEL RELOJ: $token")
                    scope.launch {
                        val success = updatePushToken(context, token)
                        Log.d("HorusWear", "Sincronización de token con servidor: ${if (success) "COMPLETA" else "FALLIDA"}")
                    }
                } else {
                    Log.e("HorusWear", "Error crítico obteniendo token FCM", task.exception)
                }
            }
        }
    }

    HorusWearTheme {
        when (screen) {
            "splash"  -> LoadingScreen()
            "login"   -> LoginScreen(onLogin = { code ->
                saveUserId(context, code)
                activeUserId = code
            })
            "loading" -> LoadingScreen()
            "error"   -> ErrorScreen(errorMsg) {
                activeUserId?.let { uid ->
                    scope.launch {
                        screen = "loading"
                        val result = fetchMedicalProfile(context, uid)
                        if (result != null) { profile = result; screen = "profile" }
                        else { errorMsg = "Sin conexión"; screen = "error" }
                    }
                }
            }
            "profile" -> profile?.let {
                ProfileScreen(
                    profile = it,
                    onLogout = {
                        clearSession(context)
                        activeUserId = null
                        screen = "login"
                    },
                    onSync = {
                        scope.launch {
                            screen = "loading"
                            val result = fetchMedicalProfile(context, it.userId)
                            if (result != null) { profile = result; screen = "profile" }
                            else { errorMsg = "Sin conexión"; screen = "error" }
                        }
                    }
                )
            }
        }
    }
}
