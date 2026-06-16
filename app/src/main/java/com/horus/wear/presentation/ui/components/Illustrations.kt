package com.horus.wear.presentation.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.wear.compose.material3.Icon
import com.horus.wear.R

@Composable
fun HeartIllustration(modifier: Modifier = Modifier, color: Color) {
    Icon(
        painter = painterResource(R.drawable.ic_horus_heart),
        contentDescription = null,
        tint = color,
        modifier = modifier,
    )
}

@Composable
fun WarningIllustration(modifier: Modifier = Modifier, color: Color) {
    Icon(
        painter = painterResource(R.drawable.ic_horus_warning),
        contentDescription = null,
        tint = color,
        modifier = modifier,
    )
}

@Composable
fun PillIllustration(modifier: Modifier = Modifier, color: Color) {
    Icon(
        painter = painterResource(R.drawable.ic_horus_medical),
        contentDescription = null,
        tint = color,
        modifier = modifier,
    )
}

@Composable
fun ShieldIllustration(modifier: Modifier = Modifier, color: Color) {
    Icon(
        painter = painterResource(R.drawable.ic_horus_shield),
        contentDescription = null,
        tint = color,
        modifier = modifier,
    )
}

@Composable
fun PhoneIllustration(modifier: Modifier = Modifier, color: Color) {
    Icon(
        painter = painterResource(R.drawable.ic_horus_phone),
        contentDescription = null,
        tint = color,
        modifier = modifier,
    )
}

@Composable
fun DropletIllustration(modifier: Modifier = Modifier, color: Color) {
    Icon(
        painter = painterResource(R.drawable.ic_horus_droplet),
        contentDescription = null,
        tint = color,
        modifier = modifier,
    )
}

@Composable
fun PersonIllustration(modifier: Modifier = Modifier, color: Color) {
    Icon(
        painter = painterResource(R.drawable.ic_horus_person),
        contentDescription = null,
        tint = color,
        modifier = modifier,
    )
}
