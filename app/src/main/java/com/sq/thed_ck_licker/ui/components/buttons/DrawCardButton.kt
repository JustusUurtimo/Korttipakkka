package com.sq.thed_ck_licker.ui.components.buttons

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableFloatState
import androidx.compose.runtime.MutableIntState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sq.thed_ck_licker.card.CardClassification
import com.sq.thed_ck_licker.card.CardEffect
import com.sq.thed_ck_licker.card.CardEffectType
import com.sq.thed_ck_licker.card.CardIdentity
import com.sq.thed_ck_licker.card.Cards

@Composable
fun DrawCard(
    cards: Cards,
    cardsOnHand: MutableIntState,
    playerHealth: MutableFloatState,
    navigationBarPadding: PaddingValues,
    modifier: Modifier,
    latestCard: Pair<CardIdentity, CardEffect>,
    onUpdateState: (Pair<CardIdentity, CardEffect>) -> Unit
) {
    Column(
        modifier = modifier.padding(
            start = 16.dp,
            bottom = navigationBarPadding.calculateBottomPadding() // Add bottom padding for the navigation bar
        ),
    ) {
        // atm ottaa aina dmg 2% ja sit kortin arvon verran.
        //static 2% dmg idea, että tulevat kortit ei välttämättä ole dmg kortteja
        Button(onClick = {
            handleCardEffect(cards, latestCard, cardsOnHand, playerHealth, onUpdateState)
        }) { Text("draw a card") }
    }
}

fun handleCardEffect(
    cards: Cards,
    latestCard: Pair<CardIdentity, CardEffect>,
    cardsOnHand: MutableIntState,
    playerHealth: MutableFloatState,
    onUpdateState: (Pair<CardIdentity, CardEffect>) -> Unit
) {
    val newCard = cards.pullRandomCard()

    if (latestCard.first.id != -1) {
        val latestCardEffectCardClassification: CardClassification =
            latestCard.second.classification
        val latestCardEffectCardEffectType: CardEffectType =
            latestCard.second.effectType

        if (latestCardEffectCardClassification == CardClassification.MISC) {
            when (latestCardEffectCardEffectType) {
                CardEffectType.DOUBLE_TROUBLE -> {
                    cards.applyCardEffect(
                        newCard, playerHealth, reverseDamage = false,
                        doubleTrouble = true
                    )
                }

                CardEffectType.REVERSE_DAMAGE -> {
                    cards.applyCardEffect(
                        newCard, playerHealth, reverseDamage = true,
                        doubleTrouble = false
                    )
                }

                else -> {
                    println("its shop cupong i quess")
                }
            }
        } else {
            cards.applyCardEffect(
                newCard, playerHealth, reverseDamage = false,
                doubleTrouble = false
            )
        }
    }
    cardsOnHand.intValue++
    playerHealth.floatValue += 2f
    onUpdateState(newCard)
}