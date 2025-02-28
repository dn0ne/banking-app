package com.dn0ne.banking.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val colorScheme = lightColorScheme(
    primary = BlueDarker,
    onPrimary = Color.White,
    surface = Color.White,
    onSurface = Color.Black,
    onSurfaceVariant = Color.Gray
)

@Composable
fun BankingTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}