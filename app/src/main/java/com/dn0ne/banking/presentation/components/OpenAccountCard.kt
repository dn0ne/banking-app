package com.dn0ne.banking.presentation.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.dn0ne.banking.LocalDarkHazeStyle
import com.dn0ne.banking.LocalHazeState
import com.dn0ne.banking.R
import dev.chrisbanes.haze.hazeEffect

@Composable
fun OpenAccountCard(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .aspectRatio(1.55f)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.inverseOnSurface.copy(.3f),
                shape = MaterialTheme.shapes.large
            )
            .clip(MaterialTheme.shapes.large)
            .clickable(onClick = onClick)
            .hazeEffect(
                state = LocalHazeState.current,
                style = LocalDarkHazeStyle.current
            ),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Rounded.AddCircle,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.inverseOnSurface.copy(alpha = .5f),
            modifier = Modifier.size(48.dp)
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = stringResource(R.string.open_account),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.inverseOnSurface.copy(alpha = .5f)
        )
    }
}