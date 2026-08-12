package com.bharatisethiya.explorableexplanations.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val Colors = lightColorScheme(
    primary = Color(0xFF315C49),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFD5E8DC),
    onPrimaryContainer = Color(0xFF173B2D),
    secondary = Color(0xFF735C2E),
    secondaryContainer = Color(0xFFF3E1B4),
    background = Color(0xFFF8F7F2),
    surface = Color(0xFFFFFDF8),
    surfaceVariant = Color(0xFFE8E5DC),
    outline = Color(0xFF77756D),
)

@Composable
fun ExplorableTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = Colors, typography = Typography(), content = content)
}
