package com.sq.thed_ck_licker.ui.components.buttons

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sq.thed_ck_licker.ecs.systems.CardsSystem.Companion.cardsSystem
import com.sq.thed_ck_licker.ecs.EntityManager.getPlayerID as playerId

@Composable
fun PullCardButton(
    navigationBarPadding: PaddingValues,
    modifier: Modifier,
    latestCard: MutableIntState,
    playerCardCount: MutableIntState
) {
    Column(
        modifier = modifier.padding(
            start = 16.dp,
            bottom = navigationBarPadding.calculateBottomPadding() // Add bottom padding for the navigation bar
        ),
    ) {
        Button(
            onClick = {cardsSystem.pullRandomCardFromEntityDeck(playerId(), latestCard, playerCardCount)}
        ) { Text("draw a card") }
    }
}

