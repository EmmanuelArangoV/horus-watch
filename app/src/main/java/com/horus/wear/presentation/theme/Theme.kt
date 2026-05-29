package com.horus.wear.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.wear.compose.material3.MaterialTheme
import com.horus.wear.R

val Exo2FontFamily = FontFamily(
    Font(R.font.exo2, FontWeight.Normal),
    Font(R.font.exo2, FontWeight.Medium),
    Font(R.font.exo2, FontWeight.Bold),
    Font(R.font.exo2, FontWeight.ExtraBold)
)

data class HorusColors(
    val background: Color,
    val surface: Color,
    val text: Color,
    val textMuted: Color,
    val pillBlue: Color = HorusPastelBlue,
    val pillGreen: Color = HorusPastelGreen,
    val pillPink: Color = HorusPastelPink,
    val pillYellow: Color = HorusPastelYellow,
)

val LightColors = HorusColors(
    background = HorusLightCream,
    surface = HorusSecondaryLight,
    text = HorusTextDark,
    textMuted = Color.Gray,
)

val DarkColors = HorusColors(
    background = HorusDarkBg,
    surface = HorusSecondaryDark,
    text = HorusTextLight,
    textMuted = Color(0xFFA0A0A0),
)

val LocalHorusColors = staticCompositionLocalOf { DarkColors }

@Composable
fun HorusWearTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColors else LightColors

    CompositionLocalProvider(LocalHorusColors provides colors) {
        // We still wrap in Wear Compose MaterialTheme for some internal defaults,
        // but we'll use our LocalHorusColors explicitly in our UI.
        MaterialTheme(
            content = content
        )
    }
}