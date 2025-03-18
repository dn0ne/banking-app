package com.dn0ne.banking.presentation.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dn0ne.banking.LocalHazeState
import com.dn0ne.banking.LocalLightHazeStyle
import com.dn0ne.banking.R
import com.dn0ne.banking.presentation.components.BankingButton
import dev.chrisbanes.haze.hazeEffect

@Composable
fun ProfileScreen(
    state: BankingState,
    modifier: Modifier = Modifier,
    onLogoutClick: () -> Unit
) {
    Column(
        modifier = modifier
    ) {
        Spacer(Modifier.fillMaxHeight(.1f))

        Text(
            text = stringResource(R.string.profile),
            style = MaterialTheme.typography.displaySmall,
            color = MaterialTheme.colorScheme.inverseOnSurface,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 28.dp)
        )

        Spacer(Modifier.height(28.dp))

        Column(
            modifier = Modifier
                .fillMaxSize()
                .clip(MaterialTheme.shapes.large)
                .hazeEffect(LocalHazeState.current, LocalLightHazeStyle.current)
                .padding(horizontal = 28.dp)
                .padding(top = 28.dp, bottom = 120.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(150.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surface),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.Person,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(75.dp)
                )
            }

            Spacer(Modifier.height(16.dp))

            Text(
                text = state.username,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(Modifier.weight(1f))

            BankingButton(
                text = stringResource(R.string.log_out),
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                isLoading = false,
                onClick = onLogoutClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            )
        }
    }
}