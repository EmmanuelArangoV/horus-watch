package com.horus.wear.presentation.ui.components

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material3.*
import com.horus.wear.presentation.model.AllergyItem
import com.horus.wear.presentation.model.ConditionItem
import com.horus.wear.presentation.model.MedicalProfile
import com.horus.wear.presentation.theme.*

// ── Loading ───────────────────────────────────────────────────────────────────

@Composable
fun LoadingScreen() {
    val c = LocalHorusColors.current
    Box(
        modifier = Modifier.fillMaxSize().background(c.background),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            // HORUS wordmark
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
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = "HORUS",
                    color = c.text,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp,
                    fontFamily = SpaceGroteskFamily,
                )
            }
            CircularProgressIndicator(
                modifier = Modifier.size(24.dp),
                strokeWidth = 3.dp,
                colors = ProgressIndicatorDefaults.colors(indicatorColor = c.pillGreen),
            )
        }
    }
}

// ── Error ─────────────────────────────────────────────────────────────────────

@Composable
fun ErrorScreen(msg: String, onRetry: () -> Unit) {
    val c = LocalHorusColors.current
    Box(
        modifier = Modifier.fillMaxSize().background(c.background),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.padding(horizontal = 20.dp),
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(c.red.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center,
            ) {
                Text("!", color = c.red, fontSize = 16.sp, fontWeight = FontWeight.Bold, fontFamily = SpaceGroteskFamily)
            }
            Text(
                text = msg,
                color = c.textMuted,
                fontSize = 11.sp,
                textAlign = TextAlign.Center,
                fontFamily = DMSansFamily,
                lineHeight = 15.sp,
            )
            Button(
                onClick = onRetry,
                modifier = Modifier.fillMaxWidth().height(36.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = c.mutedSurface,
                    contentColor   = c.text,
                ),
                shape = RoundedCornerShape(99.dp),
            ) {
                Text(
                    text = "Reintentar",
                    fontSize = 12.sp,
                    color = c.text,
                    fontFamily = SpaceGroteskFamily,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
    }
}

// ── Header ────────────────────────────────────────────────────────────────────

@Composable
fun HorusHeader(modifier: Modifier = Modifier, transformation: SurfaceTransformation) {
    val c = LocalHorusColors.current
    ListHeader(modifier = modifier, transformation = transformation) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
        ) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(c.pillBlue)
            )
            Spacer(modifier = Modifier.width(5.dp))
            Text(
                text = "HORUS",
                color = c.text,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp,
                fontFamily = SpaceGroteskFamily,
            )
        }
    }
}

// ── Section label ─────────────────────────────────────────────────────────────

@Composable
fun SectionLabel(
    text: String,
    modifier: Modifier = Modifier,
    transformation: SurfaceTransformation,
) {
    val c = LocalHorusColors.current
    ListHeader(modifier = modifier, transformation = transformation) {
        Text(
            text = text,
            color = c.textMuted,
            fontSize = 10.sp,
            fontFamily = SpaceGroteskFamily,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp,
        )
    }
}

// ── Name / blood type card ────────────────────────────────────────────────────

@Composable
fun NameBloodCard(
    profile: MedicalProfile,
    modifier: Modifier = Modifier,
    transformation: SurfaceTransformation,
) {
    val c = LocalHorusColors.current
    Card(
        onClick = {},
        modifier = modifier
            .padding(horizontal = 8.dp, vertical = 2.dp)
            .border(1.dp, c.border, RoundedCornerShape(24.dp)),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = c.surface),
        transformation = transformation,
    ) {
        Column(
            modifier = Modifier.padding(12.dp).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            // Name
            Text(
                text = profile.name,
                color = c.text,
                fontSize = 15.sp,
                fontFamily = DMSansFamily,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 18.sp,
            )
            // Age
            if (profile.age.isNotEmpty()) {
                Text(
                    text = "${profile.age} años",
                    color = c.textMuted,
                    fontSize = 11.sp,
                    fontFamily = DMSansFamily,
                    fontWeight = FontWeight.Medium,
                )
            }
            // Blood type pill
            if (profile.bloodType != "—") {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(top = 2.dp)
                        .clip(RoundedCornerShape(99.dp))
                        .background(c.pillPink)
                        .padding(horizontal = 12.dp, vertical = 4.dp),
                ) {
                    HeartIllustration(color = c.pillPinkFg, modifier = Modifier.size(11.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Tipo ${profile.bloodType}",
                        color = c.pillPinkFg,
                        fontSize = 12.sp,
                        fontFamily = SpaceGroteskFamily,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }
            // Extra tags column (more compact than Row)
            val tags = buildList {
                if (profile.organDonor) add(Pair("Donante de órganos", c.pillGreen to c.pillGreenFg))
                if (profile.insuranceProvider.isNotEmpty()) add(Pair(profile.insuranceProvider, c.pillBlue to c.pillBlueFg))
            }
            if (tags.isNotEmpty()) {
                Column(
                    modifier = Modifier.padding(top = 2.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    tags.forEach { (label, colors) ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(99.dp))
                                .background(colors.first)
                                .padding(horizontal = 8.dp, vertical = 2.dp),
                        ) {
                            Text(
                                text = label,
                                color = colors.second,
                                fontSize = 9.sp,
                                fontFamily = DMSansFamily,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                            )
                        }
                    }
                }
            }
        }
    }
}

