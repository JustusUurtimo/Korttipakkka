package com.sq.thed_ck_licker

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sq.thed_ck_licker.ecs.ComponentManager
import com.sq.thed_ck_licker.ecs.TheGameHandler
import com.sq.thed_ck_licker.ecs.systems.CardDisplaySystem
import com.sq.thed_ck_licker.ecs.systems.CardEffectSystem
import com.sq.thed_ck_licker.helpers.getRandomElement
import com.sq.thed_ck_licker.player.HealthBar
import com.sq.thed_ck_licker.player.ScoreDisplayer
import com.sq.thed_ck_licker.ui.components.buttons.PullCardButton
import com.sq.thed_ck_licker.ui.components.views.CardDeck

const val NO_CARD = -1

@Composable
fun Game(innerPadding: PaddingValues) {

    val componentManager = ComponentManager.componentManager

    val navigationBarPadding = WindowInsets.navigationBars.asPaddingValues()
    val playerCardCount = rememberSaveable { mutableIntStateOf(0) }
    var latestCard by rememberSaveable { mutableIntStateOf(NO_CARD) }
    val cardsOnHand = rememberSaveable { mutableIntStateOf(0) }
    val playerHealth =
        rememberSaveable { TheGameHandler.getPlayerHealthM() }
    val playerScore = rememberSaveable { TheGameHandler.getPlayerScoreM() }
    // TODO here probably should be viewModel from about the game state data or something like that
    //  Something about state holder and all that
    val cardDisplaySystem = CardDisplaySystem(componentManager)
    val cardEffectSystem = CardEffectSystem(componentManager)


    val modifier = Modifier

    // TODO: this should contain no card or something like that in the beginning
    var cardde by remember { mutableIntStateOf(TheGameHandler.getRandomCard()!!.keys.getRandomElement()) }

    val activateCard = {
        cardEffectSystem.playerTargetsPlayer(cardde)
        playerCardCount.intValue += 1
        // TODO: Add here things needed to discard the card
    }
    val pullNewCard = {
        cardde = TheGameHandler.getRandomCard()!!.keys.getRandomElement()
        // TODO: Add here things needed to pull new card from the deck and not just card
    }

    Column(modifier.fillMaxWidth()) {

        // tällä toteutuksella hp menee takas täyteen ku se menee alle 0 :D
        // Se voinee miettiä kuntoon, sit ku saadaan game state käyntiin paremmin
        HealthBar(playerHealth.floatValue, modifier.padding(innerPadding))

        ScoreDisplayer(playerScore.intValue)

        Box(modifier.fillMaxSize()) {
            CardDeck(navigationBarPadding, pullNewCard)
            Box(modifier.align(Alignment.BottomCenter)) {
                Column(modifier.padding(35.dp, 0.dp, 0.dp, 0.dp)) {
                    cardDisplaySystem.CardsOnHandView(
                        playerCardCount,
                        modifier,
                        cardde,
                        activateCard
                    )
                    PullCardButton(
                        navigationBarPadding,
                        modifier.offset((-15).dp),
                        pullNewCard
                    )
                }

            }
        }

    }
}




