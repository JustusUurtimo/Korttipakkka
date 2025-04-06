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
import com.sq.thed_ck_licker.ecs.TheGameHandler.cardsSystem
import com.sq.thed_ck_licker.ecs.components.CardClassification
import com.sq.thed_ck_licker.ecs.components.CardEffectType
import com.sq.thed_ck_licker.ecs.EntityManager.getPlayerID as playerId

@Composable
fun DrawCard(
    cardsOnHand: MutableIntState,
    playerHealth: MutableFloatState,
    navigationBarPadding: PaddingValues,
    modifier: Modifier,
    latestCard: Int,
    onUpdateState: (Int) -> Unit
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
            handleCardEffect(cardsOnHand, playerHealth, latestCard, onUpdateState)
        }) { Text("draw a card") }
    }
}

private fun handleCardEffect(
    cardsOnHand: MutableIntState,
    playerHealth: MutableFloatState,
    latestCardID: Int,
    onUpdateState: (Int) -> Unit
) {
    val newCard = cardsSystem.pullRandomCardFromEntityDeck(playerId())
    val latestCard = cardsSystem.getCardComponentByEntity(latestCardID)

    val latestCardEffectCardClassification: CardClassification =
        latestCard.second.classification
    val latestCardEffectCardEffectType: CardEffectType =
        latestCard.second.effectType

    if (latestCardEffectCardClassification == CardClassification.MISC) {
        when (latestCardEffectCardEffectType) {
            CardEffectType.DOUBLE_TROUBLE -> {
                cardsSystem.applyCardEffect(
                    newCard, playerHealth, reverseDamage = false,
                    doubleTrouble = true
                )
            }

            CardEffectType.REVERSE_DAMAGE -> {
                cardsSystem.applyCardEffect(
                    newCard, playerHealth, reverseDamage = true,
                    doubleTrouble = false
                )
            }

            else -> {
                println("its shop cupong i quess")
            }
        }
    } else {
        cardsSystem.applyCardEffect(
            newCard, playerHealth, reverseDamage = false,
            doubleTrouble = false
        )
    }

    cardsOnHand.intValue++
    playerHealth.floatValue += 2f
    onUpdateState(newCard.third)
}