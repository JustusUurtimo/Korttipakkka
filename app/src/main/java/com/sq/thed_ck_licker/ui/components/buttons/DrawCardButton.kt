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
import com.sq.thed_ck_licker.card.CardClass
import com.sq.thed_ck_licker.card.CardClassification
import com.sq.thed_ck_licker.card.CardEffectType

@Composable
fun DrawCard(
    cardsOnHand: MutableIntState,
    playerHealth: MutableFloatState,
    navigationBarPadding: PaddingValues,
    cardsOnDeck: ArrayDeque<CardClass>,
    modifier: Modifier,
    latestCard: CardClass,
    onUpdateState: (CardClass) -> Unit
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
            if (cardsOnDeck.isNotEmpty()) {
                handleCardEffect(latestCard, cardsOnHand, cardsOnDeck, playerHealth, onUpdateState)
            }
        }) { Text("draw a card") }
    }
}

fun handleCardEffect(
    latestCard: CardClass,
    cardsOnHand: MutableIntState,
    cardsOnDeck: ArrayDeque<CardClass>,
    playerHealth: MutableFloatState,
    onUpdateState: (CardClass) -> Unit
) {
    val latestCardEffectCardClassification: CardClassification = latestCard.effect.CardClassification
    val latestCardEffectCardEffectType: CardEffectType = latestCard.effect.CardEffectType
    val newCard = cardsOnDeck.shuffled().take(1)[0]
    val newCardEffectValue = newCard.effect.CardEffectValue.value

    cardsOnHand.intValue++
    playerHealth.floatValue += 2f

    when(latestCardEffectCardClassification) {
        CardClassification.MISC -> {
            when(latestCardEffectCardEffectType) {
                CardEffectType.DOUBLE_TROUBLE -> {
                    playerHealth.floatValue += (newCardEffectValue * 2)
                }
                CardEffectType.REVERSE_DAMAGE -> {
                    if(newCardEffectValue > 0) {
                        playerHealth.floatValue -= (newCardEffectValue)
                    }
                }
                CardEffectType.SHOP_COUPON -> {
                    print("shop not implemented :D")
                }
                else -> {
                    throw IllegalArgumentException("The card effectType was not on MISC: " + latestCardEffectCardEffectType.name)
                }
            }
        }
        else -> {}
    }
    when(newCard.effect.CardEffectType) {
        CardEffectType.HP_REGEN -> print("todo")
        CardEffectType.HEAL -> playerHealth.floatValue += newCardEffectValue
        CardEffectType.DAMAGE -> playerHealth.floatValue += newCardEffectValue
        CardEffectType.MAX_HP -> print("todo")
        CardEffectType.DOUBLE_TROUBLE -> {}
        CardEffectType.REVERSE_DAMAGE -> {}
        CardEffectType.SHOP_COUPON -> print("todo")
    }
    playerHealth.floatValue += (newCardEffectValue)
    onUpdateState(CardClass(newCard.id, newCard.cardImage, newCard.effect))
}