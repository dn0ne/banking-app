package com.dn0ne.banking.presentation.components

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun BankingButton(
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    text: String,
    containerColor: Color,
    contentColor: Color,
    isLoading: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor,
            disabledContainerColor = MaterialTheme.colorScheme.onSurfaceVariant,
            disabledContentColor = MaterialTheme.colorScheme.inverseOnSurface
        ),
        shape = MaterialTheme.shapes.extraSmall,
        enabled = isEnabled,
        modifier = modifier
            .height(56.dp)
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                color = contentColor,
                trackColor = contentColor.copy(alpha = .5f),
                modifier = Modifier.size(16.dp)
            )
        } else {
            Text(
                text = text,
                modifier = Modifier.padding(4.dp)
            )
        }
    }
}