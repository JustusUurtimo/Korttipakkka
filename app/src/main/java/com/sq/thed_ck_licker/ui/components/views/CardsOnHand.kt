package com.sq.thed_ck_licker.ui.components.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.sq.thed_ck_licker.ecs.TheGameHandler
import com.sq.thed_ck_licker.ecs.systems.CardDisplaySystem


@Composable
fun CardsOnHandView(
    cardsDrawCount: MutableIntState,
    modifier: Modifier,
    cardId: Int,
    cardDisplaySystem: CardDisplaySystem
) {

    BadgedBox(
        badge = {
            Badge(containerColor = Color.Red) {
                Text("${cardsDrawCount.intValue}")
            }
        },
        modifier = modifier
            .width(120.dp)
            .height(170.dp)
            .background(color = Color.Magenta)
            .offset(20.dp, 0.dp)


    ) {
        // TODO: There might be some modifier that "just rounds the corners"
        Card(
            modifier = modifier.background(color = Color.Green)
        ) {
            cardDisplaySystem.EntityDisplay(cardId)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DisplayACardPreview(
    @PreviewParameter(
        CardEntityPreviewParameterProvider::class,
        limit = 1
    ) cardEntity: Int
) {
    val cardCounter = remember { mutableIntStateOf(69) }
    CardsOnHandView(
        cardCounter,
        Modifier,
        cardEntity,
        CardDisplaySystem(TheGameHandler.getTheComponents())
    )
}


// TODO: maybe this should be part of test things?
class CardEntityPreviewParameterProvider : PreviewParameterProvider<Int> {
    // TODO here should be some way of actually getting card entities
    init {
        TheGameHandler.initTheGame()
    }

    override val values = TheGameHandler.getTestingCardSequence()
}
