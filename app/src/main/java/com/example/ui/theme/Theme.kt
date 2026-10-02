package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val TalkFlowColorScheme = darkColorScheme(
    primary = NeonMint,
    onPrimary = DeepNightBlue,
    primaryContainer = Surface2,
    onPrimaryContainer = NeonMint,
    secondary = VibrantPurple,
    onSecondary = WhiteText,
    secondaryContainer = Surface3,
    onSecondaryContainer = LilacAccent,
    tertiary = LilacAccent,
    onTertiary = DeepNightBlue,
    background = DeepNightBlue,
    onBackground = WhiteText,
    surface = ElevatedSurface,
    onSurface = WhiteText,
    surfaceVariant = Surface2,
    onSurfaceVariant = CoolGray,
    error = CoralRed,
    onError = WhiteText,
    outline = BorderLine,
    outlineVariant = BorderLine
)

@Composable
fun TalkFlowTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = TalkFlowColorScheme,
        typography = Typography,
        content = content
    )
}
