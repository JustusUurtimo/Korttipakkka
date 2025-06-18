package com.sq.thed_ck_licker.ecs.systems.viewSystems

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex

@Composable
fun CardRow(cardSize: DpSize, zoomedCardId: MutableIntState, cards: List<Int>, onCardClick: (Int) -> Unit, onZoomChange: (Int) -> Unit, modifier: Modifier) {
    Row {
        for (card in cards) {
            val isThisCardZoomed = zoomedCardId.intValue == card
            Column(
                modifier = Modifier
                    .weight(2f)
                    .zIndex(if (isThisCardZoomed) 99f else 0f)
            ) {
                CardView(
                    cardSize = cardSize,
                    isZoomed = isThisCardZoomed,
                    isForCardRow = true,
                    card,
                    { onCardClick(card) },
                    modifier.height(150.dp),
                    onZoomChange = onZoomChange
                )
            }
        }
    }
}
