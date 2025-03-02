package com.dn0ne.banking.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.dn0ne.banking.R

@Composable
fun Bubble(
    modifier: Modifier = Modifier,
    shape: Shape = CircleShape,
    imageOffset: Int = 0
) {
    Box(
        modifier = modifier
            .aspectRatio(1f)
            .shadow(
                8.dp,
                shape = shape,
            )
            .clip(shape)
    ) {
        Image(
            painter = painterResource(R.drawable.gradient),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            alignment = when (imageOffset % 3) {
                0 -> Alignment.Center
                1 -> Alignment.CenterStart
                else -> Alignment.CenterEnd
            },
            modifier = Modifier
                .matchParentSize()
        )
    }
}