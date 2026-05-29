package com.horus.wear.presentation.util

import androidx.compose.ui.graphics.Color
import com.horus.wear.presentation.theme.HorusCritical
import com.horus.wear.presentation.theme.HorusPastelGreen
import com.horus.wear.presentation.theme.HorusWarning
import com.horus.wear.presentation.theme.HorusPastelYellow

fun bloodTypeLabel(bt: String): String =
    bt.replace("_POSITIVE", "+").replace("_NEGATIVE", "-")
        .replace("A_", "A").replace("B_", "B")
        .replace("O_", "O").replace("AB_", "AB")

fun severityColor(s: String): Color = when (s.uppercase()) {
    "LIFE_THREATENING" -> HorusCritical
    "SEVERE"           -> HorusWarning
    "MODERATE"         -> HorusPastelYellow
    else               -> HorusPastelGreen
}

fun ageFromDob(dob: String?): String {
    if (dob.isNullOrEmpty()) return ""
    return try {
        val parts = dob.split("-")
        val year = parts[0].toInt()
        val currentYear = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)
        "${currentYear - year} años"
    } catch (e: Exception) { "" }
}

