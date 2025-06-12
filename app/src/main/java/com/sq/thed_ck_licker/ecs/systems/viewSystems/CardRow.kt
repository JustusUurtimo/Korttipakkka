package com.sq.thed_ck_licker.ecs.systems.viewSystems

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CardRow(cards: List<Int>, onCardClick: (Int) -> Unit, modifier: Modifier) {
    Row {
        for (card in cards) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)
            ) {
                CardView(
                    card,
                    { onCardClick(card) },
                    modifier.height(150.dp)
                )
            }
        }
    }
}
