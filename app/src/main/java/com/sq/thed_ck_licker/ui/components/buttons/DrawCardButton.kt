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

@Composable
fun DrawCard(
    cardsOnHand: MutableIntState,
    playerHealth: MutableFloatState,
    navigationBarPadding: PaddingValues,
    cardsOnDeck: ArrayDeque<CardClass>,
    modifier: Modifier,
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
                cardsOnHand.intValue++
                playerHealth.floatValue += 2f
                val newCard = cardsOnDeck.shuffled().take(1)[0]
                playerHealth.floatValue += newCard.cardEffect.cardEffectValue.value
                onUpdateState(CardClass(newCard.id, newCard.cardImage, newCard.cardEffect))
            }
        }) { Text("draw a card") }
    }
}