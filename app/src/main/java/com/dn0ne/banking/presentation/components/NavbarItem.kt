package com.dn0ne.banking.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun NavbarItem(
    isSelected: Boolean,
    destination: NavbarDestination,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .width(80.dp)
            .clip(MaterialTheme.shapes.large)
            .clickable {
                if (!isSelected) {
                    destination.onClick()
                }
            }
            .padding(4.dp)
    ) {
        Icon(
            imageVector = destination.icon,
            contentDescription = null,
            tint = if (isSelected) {
                MaterialTheme.colorScheme.secondary
            } else MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(28.dp)
        )
        Text(
            text = destination.label,
            style = MaterialTheme.typography.bodySmall,
            color = if (isSelected) {
                MaterialTheme.colorScheme.secondary
            } else MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = if (isSelected) {
                FontWeight.Bold
            } else FontWeight.Normal
        )
    }
}