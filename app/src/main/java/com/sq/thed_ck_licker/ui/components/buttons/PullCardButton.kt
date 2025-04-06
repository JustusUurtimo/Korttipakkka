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
import com.sq.thed_ck_licker.ecs.TheGameHandler.cards
import com.sq.thed_ck_licker.ecs.components.CardClassification
import com.sq.thed_ck_licker.ecs.components.CardEffect
import com.sq.thed_ck_licker.ecs.components.CardEffectType
import com.sq.thed_ck_licker.ecs.components.CardIdentity

@Composable
fun PullCardButton(
    cardsOnHand: MutableIntState,
    playerHealth: MutableFloatState,
    navigationBarPadding: PaddingValues,
    modifier: Modifier,
    latestCard: Pair<CardIdentity, CardEffect>,
    onUpdateState: (Pair<CardIdentity, CardEffect>) -> Unit,
    pullNewCard: () -> Unit
) {
    Column(
        modifier = modifier.padding(
            start = 16.dp,
            bottom = navigationBarPadding.calculateBottomPadding() // Add bottom padding for the navigation bar
        ),
    ) {
        // atm ottaa aina dmg 2% ja sit kortin arvon verran.
        //static 2% dmg idea, että tulevat kortit ei välttämättä ole dmg kortteja
        Button(onClick = pullNewCard
//            {
////            handleCardEffect(latestCard, cardsOnHand, playerHealth, onUpdateState)
//        }
        ) { Text("draw a card") }
    }
}

