package com.dn0ne.banking.presentation.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dn0ne.banking.LocalDarkHazeStyle
import com.dn0ne.banking.LocalHazeState
import com.dn0ne.banking.R
import com.dn0ne.banking.domain.Account
import dev.chrisbanes.haze.hazeEffect

@Composable
fun CreditCard(
    account: Account,
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
            .hazeEffect(
                state = LocalHazeState.current,
                style = LocalDarkHazeStyle.current
            )
            .alpha(
                if (account.isActive && account.id.isNotBlank()) 1f else .5f
            )
            .padding(28.dp)
    ) {
        Text(
            text = stringResource(R.string.current_balance),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.inverseOnSurface.copy(.5f),
        )

        val (whole, fraction) = account.balance.toString().split('.')
        Text(
            text = "$$whole,${fraction.take(2).padEnd(2, '0')}",
            style = MaterialTheme.typography.displaySmall.copy(
                shadow = Shadow(
                    color = Color.Black.copy(.25f),
                    blurRadius = 20f,
                    offset = Offset(0f, 10f)
                )
            ),
            color = MaterialTheme.colorScheme.inverseOnSurface,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(Modifier.height(16.dp))

        val id = "${account.id.take(4)} **** **** ${account.id.takeLast(4)}"
        Text(
            text = id,
            style = MaterialTheme.typography.titleMedium.copy(
                shadow = Shadow(
                    color = Color.Black.copy(.25f),
                    blurRadius = 20f,
                    offset = Offset(0f, 10f)
                ),
                letterSpacing = 4.sp
            ),
            color = MaterialTheme.colorScheme.inverseOnSurface
        )

        if (!account.isActive) {
            Spacer(Modifier.height(16.dp))

            Text(
                text = stringResource(R.string.closed),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.inverseOnSurface
            )
        }
    }
}