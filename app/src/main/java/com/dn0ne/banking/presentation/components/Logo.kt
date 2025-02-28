package com.dn0ne.banking.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun Logo(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier,
            contentAlignment = Alignment.Center
        ) {
            Bubble(
                modifier = Modifier.matchParentSize()
            )

            Text(
                text = "Bank",
                style = MaterialTheme.typography.displayLarge,
                color = MaterialTheme.colorScheme.surface,
                modifier = Modifier.padding(8.dp)
            )
        }

        Text(
            text = "App",
            style = MaterialTheme.typography.displayLarge,
            color = MaterialTheme.colorScheme.surface,
        )
    }
}