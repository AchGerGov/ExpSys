package com.example.hardwareexpert.ui
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily

val NavyBlue = Color(0xFF001F3F)
val NavyDeep = Color(0xFF00132B)
val AccentCyan = Color(0xFF7FDBFF)
val AccentGlow = Color(0xFF4FC3F7)
val TextWhite = Color(0xFFE6F1FF)
val Consolas: FontFamily = FontFamily.Monospace

private val DarkColors = darkColorScheme(primary = AccentCyan, onPrimary = NavyBlue, background = NavyBlue, onBackground = TextWhite, surface = NavyDeep, onSurface = TextWhite, secondary = AccentGlow)

@Composable
fun HardwareExpertTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = DarkColors, content = content)
}
