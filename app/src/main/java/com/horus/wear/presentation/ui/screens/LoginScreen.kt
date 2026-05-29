package com.horus.wear.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.foundation.lazy.TransformingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberTransformingLazyColumnState
import androidx.wear.compose.material3.*
import com.horus.wear.presentation.network.verifyDeviceCode
import com.horus.wear.presentation.theme.Exo2FontFamily
import com.horus.wear.presentation.theme.LocalHorusColors
import com.horus.wear.presentation.util.saveTokens
import kotlinx.coroutines.launch
import androidx.compose.ui.platform.LocalContext

@Composable
fun LoginScreen(onLogin: (String) -> Unit) {
    var code by remember { mutableStateOf("") }
    var errorMsg by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    
    val colors = LocalHorusColors.current
    val listState = rememberTransformingLazyColumnState()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    AppScaffold {
        ScreenScaffold(
            scrollState = listState,
        ) { contentPadding ->
            TransformingLazyColumn(
                contentPadding = contentPadding,
                state = listState,
                modifier = Modifier.background(colors.background),
                verticalArrangement = Arrangement.Center
            ) {
                item {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(top = 24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "SINCRONIZAR",
                            color = colors.text,
                            fontSize = 14.sp,
                            fontFamily = Exo2FontFamily,
                            fontWeight = FontWeight.ExtraBold,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        if (errorMsg != null) {
                            Text(
                                text = errorMsg!!,
                                color = com.horus.wear.presentation.theme.HorusCritical,
                                fontSize = 11.sp,
                                fontFamily = Exo2FontFamily,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                modifier = Modifier.padding(bottom = 8.dp, start = 16.dp, end = 16.dp)
                            )
                        }
                    }
                }

                item {
                    if (isLoading) {
                        Box(modifier = Modifier.fillMaxWidth().height(26.dp).padding(bottom = 12.dp), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                colors = ProgressIndicatorDefaults.colors(indicatorColor = colors.pillYellow)
                            )
                        }
                    } else {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
                        ) {
                            for (i in 0 until 6) {
                                val digit = code.getOrNull(i)
                                Box(
                                    modifier = Modifier
                                        .padding(2.dp)
                                        .size(22.dp)
                                        .clip(CircleShape)
                                        .background(if (digit != null) colors.pillBlue else colors.surface),
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (digit != null) {
                                        Text(
                                            text = digit.toString(),
                                            color = colors.text,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold,
                                            fontFamily = Exo2FontFamily
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                val pad = listOf(
                    listOf("1", "2", "3"),
                    listOf("4", "5", "6"),
                    listOf("7", "8", "9"),
                    listOf("DEL", "0", "OK")
                )

                pad.forEach { row ->
                    item {
                        Row(
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp)
                        ) {
                            row.forEach { btn ->
                                Box(
                                    modifier = Modifier
                                        .size(38.dp)
                                        .clip(CircleShape)
                                        .background(
                                            when (btn) {
                                                "OK" -> if (code.length == 6) colors.pillGreen else colors.surface
                                                "DEL" -> colors.pillPink
                                                else -> colors.surface
                                            }
                                        )
                                        .clickable {
                                            if (isLoading) return@clickable
                                            
                                            if (btn == "DEL") {
                                                if (code.isNotEmpty()) code = code.dropLast(1)
                                            } else if (btn == "OK") {
                                                if (code.length == 6) {
                                                    isLoading = true
                                                    errorMsg = null
                                                    scope.launch {
                                                        val result = verifyDeviceCode(code)
                                                        isLoading = false
                                                        if (result.isSuccess) {
                                                            val json = result.getOrNull()
                                                            val accessToken = json?.optString("accessToken") ?: ""
                                                            val refreshToken = json?.optString("refreshToken") ?: ""
                                                            val uid = json?.optJSONObject("user")?.optString("id") ?: ""
                                                            
                                                            if (uid.isNotEmpty() && accessToken.isNotEmpty()) {
                                                                saveTokens(context, accessToken, refreshToken)
                                                                onLogin(uid)
                                                            } else {
                                                                errorMsg = "Respuesta del servidor inválida"
                                                            }
                                                        } else {
                                                            when (result.exceptionOrNull()?.message) {
                                                                "401" -> errorMsg = "El código ha expirado o es incorrecto"
                                                                "429" -> errorMsg = "Demasiados intentos, espera unos minutos"
                                                                else  -> errorMsg = "Error de conexión"
                                                            }
                                                        }
                                                    }
                                                }
                                            } else {
                                                if (code.length < 6) code += btn
                                            }
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = btn,
                                        color = colors.text,
                                        fontSize = 12.sp,
                                        fontFamily = Exo2FontFamily,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }

                item { Spacer(modifier = Modifier.height(32.dp)) }
            }
        }
    }
}
