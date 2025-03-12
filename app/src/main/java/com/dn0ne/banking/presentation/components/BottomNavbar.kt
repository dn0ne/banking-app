package com.dn0ne.banking.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun BottomNavbar(
    destinations: List<NavbarDestination>,
    selectedDestination: NavbarDestination,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clip(
                RoundedCornerShape(
                    topStart = 16.dp,
                    topEnd = 16.dp
                )
            )
            .background(color = MaterialTheme.colorScheme.surface)
            .padding(horizontal = 28.dp, vertical = 16.dp)
            .navigationBarsPadding(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(
            space = 28.dp,
            alignment = Alignment.CenterHorizontally
        )
    ) {
        destinations.forEach {
            NavbarItem(
                isSelected = it == selectedDestination,
                destination = it
            )
        }
    }
}

data class NavbarDestination(
    val label: String,
    val icon: ImageVector,
    val onClick: () -> Unit
)