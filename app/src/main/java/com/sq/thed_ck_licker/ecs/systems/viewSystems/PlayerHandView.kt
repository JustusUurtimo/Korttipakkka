package com.sq.thed_ck_licker.ecs.systems.viewSystems

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun PlayerHandView(
    playerCardCount: MutableIntState,
    modifier: Modifier,
    latestCard: Int,
    activateCard: () -> Unit,
) {
    BadgedBox(
        badge = {
            Badge(
                modifier = Modifier.offset((-20).dp, (5).dp),
                containerColor = Color.Red
            ) {
                Text("${playerCardCount.intValue}")
            }
        },
        modifier = modifier
            .width(120.dp)
            .height(170.dp)
            .background(color = Color.Magenta),


        ) {
        CardView(latestCard, activateCard, modifier)
    }
}