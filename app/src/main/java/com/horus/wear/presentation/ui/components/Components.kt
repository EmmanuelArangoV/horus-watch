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
import com.horus.wear.presentation.model.MedicalProfile
import com.horus.wear.presentation.theme.*
import com.horus.wear.presentation.util.severityColor
import androidx.wear.compose.foundation.lazy.TransformingLazyColumn

@Composable
fun LoadingScreen() {
    Box(
        modifier = Modifier.fillMaxSize().background(HorusDark),
        contentAlignment = Alignment.Center,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
            CircularProgressIndicator(colors = ProgressIndicatorDefaults.colors(indicatorColor = HorusRed))
            Text("Cargando...", color = HorusMuted, fontSize = 12.sp)
        }
    }
}

@Composable
fun ErrorScreen(msg: String, onRetry: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize().background(HorusDark),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(16.dp),
        ) {
            Text("⚠️", fontSize = 28.sp)
            Text(msg, color = HorusMuted, fontSize = 11.sp, textAlign = TextAlign.Center)
            Button(
                onClick = onRetry,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = HorusRed),
            ) {
                Text("Reintentar", fontSize = 12.sp, color = Color.White)
            }
        }
    }
}

@Composable
fun HorusHeader(modifier: Modifier = Modifier, transformation: SurfaceTransformation) {
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
                    .background(HorusRed)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "HORUS",
                color = HorusRed,
                fontSize = 11.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 2.sp,
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
    Card(
        onClick = {},
        modifier = modifier.padding(horizontal = 8.dp, vertical = 2.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = HorusSurface),
        transformation = transformation,
    ) {
        Column(
            modifier = Modifier.padding(12.dp).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Text(
                text = profile.name,
                color = HorusCream,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
            if (profile.age.isNotEmpty()) {
                Text(text = profile.age, color = HorusMuted, fontSize = 11.sp)
            }
            if (profile.bloodType != "—") {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color(0x20EF233C))
                        .padding(horizontal = 12.dp, vertical = 4.dp),
                ) {
                    Text(
                        text = "🩸 ${profile.bloodType}",
                        color = HorusRed,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.ExtraBold,
                    )
                }
            }
            if (profile.organDonor) {
                Text(text = "♥ Donante de órganos", color = HorusGreen, fontSize = 10.sp)
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
    ListHeader(modifier = modifier, transformation = transformation) {
        Text(
            text = text,
            color = HorusMuted,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp,
        )
    }
}


@Composable
fun AllergyCard(
    allergy: AllergyItem,
    modifier: Modifier = Modifier,
    transformation: SurfaceTransformation,
) {
    val color = severityColor(allergy.severity)
    Card(
        onClick = {},
        modifier = modifier.padding(horizontal = 8.dp, vertical = 2.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = HorusSurface),
        transformation = transformation,
    ) {
        Row(
            modifier = Modifier.padding(10.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .width(3.dp)
                    .height(32.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(color)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = allergy.name,
                    color = HorusCream,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                )
                Text(
                    text = when (allergy.severity.uppercase()) {
                        "LIFE_THREATENING" -> "⚠️ Riesgo vital"
                        "SEVERE"           -> "Severa"
                        "MODERATE"         -> "Moderada"
                        else               -> "Leve"
                    },
                    color = color,
                    fontSize = 10.sp,
                )
            }
        }
    }
}


@Composable
fun InfoCard(
    text: String,
    modifier: Modifier = Modifier,
    transformation: SurfaceTransformation,
) {
    Card(
        onClick = {},
        modifier = modifier.padding(horizontal = 8.dp, vertical = 2.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = HorusSurface),
        transformation = transformation,
    ) {
        Text(
            text = text,
            color = HorusCream,
            fontSize = 12.sp,
            modifier = Modifier.padding(10.dp).fillMaxWidth(),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
        )
    }
}


@Composable
fun EmergencyContactCard(
    profile: MedicalProfile,
    modifier: Modifier = Modifier,
    transformation: SurfaceTransformation,
    context: Context,
) {
    Card(
        onClick = {
            if (profile.emergencyPhone.isNotEmpty()) {
                val intent = Intent(Intent.ACTION_DIAL,
                    Uri.parse("tel:${profile.emergencyPhone}"))
                context.startActivity(intent)
            }
        },
        modifier = modifier.padding(horizontal = 8.dp, vertical = 2.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = HorusSurface),
        transformation = transformation,
    ) {
        Column(modifier = Modifier.padding(10.dp).fillMaxWidth()) {
            Text(text = profile.emergencyContact, color = HorusCream, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
            Text(text = profile.emergencyPhone, color = HorusRed, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Text(text = "Toca para llamar", color = HorusMuted, fontSize = 9.sp)
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
        modifier = modifier.padding(horizontal = 8.dp, vertical = 4.dp),
        colors = ButtonDefaults.buttonColors(containerColor = HorusRed),
        shape = RoundedCornerShape(14.dp),
        transformation = transformation,
    ) {
        Text(
            text = "🚨 EMERGENCIA",
            color = Color.White,
            fontSize = 13.sp,
            fontWeight = FontWeight.ExtraBold,
        )
    }
}

