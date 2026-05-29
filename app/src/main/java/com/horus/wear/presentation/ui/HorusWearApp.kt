package com.horus.wear.presentation.ui

import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.horus.wear.presentation.model.MedicalProfile
import com.horus.wear.presentation.network.fetchMedicalProfile
import com.horus.wear.presentation.theme.HorusWearTheme
import com.horus.wear.presentation.ui.components.ErrorScreen
import com.horus.wear.presentation.ui.components.LoadingScreen
import com.horus.wear.presentation.ui.screens.LoginScreen
import com.horus.wear.presentation.ui.screens.ProfileScreen
import com.horus.wear.presentation.util.clearUserId
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

    LaunchedEffect(Unit) {
        val savedUserId = getSavedUserId(context)
        if (savedUserId != null) {
            activeUserId = savedUserId
        } else {
            screen = "login"
        }
    }

    LaunchedEffect(activeUserId) {
        val uid = activeUserId
        if (uid != null) {
            screen = "loading"
            val result = fetchMedicalProfile(uid)
            if (result != null) {
                profile = result
                screen = "profile"
            } else {
                errorMsg = "No se pudo cargar el perfil"
                screen = "error"
            }
        } else if (screen != "splash") {
            screen = "login"
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
                        val result = fetchMedicalProfile(uid)
                        if (result != null) { profile = result; screen = "profile" }
                        else { errorMsg = "Sin conexión"; screen = "error" }
                    }
                }
            }
            "profile" -> profile?.let {
                ProfileScreen(
                    profile = it,
                    onLogout = {
                        clearUserId(context)
                        activeUserId = null
                    }
                )
            }
        }
    }
}

