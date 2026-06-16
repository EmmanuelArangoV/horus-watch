package com.horus.wear.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.foundation.lazy.TransformingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberTransformingLazyColumnState
import androidx.wear.compose.material3.*
import com.horus.wear.presentation.network.verifyDeviceCode
import com.horus.wear.presentation.theme.SpaceGroteskFamily
import com.horus.wear.presentation.theme.DMSansFamily
import com.horus.wear.presentation.theme.LocalHorusColors
import com.horus.wear.presentation.util.saveTokens
import kotlinx.coroutines.launch
import androidx.compose.ui.platform.LocalContext

@Composable
fun LoginScreen(onLogin: (String) -> Unit) {
    var code by remember { mutableStateOf("") }
    var errorMsg by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    val c = LocalHorusColors.current
    val listState = rememberTransformingLazyColumnState()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    AppScaffold {
        ScreenScaffold(scrollState = listState) { contentPadding ->
            TransformingLazyColumn(
                contentPadding = contentPadding,
                state = listState,
                modifier = Modifier.background(c.background),
                verticalArrangement = Arrangement.Center,
            ) {

                // ── HORUS wordmark ──────────────────────────────────────────
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 24.dp, bottom = 4.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .clip(CircleShape)
                                    .background(c.pillBlue)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "HORUS",
                                color = c.text,
                                fontSize = 16.sp,
                                fontFamily = SpaceGroteskFamily,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp,
                            )
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Vincula tu reloj",
                            color = c.textMuted,
                            fontSize = 11.sp,
                            fontFamily = DMSansFamily,
                            fontWeight = FontWeight.Medium,
                        )
                    }
                }

                // ── Error message ───────────────────────────────────────────
                if (errorMsg != null) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 4.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(c.red.copy(alpha = 0.10f))
                                .padding(horizontal = 12.dp, vertical = 6.dp),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                text = errorMsg!!,
                                color = c.red,
                                fontSize = 10.sp,
                                fontFamily = DMSansFamily,
                                fontWeight = FontWeight.Medium,
                                textAlign = TextAlign.Center,
                            )
                        }
                    }
                }

                // ── Code display / loading ──────────────────────────────────
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 6.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                colors = ProgressIndicatorDefaults.colors(indicatorColor = c.pillGreen),
                            )
                        } else {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                for (i in 0 until 6) {
                                    val digit = code.getOrNull(i)
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .aspectRatio(1f)
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(
                                                if (digit != null) c.pillBlue
                                                else c.mutedSurface
                                            )
                                            .then(
                                                if (digit == null) Modifier.border(
                                                    1.dp,
                                                    c.border,
                                                    RoundedCornerShape(8.dp),
                                                ) else Modifier
                                            ),
                                        contentAlignment = Alignment.Center,
                                    ) {
                                        if (digit != null) {
                                            Text(
                                                text = digit.toString(),
                                                color = c.pillBlueFg,
                                                fontSize = 15.sp,
                                                fontWeight = FontWeight.Bold,
                                                fontFamily = SpaceGroteskFamily,
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // ── Numpad ──────────────────────────────────────────────────
                val pad = listOf(
                    listOf("1", "2", "3"),
                    listOf("4", "5", "6"),
                    listOf("7", "8", "9"),
                    listOf("DEL", "0", "OK"),
                )

                pad.forEach { row ->
                    item {
                        Row(
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp, vertical = 2.dp),
                        ) {
                            row.forEach { btn ->
                                val isOk  = btn == "OK"
                                val isDel = btn == "DEL"
                                val okEnabled = isOk && code.length == 6

                                val bgColor = when {
                                    isOk  && okEnabled -> c.pillGreen
                                    isOk               -> c.mutedSurface
                                    isDel              -> c.pillPink
                                    else               -> c.mutedSurface
                                }
                                val fgColor = when {
                                    isOk  && okEnabled -> c.pillGreenFg
                                    isDel              -> c.pillPinkFg
                                    else               -> c.text
                                }

                                Box(
                                    modifier = Modifier
                                        .size(38.dp)
                                        .clip(CircleShape)
                                        .background(bgColor)
                                        .clickable {
                                            if (isLoading) return@clickable
                                            when (btn) {
                                                "DEL" -> if (code.isNotEmpty()) code = code.dropLast(1)
                                                "OK"  -> if (code.length == 6) {
                                                    isLoading = true
                                                    errorMsg = null
                                                    scope.launch {
                                                        val result = verifyDeviceCode(code)
                                                        isLoading = false
                                                        if (result.isSuccess) {
                                                            val json = result.getOrNull()
                                                            val accessToken  = json?.optString("accessToken")  ?: ""
                                                            val refreshToken = json?.optString("refreshToken") ?: ""
                                                            val uid          = json?.optJSONObject("user")?.optString("id") ?: ""
                                                            if (uid.isNotEmpty() && accessToken.isNotEmpty()) {
                                                                saveTokens(context, accessToken, refreshToken)
                                                                onLogin(uid)
                                                            } else {
                                                                errorMsg = "Respuesta inválida"
                                                            }
                                                        } else {
                                                            errorMsg = when (result.exceptionOrNull()?.message) {
                                                                "401" -> "Código incorrecto"
                                                                "429" -> "Muchos intentos"
                                                                else  -> "Error de red"
                                                            }
                                                        }
                                                    }
                                                }
                                                else  -> if (code.length < 6) code += btn
                                            }
                                        },
                                    contentAlignment = Alignment.Center,
                                ) {
                                    Text(
                                        text = btn,
                                        color = fgColor,
                                        fontSize = if (isDel || isOk) 10.sp else 14.sp,
                                        fontFamily = SpaceGroteskFamily,
                                        fontWeight = FontWeight.Bold,
                                    )
                                }
                            }
                        }
                    }
                }

                item { Spacer(modifier = Modifier.height(24.dp)) }
            }
        }
    }
}
