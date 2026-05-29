package com.horus.wear.presentation.ui

import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.wear.compose.material3.MaterialTheme
import com.horus.wear.presentation.model.MedicalProfile
import com.horus.wear.presentation.network.fetchMedicalProfile
import com.horus.wear.presentation.ui.components.ErrorScreen
import com.horus.wear.presentation.ui.components.LoadingScreen
import com.horus.wear.presentation.ui.screens.ProfileScreen
import com.horus.wear.presentation.util.getSavedUserId
import kotlinx.coroutines.launch

@Composable
fun HorusWearApp() {
    val context = LocalContext.current
    var screen by remember { mutableStateOf("loading") }
    var profile by remember { mutableStateOf<MedicalProfile?>(null) }
    var errorMsg by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    val userId = getSavedUserId(context) ?: "3a89a6c6-b7a7-446d-8e0c-745d2bbfd4fd"

    LaunchedEffect(userId) {
        screen = "loading"
        val result = fetchMedicalProfile(userId)
        if (result != null) {
            profile = result
            screen = "profile"
        } else {
            errorMsg = "No se pudo cargar el perfil"
            screen = "error"
        }
    }

    MaterialTheme {
        when (screen) {
            "loading" -> LoadingScreen()
            "error"   -> ErrorScreen(errorMsg) {
                scope.launch {
                    screen = "loading"
                    val result = fetchMedicalProfile(userId)
                    if (result != null) { profile = result; screen = "profile" }
                    else { errorMsg = "Sin conexin"; screen = "error" }
                }
            }
            "profile" -> profile?.let { ProfileScreen(it) }
        }
    }
}

