package com.horus.wear.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.foundation.lazy.TransformingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberTransformingLazyColumnState
import androidx.wear.compose.material3.*
import androidx.wear.compose.material3.lazy.rememberTransformationSpec
import androidx.wear.compose.material3.lazy.transformedHeight
import com.horus.wear.presentation.model.MedicalProfile
import com.horus.wear.presentation.theme.DMSansFamily
import com.horus.wear.presentation.theme.SpaceGroteskFamily
import com.horus.wear.presentation.theme.HorusGreenFg
import com.horus.wear.presentation.theme.LocalHorusColors
import com.horus.wear.presentation.ui.components.*

@Composable
fun ProfileScreen(profile: MedicalProfile, onLogout: () -> Unit, onSync: () -> Unit) {
    val context = LocalContext.current
    val listState = rememberTransformingLazyColumnState()
    val transformationSpec = rememberTransformationSpec()
    val c = LocalHorusColors.current

    AppScaffold {
        ScreenScaffold(
            scrollState = listState,
            edgeButton = {
                EdgeButton(
                    onClick = onSync,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = c.pillBlue,
                        contentColor   = c.pillBlueFg,
                    ),
                ) {
                    Text(
                        text = "SYNC",
                        fontSize = 10.sp,
                        fontFamily = SpaceGroteskFamily,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp,
                    )
                }
            },
        ) { contentPadding ->
            TransformingLazyColumn(
                contentPadding = contentPadding,
                state = listState,
                modifier = Modifier.background(c.background),
            ) {

                // ── HORUS wordmark ──────────────────────────────────────────
                item {
                    HorusHeader(
                        modifier = Modifier
                            .fillMaxWidth()
                            .transformedHeight(this, transformationSpec),
                        transformation = SurfaceTransformation(transformationSpec),
                    )
                }

                // ── Identity card ───────────────────────────────────────────
                item {
                    NameBloodCard(
                        profile = profile,
                        modifier = Modifier
                            .fillMaxWidth()
                            .transformedHeight(this, transformationSpec),
                        transformation = SurfaceTransformation(transformationSpec),
                    )
                }

                // ── Allergies ───────────────────────────────────────────────
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

                // ── Chronic conditions ──────────────────────────────────────
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
                            ConditionCard(
                                condition = cond,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .transformedHeight(this, transformationSpec),
                                transformation = SurfaceTransformation(transformationSpec),
                            )
                        }
                    }
                }

                // ── Medications ─────────────────────────────────────────────
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
                                bgColor = c.pillGreen,
                                textColor = HorusGreenFg,
                                icon = {
                                    PillIllustration(
                                        color = HorusGreenFg,
                                        modifier = Modifier.size(16.dp),
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

                // ── Medical notes ───────────────────────────────────────────
                if (profile.medicalNotes.isNotEmpty()) {
                    item {
                        SectionLabel(
                            text = "NOTAS MÉDICAS",
                            modifier = Modifier
                                .fillMaxWidth()
                                .transformedHeight(this, transformationSpec),
                            transformation = SurfaceTransformation(transformationSpec),
                        )
                    }
                    item {
                        InfoCard(
                            text = profile.medicalNotes,
                            bgColor = c.mutedSurface,
                            textColor = c.textMuted,
                            icon = {},
                            modifier = Modifier
                                .fillMaxWidth()
                                .transformedHeight(this, transformationSpec),
                            transformation = SurfaceTransformation(transformationSpec),
                            fontSize = 11.sp,
                        )
                    }
                }

                // ── Emergency contact ───────────────────────────────────────
                if (profile.emergencyContact.isNotEmpty()) {
                    item {
                        SectionLabel(
                            text = "CONTACTO",
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

                // ── Panic button ────────────────────────────────────────────
                if (profile.emergencyPhone.isNotEmpty()) {
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
                }

                // ── Logout ──────────────────────────────────────────────────
                item {
                    Button(
                        onClick = onLogout,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                            .transformedHeight(this, transformationSpec),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = c.mutedSurface,
                            contentColor   = c.textMuted,
                        ),
                        shape = RoundedCornerShape(99.dp),
                        transformation = SurfaceTransformation(transformationSpec),
                    ) {
                        Text(
                            text = "Cerrar sesión",
                            color = c.textMuted,
                            fontSize = 11.sp,
                            fontFamily = DMSansFamily,
                            fontWeight = FontWeight.Medium,
                        )
                    }
                }

                item { Spacer(modifier = Modifier.height(20.dp)) }
            }
        }
    }
}
