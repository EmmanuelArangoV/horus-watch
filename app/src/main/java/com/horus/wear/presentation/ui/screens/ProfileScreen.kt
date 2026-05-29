package com.horus.wear.presentation.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.foundation.lazy.TransformingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberTransformingLazyColumnState
import androidx.wear.compose.material3.*
import androidx.wear.compose.material3.lazy.rememberTransformationSpec
import androidx.wear.compose.material3.lazy.transformedHeight
import com.horus.wear.presentation.model.MedicalProfile
import com.horus.wear.presentation.theme.LocalHorusColors
import com.horus.wear.presentation.ui.components.*
import com.horus.wear.presentation.util.BASE_URL

@Composable
fun ProfileScreen(profile: MedicalProfile, onLogout: () -> Unit) {
    val context = LocalContext.current
    val listState = rememberTransformingLazyColumnState()
    val transformationSpec = rememberTransformationSpec()
    val colors = LocalHorusColors.current

    AppScaffold {
        ScreenScaffold(
            scrollState = listState,
            edgeButton = {
                EdgeButton(
                    onClick = {
                        val intent = Intent(Intent.ACTION_VIEW,
                            Uri.parse("$BASE_URL/emergency/${profile.userId}"))
                        context.startActivity(intent)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colors.pillBlue,
                        contentColor = com.horus.wear.presentation.theme.HorusTextDark,
                    ),
                ) {
                    Text("Ver ficha", fontSize = 11.sp, fontFamily = com.horus.wear.presentation.theme.Exo2FontFamily, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
                }
            }
        ) { contentPadding ->
            TransformingLazyColumn(
                contentPadding = contentPadding,
                state = listState,
                modifier = Modifier.background(colors.background),
            ) {
                item {
                    HorusHeader(
                        modifier = Modifier
                            .fillMaxWidth()
                            .transformedHeight(this, transformationSpec),
                        transformation = SurfaceTransformation(transformationSpec),
                    )
                }

                item {
                    NameBloodCard(
                        profile = profile,
                        modifier = Modifier
                            .fillMaxWidth()
                            .transformedHeight(this, transformationSpec),
                        transformation = SurfaceTransformation(transformationSpec),
                    )
                }

                if (profile.allergies.isNotEmpty()) {
                    item {
                        SectionLabel(
                            text = "ALERGIAS",
                            modifier = Modifier
                                .fillMaxWidth()
                                .transformedHeight(this, transformationSpec),
                            transformation = SurfaceTransformation(transformationSpec),
                        )
                    }
                    profile.allergies.forEach { allergy ->
                        item {
                            AllergyCard(
                                allergy = allergy,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .transformedHeight(this, transformationSpec),
                                transformation = SurfaceTransformation(transformationSpec),
                            )
                        }
                    }
                }

                if (profile.medications.isNotEmpty()) {
                    item {
                        SectionLabel(
                            text = "MEDICAMENTOS",
                            modifier = Modifier
                                .fillMaxWidth()
                                .transformedHeight(this, transformationSpec),
                            transformation = SurfaceTransformation(transformationSpec),
                        )
                    }
                    profile.medications.forEach { med ->
                        item {
                            InfoCard(
                                text = med,
                                bgColor = colors.pillGreen,
                                icon = {
                                    PillIllustration(
                                        color = com.horus.wear.presentation.theme.HorusTextDark,
                                        modifier = Modifier.size(20.dp)
                                    )
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .transformedHeight(this, transformationSpec),
                                transformation = SurfaceTransformation(transformationSpec),
                            )
                        }
                    }
                }

                if (profile.conditions.isNotEmpty()) {
                    item {
                        SectionLabel(
                            text = "CONDICIONES",
                            modifier = Modifier
                                .fillMaxWidth()
                                .transformedHeight(this, transformationSpec),
                            transformation = SurfaceTransformation(transformationSpec),
                        )
                    }
                    profile.conditions.forEach { cond ->
                        item {
                            InfoCard(
                                text = cond,
                                bgColor = colors.pillBlue,
                                icon = {
                                    ShieldIllustration(
                                        color = com.horus.wear.presentation.theme.HorusTextDark,
                                        modifier = Modifier.size(20.dp)
                                    )
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .transformedHeight(this, transformationSpec),
                                transformation = SurfaceTransformation(transformationSpec),
                            )
                        }
                    }
                }

                if (profile.emergencyContact.isNotEmpty()) {
                    item {
                        SectionLabel(
                            text = "EMERGENCIA",
                            modifier = Modifier
                                .fillMaxWidth()
                                .transformedHeight(this, transformationSpec),
                            transformation = SurfaceTransformation(transformationSpec),
                        )
                    }
                    item {
                        EmergencyContactCard(
                            profile = profile,
                            modifier = Modifier
                                .fillMaxWidth()
                                .transformedHeight(this, transformationSpec),
                            transformation = SurfaceTransformation(transformationSpec),
                            context = context,
                        )
                    }
                }

                item {
                    PanicButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .transformedHeight(this, transformationSpec),
                        transformation = SurfaceTransformation(transformationSpec),
                        phone = profile.emergencyPhone,
                        context = context,
                    )
                }

                item {
                    Button(
                        onClick = onLogout,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                            .transformedHeight(this, transformationSpec),
                        colors = ButtonDefaults.buttonColors(containerColor = colors.surface),
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(32.dp),
                        transformation = SurfaceTransformation(transformationSpec),
                    ) {
                        Text(
                            text = "Cerrar sesión",
                            color = colors.textMuted,
                            fontSize = 12.sp,
                            fontFamily = com.horus.wear.presentation.theme.Exo2FontFamily,
                            fontWeight = androidx.compose.ui.text.font.FontWeight.Medium,
                        )
                    }
                }

                item { Spacer(modifier = Modifier.height(16.dp)) }
            }
        }
    }
}
