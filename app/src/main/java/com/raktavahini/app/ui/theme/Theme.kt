package com.raktavahini.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Rakta-Vahini Brand Colors
val RaktaRed = Color(0xFFD32F2F)
val RaktaRedDark = Color(0xFFB71C1C)
val RaktaRedLight = Color(0xFFEF9A9A)
val RaktaRedContainer = Color(0xFFFFCDD2)
val SurfaceWhite = Color(0xFFFFFFFF)
val BackgroundLight = Color(0xFFFFF8F8)
val OnRed = Color(0xFFFFFFFF)
val TextPrimary = Color(0xFF1C1B1F)
val TextSecondary = Color(0xFF49454F)
val EligibleGreen = Color(0xFF2E7D32)
val IneligibleOrange = Color(0xFFE65100)
val CardBackground = Color(0xFFFFF1F1)

private val LightColorScheme = lightColorScheme(
    primary = RaktaRed,
    onPrimary = OnRed,
    primaryContainer = RaktaRedContainer,
    onPrimaryContainer = RaktaRedDark,
    secondary = RaktaRedDark,
    onSecondary = OnRed,
    secondaryContainer = RaktaRedLight,
    onSecondaryContainer = RaktaRedDark,
    tertiary = EligibleGreen,
    background = BackgroundLight,
    surface = SurfaceWhite,
    onBackground = TextPrimary,
    onSurface = TextPrimary,
    error = IneligibleOrange,
)

private val DarkColorScheme = darkColorScheme(
    primary = RaktaRedLight,
    onPrimary = RaktaRedDark,
    primaryContainer = RaktaRedDark,
    onPrimaryContainer = RaktaRedLight,
    secondary = RaktaRedLight,
    onSecondary = RaktaRedDark,
    background = Color(0xFF1C0000),
    surface = Color(0xFF2C0000),
    onBackground = Color(0xFFFFFFFF),
    onSurface = Color(0xFFFFFFFF),
)

@Composable
fun RaktaVahiniTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
