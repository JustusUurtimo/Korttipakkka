package com.sq.thed_ck_licker

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sq.thed_ck_licker.ecs.TheGameHandler
import com.sq.thed_ck_licker.ecs.TheGameHandler.getDefaultCardPair
import com.sq.thed_ck_licker.player.HealthBar
import com.sq.thed_ck_licker.ui.components.buttons.DrawCard
import com.sq.thed_ck_licker.ui.components.views.CardDeck
import com.sq.thed_ck_licker.ui.components.views.CardsOnHand2


@Composable
fun Game(innerPadding: PaddingValues) {

    TheGameHandler.initTheGame()
    val navigationBarPadding = WindowInsets.navigationBars.asPaddingValues()
    var latestCard by rememberSaveable { mutableStateOf(getDefaultCardPair()) }
    val cardsOnHand = rememberSaveable { mutableIntStateOf(0) }
    val playerHealth =
        rememberSaveable { TheGameHandler.getPlayerHealthM() }
    // TODO here probably should be viewModel from about the game state data or something like that
    //  Something about state holder and all that

    val modifier = Modifier

    Column(modifier.fillMaxWidth()) {
        // tällä toteutuksella hp menee takas täyteen ku se menee alle 0 :D
        // Se voinee miettiä kuntoon, sit ku saadaan game state käyntiin paremmin
        HealthBar(playerHealth.floatValue, modifier.padding(innerPadding))

        Box(modifier.fillMaxSize()) {
            CardDeck(navigationBarPadding)
            Box(modifier.align(Alignment.BottomCenter)) {
                Column(modifier.padding(5.dp)) {
                    CardsOnHand2(cardsOnHand, modifier, latestCard)
                    DrawCard(
                        cardsOnHand,
                        playerHealth,
                        navigationBarPadding,
                        modifier,
                        latestCard,
                        onUpdateState = { newCard ->
                            latestCard = newCard
                        }
                    )
                }

            }
        }

    }
}




