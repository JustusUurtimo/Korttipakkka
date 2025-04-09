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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sq.thed_ck_licker.ecs.EntityManager.getPlayerID
import com.sq.thed_ck_licker.ecs.TheGameHandler.playerSystem
import com.sq.thed_ck_licker.ecs.components.ActivationCounterComponent
import com.sq.thed_ck_licker.ecs.components.EffectComponent
import com.sq.thed_ck_licker.ecs.components.deactivate
import com.sq.thed_ck_licker.ecs.get
import com.sq.thed_ck_licker.ecs.systems.CardDisplaySystem.Companion.cardDisplaySystem
import com.sq.thed_ck_licker.ecs.systems.CardsSystem.Companion.cardsSystem
import com.sq.thed_ck_licker.ecs.systems.onDeathSystem
import com.sq.thed_ck_licker.ecs.systems.onDiscardSystem
import com.sq.thed_ck_licker.ecs.systems.onTurnStartEffectStackSystem
import com.sq.thed_ck_licker.player.HealthBar
import com.sq.thed_ck_licker.player.ScoreDisplayer
import com.sq.thed_ck_licker.ui.components.buttons.PullCardButton
import com.sq.thed_ck_licker.ui.components.views.CardDeck
import com.sq.thed_ck_licker.ecs.EntityManager.getPlayerID as playerId


@Composable
fun Game(innerPadding: PaddingValues) {

    val navigationBarPadding = WindowInsets.navigationBars.asPaddingValues()
    val playerCardCount = rememberSaveable { mutableIntStateOf(0) }
    val latestCard = rememberSaveable { mutableIntStateOf(-1) }
    val playerHealth =
        rememberSaveable { playerSystem.getPlayerHealthM() }
    val playerMaxHealth =
        rememberSaveable { playerSystem.getPlayerMaxHealthM() }
    val playerScore = rememberSaveable { playerSystem.getPlayerScoreM() }
// TODO we should read more about viewModels and state holding

    val modifier = Modifier

    val activateCard = {
        // TODO: I now think all these should be such as the one bellow, just nice clean function calls
        onTurnStartEffectStackSystem()
        cardsSystem.activateCard(latestCard, playerCardCount)
        onDeathSystem()
    }

    val pullNewCard = {
        try {
            (latestCard.intValue get EffectComponent::class).onDeactivate.invoke(getPlayerID())
        } catch (_: Exception) {
            println("Yeah yeah, we get it, you are so cool there was no effect component ")
        }

        try {
            (latestCard.intValue get ActivationCounterComponent::class).deactivate()
        } catch (_: Exception) {
            println("Yeah yeah, we get it, you are so cool there was no actCounter component ")
        }
        onDiscardSystem()
        cardsSystem.pullRandomCardFromEntityDeck(playerId(), latestCard)
    }


    Column(modifier.fillMaxWidth()) {

        // tällä toteutuksella hp menee takas täyteen ku se menee alle 0 :D
        // Se voinee miettiä kuntoon, sit ku saadaan game state käyntiin paremmin
        HealthBar(playerHealth, playerMaxHealth, modifier.padding(innerPadding))

        ScoreDisplayer(playerScore.intValue)

        Box(modifier.fillMaxSize()) {
            CardDeck(navigationBarPadding, latestCard)
            Box(modifier.align(Alignment.BottomCenter)) {

                Column(modifier.padding(35.dp, 0.dp, 0.dp, 0.dp)) {
                    if (latestCard.intValue != -1) {
                        cardDisplaySystem.CardsOnHandView(
                            playerCardCount,
                            modifier,
                            latestCard,
                            activateCard
                        )
                    }
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