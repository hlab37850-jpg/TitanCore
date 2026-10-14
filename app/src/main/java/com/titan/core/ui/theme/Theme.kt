package com.titan.core.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = NeonGreen,
    secondary = NeonCyan,
    tertiary = NeonRed,
    background = BgBlack,
    surface = SurfaceDark,
    onPrimary = BgBlack,
    onSecondary = BgBlack,
    onBackground = NeonGreen,
    onSurface = NeonGreen
)

@Composable
fun TitanCoreTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = DarkColorScheme, typography = Typography, content = content)
}
