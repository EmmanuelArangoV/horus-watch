package com.horus.wear.presentation.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.horus.wear.presentation.model.MedicalProfile
import com.horus.wear.presentation.network.fetchMedicalProfile
import com.horus.wear.presentation.network.updatePushToken
import com.google.firebase.messaging.FirebaseMessaging
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

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            Log.d("HorusWear", "Permiso de notificaciones concedido")
        } else {
            Log.w("HorusWear", "Permiso de notificaciones denegado")
        }
    }

    LaunchedEffect(Unit) {
        Log.d("HorusWear", "App iniciada, buscando usuario guardado...")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                Log.d("HorusWear", "Solicitando permiso de notificaciones...")
                permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
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
