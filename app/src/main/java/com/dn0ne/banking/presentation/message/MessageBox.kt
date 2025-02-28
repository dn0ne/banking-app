package com.dn0ne.banking.presentation.message

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.dn0ne.banking.R

@Preview(showBackground = true)
@Composable
fun MessageBox(
    @PreviewParameter(MessageEventParameterProvider::class)
    message: MessageEvent,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .shadow(elevation = 10.dp, shape = MaterialTheme.shapes.small)
            .clip(MaterialTheme.shapes.small)
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(message.message),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f)
        )

        message.action?.run {
            Spacer(Modifier.width(16.dp))
            TextButton(
                onClick = action
            ) {
                Text(
                    text = stringResource(name)
                )
            }
        }
    }
}

private class MessageEventParameterProvider : PreviewParameterProvider<MessageEvent> {
    override val values: Sequence<MessageEvent>
        get() = sequenceOf(
            MessageEvent(
                message = R.string.app_name,
            ),
            MessageEvent(
                message = R.string.app_name,
                action = MessageAction(
                    name = R.string.app_name,
                    action = {}
                )
            )
        )

}