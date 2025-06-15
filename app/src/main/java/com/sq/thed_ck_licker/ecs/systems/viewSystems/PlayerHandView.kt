package com.sq.thed_ck_licker.ecs.systems.viewSystems

import androidx.compose.foundation.layout.offset
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp

@Composable
fun PlayerHandView(
    isZoomed: Boolean,
    playerCardCount: MutableIntState,
    modifier: Modifier,
    latestCard: Int,
    activateCard: () -> Unit,
    onZoomChange: (Boolean) -> Unit
) {
    BadgedBox(
        badge = {
            Badge(
                modifier = Modifier.offset((-20).dp, (5).dp),
                containerColor = Color.Red
            ) {
                Text("${playerCardCount.intValue}")
            }
        }
    ) {
        CardView(
            cardSize = DpSize(120.dp, 170.dp),
            isZoomed,
            latestCard,
            activateCard,
            modifier,
            onZoomChange
        )
    }
}