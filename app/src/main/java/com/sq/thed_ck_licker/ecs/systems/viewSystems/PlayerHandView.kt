package com.sq.thed_ck_licker.ecs.systems.viewSystems

import androidx.compose.runtime.Composable
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
    CardView(
        cardSize = DpSize(120.dp, 170.dp),
        isZoomed,
        isForCardRow = false,
        latestCard,
        activateCard,
        modifier,
        onZoomChange
    )

}