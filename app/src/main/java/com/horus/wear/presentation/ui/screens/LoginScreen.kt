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
import com.horus.wear.presentation.theme.Exo2FontFamily
import com.horus.wear.presentation.theme.LocalHorusColors

@Composable
fun LoginScreen(onLogin: (String) -> Unit) {
    var code by remember { mutableStateOf("") }
    val colors = LocalHorusColors.current
    val listState = rememberTransformingLazyColumnState()

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
                    }
                }

                item {
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
                                            if (btn == "DEL") {
                                                if (code.isNotEmpty()) code = code.dropLast(1)
                                            } else if (btn == "OK") {
                                                if (code.length == 6) {
                                                    // Faking login for the prototype, using the fixed test user UUID
                                                    onLogin("3a89a6c6-b7a7-446d-8e0c-745d2bbfd4fd")
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