// ── Allergy card ─────────────────────────────────────────────────────────────

@Composable
fun AllergyCard(
    allergy: AllergyItem,
    modifier: Modifier = Modifier,
    transformation: SurfaceTransformation,
) {
    val c = LocalHorusColors.current
    val severityLabel = when (allergy.severity.uppercase()) {
        "LIFE_THREATENING" -> "Riesgo vital"
        "SEVERE"           -> "Severa"
        "MODERATE"         -> "Moderada"
        else               -> "Leve"
    }
    val isHigh = allergy.severity.uppercase() in listOf("LIFE_THREATENING", "SEVERE")
    Card(
        onClick = {},
        modifier = modifier.padding(horizontal = 8.dp, vertical = 2.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = c.pillYellow),
        transformation = transformation,
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            WarningIllustration(color = c.pillYellowFg, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = allergy.name,
                    color = c.pillYellowFg,
                    fontSize = 13.sp,
                    fontFamily = DMSansFamily,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = severityLabel,
                    color = if (isHigh) HorusRed else c.pillYellowFg.copy(alpha = 0.7f),
                    fontSize = 10.sp,
                    fontFamily = DMSansFamily,
                    fontWeight = FontWeight.Medium,
                )
            }
        }
    }
}

// ── Condition card ────────────────────────────────────────────────────────────

@Composable
fun ConditionCard(
    condition: ConditionItem,
    modifier: Modifier = Modifier,
    transformation: SurfaceTransformation,
) {
    val c = LocalHorusColors.current
    val statusLabel = when (condition.status.uppercase()) {
        "ACTIVE"     -> "Activa"
        "MANAGED"    -> "Controlada"
        "IN_REMISSION", "REMISSION" -> "Remisión"
        "RESOLVED"   -> "Resuelta"
        else         -> condition.status
    }
    Card(
        onClick = {},
        modifier = modifier.padding(horizontal = 8.dp, vertical = 2.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = c.pillBlue),
        transformation = transformation,
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ShieldIllustration(color = c.pillBlueFg, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = condition.name,
                    color = c.pillBlueFg,
                    fontSize = 13.sp,
                    fontFamily = DMSansFamily,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = statusLabel,
                    color = c.pillBlueFg.copy(alpha = 0.7f),
                    fontSize = 10.sp,
                    fontFamily = DMSansFamily,
                    fontWeight = FontWeight.Medium,
                )
            }
        }
    }
}

// ── Info card (medications, notes) ───────────────────────────────────────────

@Composable
fun InfoCard(
    text: String,
    icon: @Composable () -> Unit,
    bgColor: Color,
    textColor: Color = HorusPrimary,
    modifier: Modifier = Modifier,
    transformation: SurfaceTransformation,
    fontSize: androidx.compose.ui.unit.TextUnit = 13.sp,
) {
    Card(
        onClick = {},
        modifier = modifier.padding(horizontal = 8.dp, vertical = 2.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor),
        transformation = transformation,
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            icon()
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = text,
                color = textColor,
                fontSize = fontSize,
                fontFamily = DMSansFamily,
                fontWeight = FontWeight.Bold,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                lineHeight = (fontSize.value * 1.3f).sp,
            )
        }
    }
}

// ── Emergency contact card ────────────────────────────────────────────────────

@Composable
fun EmergencyContactCard(
    profile: MedicalProfile,
    modifier: Modifier = Modifier,
    transformation: SurfaceTransformation,
    context: Context,
) {
    val c = LocalHorusColors.current
    Card(
        onClick = {
            if (profile.emergencyPhone.isNotEmpty()) {
                context.startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:${profile.emergencyPhone}")))
            }
        },
        modifier = modifier
            .padding(horizontal = 8.dp, vertical = 2.dp)
            .border(1.dp, c.border, RoundedCornerShape(20.dp)),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = c.surface),
        transformation = transformation,
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(c.pillBlue.copy(alpha = 0.25f)),
                contentAlignment = Alignment.Center,
            ) {
                PhoneIllustration(color = c.pillBlueFg, modifier = Modifier.size(14.dp))
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = profile.emergencyContact,
                    color = c.text,
                    fontSize = 11.sp,
                    fontFamily = DMSansFamily,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = profile.emergencyPhone,
                    color = c.red,
                    fontSize = 13.sp,
                    fontFamily = SpaceGroteskFamily,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = "Toca para llamar",
                    color = c.textMuted,
                    fontSize = 8.sp,
                    fontFamily = DMSansFamily,
                    fontWeight = FontWeight.Medium,
                )
            }
        }
    }
}

// ── Panic button ──────────────────────────────────────────────────────────────

@Composable
fun PanicButton(
    modifier: Modifier = Modifier,
    transformation: SurfaceTransformation,
    phone: String,
    context: Context,
) {
    val c = LocalHorusColors.current
    Button(
        onClick = {
            if (phone.isNotEmpty()) {
                context.startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phone")))
            }
        },
        modifier = modifier
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .fillMaxWidth().height(44.dp),
        colors = ButtonDefaults.buttonColors(containerColor = c.red),
        shape = RoundedCornerShape(99.dp),
        transformation = transformation,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            HeartIllustration(color = Color.White, modifier = Modifier.size(14.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "EMERGENCIA",
                color = Color.White,
                fontSize = 13.sp,
                fontFamily = SpaceGroteskFamily,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp,
            )
        }
    }
}
