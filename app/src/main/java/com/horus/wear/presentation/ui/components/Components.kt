package com.horus.wear.presentation.ui.components

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
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
import com.horus.wear.presentation.util.severityColor

@Composable
fun LoadingScreen() {
    val colors = LocalHorusColors.current
    Box(
        modifier = Modifier.fillMaxSize().background(colors.background),
        contentAlignment = Alignment.Center,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
            CircularProgressIndicator(colors = ProgressIndicatorDefaults.colors(indicatorColor = colors.pillYellow))
            Text("Cargando...", color = colors.textMuted, fontSize = 12.sp, fontFamily = Exo2FontFamily)
        }
    }
}

@Composable
fun ErrorScreen(msg: String, onRetry: () -> Unit) {
    val colors = LocalHorusColors.current
    Box(
        modifier = Modifier.fillMaxSize().background(colors.background),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(16.dp),
        ) {
            StarIllustration(color = HorusCritical, modifier = Modifier.size(32.dp))
            Text(msg, color = colors.textMuted, fontSize = 11.sp, textAlign = TextAlign.Center, fontFamily = Exo2FontFamily)
            Button(
                onClick = onRetry,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = colors.pillPink),
            ) {
                Text("Reintentar", fontSize = 12.sp, color = HorusTextDark, fontFamily = Exo2FontFamily, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun HorusHeader(modifier: Modifier = Modifier, transformation: SurfaceTransformation) {
    val colors = LocalHorusColors.current
    ListHeader(
        modifier = modifier,
        transformation = transformation,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(colors.pillBlue)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "HORUS",
                color = colors.text,
                fontSize = 13.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 1.sp,
                fontFamily = Exo2FontFamily
            )
        }
    }
}


@Composable
fun NameBloodCard(
    profile: MedicalProfile,
    modifier: Modifier = Modifier,
    transformation: SurfaceTransformation,
) {
    val colors = LocalHorusColors.current
    Card(
        onClick = {},
        modifier = modifier.padding(horizontal = 8.dp, vertical = 4.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = colors.surface),
        transformation = transformation,
    ) {
        Column(
            modifier = Modifier.padding(12.dp).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = profile.name,
                color = colors.text,
                fontSize = 16.sp,
                fontFamily = Exo2FontFamily,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
            if (profile.age.isNotEmpty()) {
                Text(text = "${profile.age} años", color = colors.textMuted, fontSize = 12.sp, fontFamily = Exo2FontFamily)
            }
            if (profile.bloodType != "—") {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(32.dp))
                        .background(colors.pillPink)
                        .padding(horizontal = 16.dp, vertical = 6.dp),
                ) {
                    HeartIllustration(color = HorusTextDark, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = profile.bloodType,
                        color = HorusTextDark,
                        fontSize = 15.sp,
                        fontFamily = Exo2FontFamily,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }
            if (profile.organDonor || profile.insuranceProvider.isNotEmpty()) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    if (profile.organDonor) {
                        Text(
                            text = "Donante de órganos",
                            color = colors.pillGreen,
                            fontSize = 11.sp,
                            fontFamily = Exo2FontFamily,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    if (profile.insuranceProvider.isNotEmpty()) {
                        Text(
                            text = profile.insuranceProvider,
                            color = colors.pillBlue,
                            fontSize = 11.sp,
                            fontFamily = Exo2FontFamily,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun SectionLabel(
    text: String,
    modifier: Modifier = Modifier,
    transformation: SurfaceTransformation,
) {
    val colors = LocalHorusColors.current
    ListHeader(modifier = modifier, transformation = transformation) {
        Text(
            text = text,
            color = colors.textMuted,
            fontSize = 11.sp,
            fontFamily = Exo2FontFamily,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 0.5.sp,
        )
    }
}


@Composable
fun AllergyCard(
    allergy: AllergyItem,
    modifier: Modifier = Modifier,
    transformation: SurfaceTransformation,
) {
    val colors = LocalHorusColors.current
    Card(
        onClick = {},
        modifier = modifier.padding(horizontal = 8.dp, vertical = 4.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = colors.pillYellow),
        transformation = transformation,
    ) {
        Row(
            modifier = Modifier.padding(12.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            StarIllustration(color = HorusTextDark, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = allergy.name,
                    color = HorusTextDark,
                    fontSize = 13.sp,
                    fontFamily = Exo2FontFamily,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = when (allergy.severity.uppercase()) {
                        "LIFE_THREATENING" -> "Riesgo vital"
                        "SEVERE"           -> "Severa"
                        "MODERATE"         -> "Moderada"
                        else               -> "Leve"
                    },
                    color = HorusTextDark.copy(alpha = 0.8f),
                    fontSize = 11.sp,
                    fontFamily = Exo2FontFamily,
                )
            }
        }
    }
}
@Composable
fun ConditionCard(
    condition: ConditionItem,
    modifier: Modifier = Modifier,
    transformation: SurfaceTransformation,
) {
    val colors = LocalHorusColors.current
    Card(
        onClick = {},
        modifier = modifier.padding(horizontal = 8.dp, vertical = 4.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = colors.pillBlue),
        transformation = transformation,
    ) {
        Row(
            modifier = Modifier.padding(12.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ShieldIllustration(color = HorusTextDark, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = condition.name,
                    color = HorusTextDark,
                    fontSize = 13.sp,
                    fontFamily = Exo2FontFamily,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = when (condition.status.uppercase()) {
                        "ACTIVE" -> "Activa"
                        "MANAGED" -> "Controlada"
                        "REMISSION" -> "Remisión"
                        "CHRONIC" -> "Crónica"
                        else -> condition.status
                    },
                    color = HorusTextDark.copy(alpha = 0.8f),
                    fontSize = 11.sp,
                    fontFamily = Exo2FontFamily,
                )
            }
        }
    }
}



@Composable
fun InfoCard(
    text: String,
    icon: @Composable () -> Unit,
    bgColor: Color,
    modifier: Modifier = Modifier,
    transformation: SurfaceTransformation,
    fontSize: androidx.compose.ui.unit.TextUnit = 13.sp,
) {
    Card(
        onClick = {},
        modifier = modifier.padding(horizontal = 8.dp, vertical = 4.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor),
        transformation = transformation,
    ) {
        Row(
            modifier = Modifier.padding(12.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            icon()
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = text,
                color = HorusTextDark,
                fontSize = fontSize,
                fontFamily = Exo2FontFamily,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}


@Composable
fun EmergencyContactCard(
    profile: MedicalProfile,
    modifier: Modifier = Modifier,
    transformation: SurfaceTransformation,
    context: Context,
) {
    val colors = LocalHorusColors.current
    Card(
        onClick = {
            if (profile.emergencyPhone.isNotEmpty()) {
                val intent = Intent(Intent.ACTION_DIAL,
                    Uri.parse("tel:${profile.emergencyPhone}"))
                context.startActivity(intent)
            }
        },
        modifier = modifier.padding(horizontal = 8.dp, vertical = 4.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = colors.surface),
        transformation = transformation,
    ) {
        Row(
            modifier = Modifier.padding(12.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            PhoneIllustration(color = colors.pillBlue, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(text = profile.emergencyContact, color = colors.text, fontSize = 13.sp, fontFamily = Exo2FontFamily, fontWeight = FontWeight.Bold)
                Text(text = profile.emergencyPhone, color = HorusCritical, fontSize = 12.sp, fontFamily = Exo2FontFamily, fontWeight = FontWeight.Bold)
                Text(text = "Toca para llamar", color = colors.textMuted, fontSize = 10.sp, fontFamily = Exo2FontFamily)
            }
        }
    }
}

@Composable
fun PanicButton(
    modifier: Modifier = Modifier,
    transformation: SurfaceTransformation,
    phone: String,
    context: Context,
) {
    Button(
        onClick = {
            if (phone.isNotEmpty()) {
                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phone"))
                context.startActivity(intent)
            }
        },
        modifier = modifier.padding(horizontal = 8.dp, vertical = 8.dp).fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(containerColor = HorusCritical),
        shape = RoundedCornerShape(32.dp),
        transformation = transformation,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
            HeartIllustration(color = Color.White, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "EMERGENCIA",
                color = Color.White,
                fontSize = 14.sp,
                fontFamily = Exo2FontFamily,
                fontWeight = FontWeight.ExtraBold,
            )
        }
    }
}
