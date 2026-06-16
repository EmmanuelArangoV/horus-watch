package com.horus.wear.presentation.theme

import androidx.compose.ui.graphics.Color

// ── Mobile app design tokens (exact match) ───────────────────────────────────

// Light theme surfaces
val HorusBg           = Color(0xFFF9F6ED)   // cream background
val HorusCardLight    = Color(0xFFFFFFFF)   // white card
val HorusMutedBgLight = Color(0xFFF3EFE7)   // muted surface
val HorusBorderLight  = Color(0xFFE8E2D8)   // border

// Dark theme surfaces
val HorusDarkBg       = Color(0xFF1A1510)   // warm dark background
val HorusDarkCard     = Color(0xFF252018)   // dark card
val HorusDarkMutedBg  = Color(0xFF302820)   // dark muted surface
val HorusDarkBorder   = Color(0xFF3A3028)   // dark border

// Typography
val HorusPrimary      = Color(0xFF1A1512)   // warm almost-black
val HorusDarkPrimary  = Color(0xFFF5EFE6)   // warm almost-white
val HorusMuted        = Color(0xFF8C7F6E)
val HorusDarkMuted    = Color(0xFFA89880)

// Accent colors (same in both themes — exact mobile values)
val HorusGreen        = Color(0xFF96C979)
val HorusGreenFg      = Color(0xFF1A3D0A)
val HorusYellow       = Color(0xFFFAD957)
val HorusYellowFg     = Color(0xFF3D2C00)
val HorusBlue         = Color(0xFFA5CCF4)
val HorusBlueFg       = Color(0xFF1A3A5C)
val HorusPink         = Color(0xFFFAB2D3)
val HorusPinkFg       = Color(0xFF7A1A3A)
val HorusRed          = Color(0xFFC0392B)
val HorusDarkRed      = Color(0xFFE05050)

// Legacy aliases so existing usages in Illustrations keep compiling
val HorusLightCream   = HorusBg
val HorusTextDark     = HorusPrimary
val HorusTextLight    = HorusDarkPrimary
val HorusPastelPink   = HorusPink
val HorusPastelBlue   = HorusBlue
val HorusPastelGreen  = HorusGreen
val HorusPastelYellow = HorusYellow
val HorusCritical     = HorusRed
val HorusWarning      = HorusYellow
val HorusSuccess      = HorusGreen
val HorusSecondaryDark  = HorusDarkCard
val HorusSecondaryLight = HorusMutedBgLight
val HorusMutedAlpha     = Color(0x22E8E2D8)
