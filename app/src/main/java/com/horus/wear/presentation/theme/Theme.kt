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

// Space Grotesk — display font (wordmarks, headers, labels, buttons)
val SpaceGroteskFamily = FontFamily(
    Font(R.font.space_grotesk_regular,  FontWeight.Normal),
    Font(R.font.space_grotesk_semibold, FontWeight.SemiBold),
    Font(R.font.space_grotesk_bold,     FontWeight.Bold),
    Font(R.font.space_grotesk_bold,     FontWeight.ExtraBold), // 700 is max for SG
)

// DM Sans — body font (names, descriptions, body text)
val DMSansFamily = FontFamily(
    Font(R.font.dm_sans_regular,   FontWeight.Normal),
    Font(R.font.dm_sans_medium,    FontWeight.Medium),
    Font(R.font.dm_sans_bold,      FontWeight.Bold),
    Font(R.font.dm_sans_extrabold, FontWeight.ExtraBold),
)

// Legacy alias — kept so no compile errors while we migrate remaining references
val Exo2FontFamily = DMSansFamily

data class HorusColors(
    val background: Color,
    val surface: Color,
    val mutedSurface: Color,
    val border: Color,
    val text: Color,
    val textMuted: Color,
    val pillBlue: Color     = HorusBlue,
    val pillBlueFg: Color   = HorusBlueFg,
    val pillGreen: Color    = HorusGreen,
    val pillGreenFg: Color  = HorusGreenFg,
    val pillPink: Color     = HorusPink,
    val pillPinkFg: Color   = HorusPinkFg,
    val pillYellow: Color   = HorusYellow,
    val pillYellowFg: Color = HorusYellowFg,
    val red: Color,
)

val LightColors = HorusColors(
    background   = HorusBg,
    surface      = HorusCardLight,
    mutedSurface = HorusMutedBgLight,
    border       = HorusBorderLight,
    text         = HorusPrimary,
    textMuted    = HorusMuted,
    red          = HorusRed,
)

val DarkColors = HorusColors(
    background   = HorusDarkBg,
    surface      = HorusDarkCard,
    mutedSurface = HorusDarkMutedBg,
    border       = HorusDarkBorder,
    text         = HorusDarkPrimary,
    textMuted    = HorusDarkMuted,
    red          = HorusDarkRed,
)

val LocalHorusColors = staticCompositionLocalOf { LightColors }

@Composable
fun HorusWearTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colors = if (darkTheme) DarkColors else LightColors
    CompositionLocalProvider(LocalHorusColors provides colors) {
        MaterialTheme(content = content)
    }
}
