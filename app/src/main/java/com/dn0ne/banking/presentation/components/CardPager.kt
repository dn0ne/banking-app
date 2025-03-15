package com.dn0ne.banking.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import com.dn0ne.banking.LocalHazeState
import com.dn0ne.banking.domain.Account
import dev.chrisbanes.haze.hazeSource
import kotlin.math.absoluteValue

@Composable
fun CardPagerWithBubbles(
    accounts: List<Account>,
    onOpenAccountClick: () -> Unit,
    modifier: Modifier = Modifier,
    pagerState: PagerState = rememberPagerState { accounts.size }
) {
    Box(
        contentAlignment = Alignment.Center
    ) {
        var pagerSize by remember {
            mutableStateOf(IntSize.Zero)
        }

        val pageOffset = remember(pagerState.currentPage + pagerState.currentPageOffsetFraction) {
            pagerState.currentPage + pagerState.currentPageOffsetFraction
        }
        val progress = remember(pageOffset) {
            if (pageOffset.toInt() % 2 == 0) {
                pageOffset % 2
            } else {
                2 - pageOffset % 2
            }
        }

        BackgroundBubbles(
            containerSize = pagerSize,
            progress = progress,
            modifier = Modifier.matchParentSize()
        )

        CardPager(
            accounts = accounts,
            pagerState = pagerState,
            onOpenAccountClick = onOpenAccountClick,
            modifier = modifier
                .fillMaxWidth()
                .onGloballyPositioned {
                    pagerSize = it.size
                }
        )

        ForegroundBubbles(
            containerSize = pagerSize,
            progress = progress,
            modifier = Modifier.matchParentSize()
        )
    }
}

@Composable
fun CardPager(
    accounts: List<Account>,
    modifier: Modifier = Modifier,
    onOpenAccountClick: () -> Unit,
    pagerState: PagerState = rememberPagerState { accounts.size + 1 }
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HorizontalPager(
            state = pagerState,
            contentPadding = PaddingValues(start = 0.dp, end = 56.dp),
            key = { index -> "${accounts.getOrNull(index)?.toString()}-$index" },
            modifier = modifier
        ) { page ->

            val pageOffset = pagerState.getOffsetDistanceInPages(page)
                .absoluteValue

            accounts.getOrNull(page)?.let {
                CreditCard(
                    account = it,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp)
                        .offset(
                            x = if (page == pagerState.pageCount - 1) {
                                16.dp * (1f - pageOffset).coerceIn(0f..1f)
                            } else 0.dp
                        )
                        .graphicsLayer {
                            transformOrigin = TransformOrigin(
                                pivotFractionX = 0f,
                                pivotFractionY = .5f
                            )
                            val scale = lerp(
                                start = 1f,
                                stop = .85f,
                                fraction = pageOffset
                            )

                            scaleX = scale
                            scaleY = scale
                        }
                )

            } ?: run {
                OpenAccountCard(
                    onClick = onOpenAccountClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp)
                        .offset(
                            x = if (page == pagerState.pageCount - 1) {
                                16.dp * (1f - pageOffset).coerceIn(0f..1f)
                            } else 0.dp
                        )
                        .graphicsLayer {
                            transformOrigin = TransformOrigin(
                                pivotFractionX = 0f,
                                pivotFractionY = .5f
                            )
                            val scale = lerp(
                                start = 1f,
                                stop = .85f,
                                fraction = pageOffset
                            )

                            scaleX = scale
                            scaleY = scale
                        }
                )
            }
        }

        Spacer(Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(pagerState.pageCount) { page ->
                val pageOffset = pagerState.getOffsetDistanceInPages(page).absoluteValue
                if (pageOffset < 5) {
                    val centrality = (1f - pageOffset / 2.5f).coerceIn(0f..1f)
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 4.dp * centrality)
                            .size(8.dp)
                            .graphicsLayer {
                                scaleX = centrality
                                scaleY = centrality
                            }
                            .shadow(elevation = 2.dp, shape = CircleShape)
                            .clip(CircleShape)
                            .background(color = MaterialTheme.colorScheme.inverseOnSurface)

                    )
                }
            }
        }
    }

}

@Composable
fun BackgroundBubbles(
    containerSize: IntSize,
    progress: Float,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .hazeSource(LocalHazeState.current),
        contentAlignment = Alignment.Center
    ) {
        val firstBubbleTranslationX = lerp(
            start = containerSize.width * (-.32f),
            stop = containerSize.width * (-.55f),
            fraction = progress
        )
        val firstBubbleTranslationY = lerp(
            start = containerSize.height * .5f,
            stop = containerSize.height * .35f,
            fraction = progress
        )
        val firstBubbleRotation = lerp(
            start = -35f,
            stop = -70f,
            fraction = progress
        )
        Bubble(
            shape = RoundedCornerShape(45),
            modifier = Modifier
                .matchParentSize()
                .graphicsLayer {
                    val scale = .62f
                    scaleX = scale
                    scaleY = scale
                    translationX = firstBubbleTranslationX
                    translationY = firstBubbleTranslationY
                    rotationZ = firstBubbleRotation
                }
        )

        val secondBubbleTranslationX = lerp(
            start = containerSize.width * .18f,
            stop = containerSize.width * .3f,
            fraction = progress
        )
        val secondBubbleTranslationY = lerp(
            start = containerSize.height * (-.42f),
            stop = containerSize.height * (-.05f),
            fraction = progress
        )
        Bubble(
            modifier = Modifier
                .matchParentSize()
                .graphicsLayer {
                    val scale = .3f
                    scaleX = scale
                    scaleY = scale
                    translationX = secondBubbleTranslationX
                    translationY = secondBubbleTranslationY
                }
        )
    }
}

@Composable
fun ForegroundBubbles(
    containerSize: IntSize,
    progress: Float,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        val firstBubbleTranslationX = lerp(
            start = containerSize.width * (-.37f),
            stop = containerSize.width * (-.35f),
            fraction = progress
        )
        val firstBubbleTranslationY = lerp(
            start = containerSize.height * (-.55f),
            stop = containerSize.height * (-.63f),
            fraction = progress
        )
        Bubble(
            modifier = Modifier
                .matchParentSize()
                .graphicsLayer {
                    val scale = .1f
                    scaleX = scale
                    scaleY = scale
                    translationX = firstBubbleTranslationX
                    translationY = firstBubbleTranslationY
                }
        )

        val secondBubbleTranslationX = lerp(
            start = containerSize.width * .41f,
            stop = containerSize.width * .2f,
            fraction = progress
        )
        Bubble(
            modifier = Modifier
                .matchParentSize()
                .graphicsLayer {
                    val scale = .12f
                    scaleX = scale
                    scaleY = scale
                    translationX = secondBubbleTranslationX
                    translationY = containerSize.height * .5f
                }
        )
    }
}