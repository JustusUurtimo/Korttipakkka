package com.sq.thed_ck_licker.ecs.systems.viewSystems.navigationViews.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.sq.thed_ck_licker.R
import com.sq.thed_ck_licker.ecs.systems.viewSystems.CardRow
import com.sq.thed_ck_licker.helpers.navigation.GameNavigator
import com.sq.thed_ck_licker.viewModels.PitViewModel

@Composable
fun PitScreen(
    modifier: Modifier,
    gameNavigator: GameNavigator,
    pitViewModel: PitViewModel
) {
    val pitCards by pitViewModel.pitCardSelection.collectAsState()

    if (pitCards.isEmpty()) {
        pitViewModel.getPitCards()
    }

    fun dropCardInPit(card: Int) {
        pitViewModel.dropCardInPit(card)
        gameNavigator.navigateBack()
    }

    fun buyShovel() {
        pitViewModel.buyShovel()
        gameNavigator.navigateBack()
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center)
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Pit",
                modifier = modifier
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Choose a card to drop to the pit",
                softWrap = true,
                modifier = modifier
                    .fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            CardRow(pitCards, { dropCardInPit(it) }, modifier)

            Spacer(modifier = Modifier.height(24.dp))

            Box(
                modifier = modifier
                    .width(100.dp)
                    .height(100.dp)
            ) {
                Image(
                    painter = painterResource(R.drawable.placeholder),
                    contentDescription = "The pit"
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Column {
                Text(text = "Pay 500 coins to buy a shovel, and close the pit")
                Button(
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.CenterHorizontally),
                    onClick = { buyShovel() }
                ) { Text("Buy shovel") }
            }
        }
    }
}
