package com.dn0ne.banking.presentation.main

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.calculateTargetValue
import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.dn0ne.banking.R
import com.dn0ne.banking.presentation.components.CardPagerWithBubbles
import com.dn0ne.banking.presentation.components.DetailsSheet
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

@Composable
fun HomeScreen(
    state: BankingState,
    onEvent: (BankingEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
    ) {
        val accounts = remember(state.accountsToTransactions) {
            state.accountsToTransactions.keys.toList()
        }
        LaunchedEffect(accounts) {
            println(accounts.joinToString())
        }

        val pagerState = remember(accounts) {
            PagerState { accounts.size + 1 }
        }
        val currentAccount = accounts.getOrNull(pagerState.currentPage)
        val transactionsForCurrentAccount =
            state.accountsToTransactions.getOrDefault(currentAccount, emptyList())

        var topSpacingHeight by remember {
            mutableIntStateOf(0)
        }
        var topBlockHeight by remember {
            mutableIntStateOf(0)
        }
        val detailsTranslationY = remember {
            Animatable(0f).apply {
                updateBounds(
                    lowerBound = topSpacingHeight.toFloat() - topBlockHeight,
                    upperBound = 0f
                )
            }
        }

        Column(
            modifier = Modifier
                .graphicsLayer {
                    val scale = 1 + .1f * detailsTranslationY.value / topBlockHeight
                    scaleX = scale
                    scaleY = scale
                }
                .onGloballyPositioned {
                    topBlockHeight = it.size.height
                }
        ) {
            Spacer(
                modifier = Modifier
                    .fillMaxHeight(.1f)
                    .onGloballyPositioned {
                        topSpacingHeight = it.size.height
                    }
            )

            Text(
                text = stringResource(R.string.cards),
                style = MaterialTheme.typography.displaySmall,
                color = MaterialTheme.colorScheme.inverseOnSurface,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 28.dp)
            )

            Spacer(Modifier.height(28.dp))


            CardPagerWithBubbles(
                accounts = accounts,
                pagerState = pagerState,
                onOpenAccountClick = {
                    onEvent(BankingEvent.OnOpenAccountClick)
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(20.dp))
        }

        var isDetailsSheetExpanded by remember {
            mutableStateOf(false)
        }
        val density = LocalDensity.current
        val thresholdY = remember { with(density) { 200.dp.toPx() } }

        LaunchedEffect(topSpacingHeight, topBlockHeight) {
            detailsTranslationY.updateBounds(
                lowerBound = topSpacingHeight.toFloat() - topBlockHeight,
                upperBound = 0f
            )
        }

        val decay = rememberSplineBasedDecay<Float>()
        val coroutineScope = rememberCoroutineScope()
        val detailsDraggableState = rememberDraggableState { dragAmount ->
            coroutineScope.launch {
                detailsTranslationY.snapTo(
                    detailsTranslationY.value + dragAmount
                )
            }
        }

        DetailsSheet(
            isExpanded = isDetailsSheetExpanded,
            onExpandClick = {
                isDetailsSheetExpanded = true
                coroutineScope.launch {
                    detailsTranslationY.animateTo(detailsTranslationY.lowerBound!!)
                }
            },
            account = currentAccount,
            transactions = transactionsForCurrentAccount,
            onCloseAccountClick = {
                currentAccount?.let {
                    onEvent(BankingEvent.OnCloseAccountClick(it))
                }
            },
            onReopenAccountClick = {
                currentAccount?.let {
                    onEvent(BankingEvent.OnReopenAccountClick(it))
                }
            },
            modifier = Modifier
                .fillMaxSize()
                .offset {
                    IntOffset(x = 0, y = topBlockHeight + detailsTranslationY.value.toInt())
                }
                .draggable(
                    state = detailsDraggableState,
                    orientation = Orientation.Vertical,
                    onDragStopped = { velocity ->
                        coroutineScope.launch {
                            val decayY = decay.calculateTargetValue(
                                initialValue = detailsTranslationY.value,
                                initialVelocity = velocity
                            )

                            val shouldChangeExpandedState =
                                decayY.absoluteValue > (thresholdY * 0.5f) && if (isDetailsSheetExpanded) decayY > 0f else decayY < 0f
                            if (shouldChangeExpandedState) {
                                detailsTranslationY.animateTo(
                                    if (isDetailsSheetExpanded) {
                                        detailsTranslationY.upperBound ?: 0f
                                    } else {
                                        detailsTranslationY.lowerBound ?: 0f
                                    }
                                )
                                isDetailsSheetExpanded = !isDetailsSheetExpanded
                            } else {
                                detailsTranslationY.animateTo(
                                    if (isDetailsSheetExpanded) {
                                        detailsTranslationY.lowerBound ?: 0f
                                    } else {
                                        detailsTranslationY.upperBound ?: 0f
                                    }
                                )
                            }
                        }
                    }
                )
        )
    }
}