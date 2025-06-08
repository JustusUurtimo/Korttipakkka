package com.sq.thed_ck_licker.ecs.systems.viewSystems

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun MerchantHandView(
    modifier: Modifier,
    merchantHand: List<Int>,
    chooseMerchantCard: (Int) -> Unit = {},
    onReRollShop: () -> Unit,
    onOpenShop: () -> Unit
) {

    if (merchantHand.isEmpty()) {
        onOpenShop()
    }

    Box(
        modifier = modifier
            .height(250.dp)
            .background(Color.Magenta)
    ) {
        Column(modifier = modifier
            .padding(16.dp)
            .wrapContentSize(Alignment.Center)) {
            Row {
                for (card in merchantHand) {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(8.dp)
                    ) {
                        CardView(
                            card,
                            { chooseMerchantCard(card) },
                            modifier.height(150.dp)
                        )
                    }
                }
            }
            Button(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                onClick = onReRollShop
            ) { Text("Re-roll shop") }
        }
    }
}