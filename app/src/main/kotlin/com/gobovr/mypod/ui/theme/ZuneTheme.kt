package com.gobovr.mypod.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Zune HD-inspired palette: near-black background, warm burnt-orange accent,
// oversized "Segoe"-style tile typography.
object ZuneColors {
    val Background = Color(0xFF0B0B0B)
    val Surface = Color(0xFF1A1A1A)
    val Accent = Color(0xFFB5490A)   // Zune signature burnt orange
    val AccentAlt = Color(0xFF3E7C9A) // secondary accent (Zune blue tiles)
    val TextPrimary = Color(0xFFF2F2F2)
    val TextSecondary = Color(0xFF9A9A9A)
}

val ZuneColorScheme = darkColorScheme(
    background = ZuneColors.Background,
    surface = ZuneColors.Surface,
    primary = ZuneColors.Accent,
    secondary = ZuneColors.AccentAlt,
    onBackground = ZuneColors.TextPrimary,
    onSurface = ZuneColors.TextPrimary,
    onPrimary = ZuneColors.TextPrimary,
)

// Big, edge-bleeding "twist" typography like the Zune tile UI.
val ZuneTileTitle = TextStyle(
    fontSize = 34.sp,
    fontWeight = FontWeight.Light,
    color = ZuneColors.TextPrimary
)

val ZuneTileSubtitle = TextStyle(
    fontSize = 16.sp,
    fontWeight = FontWeight.Normal,
    color = ZuneColors.TextSecondary
)

@Composable
fun MyPodZuneTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = ZuneColorScheme,
        content = content
    )
}
