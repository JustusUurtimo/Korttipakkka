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
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sq.thed_ck_licker.card.CardClassification
import com.sq.thed_ck_licker.card.CardEffect
import com.sq.thed_ck_licker.card.CardEffectType
import com.sq.thed_ck_licker.card.CardEffectValue
import com.sq.thed_ck_licker.card.CardIdentity
import com.sq.thed_ck_licker.card.Cards
import com.sq.thed_ck_licker.player.HealthBar
import com.sq.thed_ck_licker.ui.components.buttons.DrawCard
import com.sq.thed_ck_licker.ui.components.views.CardDeck
import com.sq.thed_ck_licker.ui.components.views.CardsOnHand


@Composable
fun Game(innerPadding: PaddingValues) {
    val cards = Cards()
    // käytetään placeholderina kun ei ole vielä vedetty kortteja
    val defaultCardPair = Pair(
        CardIdentity(-1, R.drawable.card_back),
        CardEffect(CardClassification.GOOD, CardEffectType.HEAL, CardEffectValue.HEAL_2)
    )

    val navigationBarPadding = WindowInsets.navigationBars.asPaddingValues()
    var latestCard by rememberSaveable { mutableStateOf(defaultCardPair) }
    val cardsOnHand = rememberSaveable() { mutableIntStateOf(0) }
    val playerHealth = rememberSaveable() { mutableFloatStateOf(0f) }

    val modifier = Modifier

    Column(modifier.fillMaxWidth()) {
        // tällä toteutuksella hp menee takas täyteen ku se menee alle 0 :D
        // Se voinee miettiä kuntoon, sit ku saadaan game state käyntiin paremmin
        HealthBar(playerHealth.floatValue, modifier.padding(innerPadding))

        Box(modifier.fillMaxSize()) {
            CardDeck(navigationBarPadding)
            Box(modifier.align(Alignment.BottomCenter)) {
                Column(modifier.padding(5.dp)) {
                    CardsOnHand(cardsOnHand, modifier, latestCard)
                    DrawCard(
                        cards,
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



