package com.sq.thed_ck_licker.ui.components.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.sq.thed_ck_licker.ecs.components.CardEffect
import com.sq.thed_ck_licker.ecs.components.CardIdentity

@Composable
fun CardsOnHand(
    cardsOnHand: MutableIntState,
    modifier: Modifier,
    latestCard: Pair<CardIdentity, CardEffect>
) {
    Box {
        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                BadgedBox(
                    badge = {
                        Badge(containerColor = Color.Red) {
                            Text("${cardsOnHand.intValue}")
                        }
                    }
                ) {
                    Card {
                        Column {
                            Image(
                                painter = painterResource(id = latestCard.first.cardImage),
                                contentDescription = "Card drawn",
                                modifier = modifier
                                    .width(120.dp)
                                    .wrapContentHeight()
                            )
                        }
                    }
                }
            }
        }
    }
}