package com.sq.thed_ck_licker.ecs.systems.viewSystems

import android.annotation.SuppressLint
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun CardRow(
    cardSize: DpSize,
    zoomedCardId: MutableIntState,
    cards: List<Int>,
    onCardClick: (Int) -> Unit,
    onZoomChange: (Int) -> Unit,
    modifier: Modifier
) {
    val fontSize by animateFloatAsState(
        targetValue = if (zoomedCardId.intValue == -1 ) 6f else 10f,
        animationSpec = tween(durationMillis = 200)
    )

    if (zoomedCardId.intValue == -1) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            for (card in cards) {
                val isThisCardZoomed = zoomedCardId.intValue == card
                val screenWidth = LocalConfiguration.current.screenWidthDp.dp.value / 2
                var cardPositionX by remember { mutableFloatStateOf(0f) }
                var cardWidth by remember { mutableFloatStateOf(0f) }
                var isTransitioning by remember { mutableStateOf(false) }

                LaunchedEffect(isThisCardZoomed) {
                    isTransitioning = !isTransitioning
                }

                Box(
                    modifier = Modifier
                        .weight(2f)
                        .zIndex(if (isThisCardZoomed) 99f else 0f)
                        .graphicsLayer {
                            if (isThisCardZoomed) {
                                scaleX = 2f
                                scaleY = 2f
                                translationX = if (!isTransitioning) {
                                    (screenWidth / 2) - cardWidth / 2 - cardPositionX
                                } else {
                                    0f
                                }
                                translationY = 0f // Adjust for vertical centering if needed
                            }
                        }
                        .onGloballyPositioned { layoutCoordinates ->
                            if (isThisCardZoomed && !isTransitioning) {
                                // Capture the card's position and size
                                val newCardPositionX = layoutCoordinates.positionInRoot().x
                                val newCardWidth = layoutCoordinates.size.width.toFloat()

                                if (!isTransitioning) {
                                    cardPositionX = newCardPositionX
                                    cardWidth = newCardWidth
                                    isTransitioning = true
                                }
                            }
                        }
                ) {
                    CardView(
                        cardSize = cardSize,
                        fontSize,
                        isZoomed = isThisCardZoomed,
                        card,
                        { onCardClick(card) },
                        modifier,
                        onZoomChange = onZoomChange
                    )
                }
            }
        }
    } else {
        Box(
            modifier = Modifier
                .zIndex(1f) // Bring to the front
                .pointerInput(Unit) {
                    detectTapGestures {
                        zoomedCardId.intValue = -1 // Dismiss zoom on tap
                    }
                }
        ) {
            CardView(
                cardSize = cardSize * 2,
                fontSize,
                isZoomed = true,
                zoomedCardId.intValue,
                { onCardClick(zoomedCardId.intValue) },
                modifier,
                onZoomChange = onZoomChange
            )
        }
    }

}
