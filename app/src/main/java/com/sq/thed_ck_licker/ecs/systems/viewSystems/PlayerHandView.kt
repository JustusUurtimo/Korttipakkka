package com.sq.thed_ck_licker.ecs.systems.viewSystems

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp

@Composable
fun PlayerHandView(
    isZoomed: Boolean,
    modifier: Modifier,
    latestCard: Int,
    activateCard: () -> Unit,
    onZoomChange: (Int) -> Unit
) {

    val fontSize by animateFloatAsState(
        targetValue = if (isZoomed ) 9f else 10f,
        animationSpec = tween(durationMillis = 200)
    )
    CardView(
        cardSize = DpSize(120.dp, 170.dp),
        fontSize,
        isZoomed,
        latestCard,
        activateCard,
        modifier,
        onZoomChange
    )

}