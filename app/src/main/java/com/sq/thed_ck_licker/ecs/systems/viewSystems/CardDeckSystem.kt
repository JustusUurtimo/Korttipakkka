package com.sq.thed_ck_licker.ecs.systems.viewSystems

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.sq.thed_ck_licker.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardDeck(
    navigationBarPadding: PaddingValues,
    playerCardCount: Int,
    pullNewCard: () -> Unit
) {

    Box(
        modifier = Modifier
            .padding(0.dp, 0.dp, 35.dp, 0.dp)
            .fillMaxSize()
    ) {

        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(
                    start = 16.dp,
                    bottom = navigationBarPadding.calculateBottomPadding() // Add bottom padding for the navigation bar
                )
                .clickable { pullNewCard() }
        ) {
            BadgedBox(
                badge = {
                    Badge(
                        modifier = Modifier.offset((-20).dp, (5).dp),
                        containerColor = Color.Red
                    ) {
                        Text("$playerCardCount")
                    }
                }
            ) {
                TooltipBox(
                    tooltip = {
                        Surface(
                            shape = MaterialTheme.shapes.medium,
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                        ) {
                            Text(
                                text = " Cards in the deck ",
                            )
                        }
                    },
                    state = rememberTooltipState(),
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .size(100.dp),
                    positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider()
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.card_back),
                        contentDescription = "Card back",
                        modifier = Modifier
                            .fillMaxSize()
                    )
                }
            }
        }
    }
}