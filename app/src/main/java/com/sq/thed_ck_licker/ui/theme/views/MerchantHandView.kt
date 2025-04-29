package com.sq.thed_ck_licker.ui.theme.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
) {

    Box(
        modifier = modifier
            .height(225.dp)
            .background(
                color = Color.Magenta
            ),
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .height(170.dp)
                .background(Color.Magenta)
        ) {
            for (card in merchantHand) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(8.dp)
                ) {
                    CardView(
                        card,
                        { chooseMerchantCard(card) },
                        Modifier.fillMaxSize()
                    )
                }
            }

        }
        Button(
            modifier = modifier
                .align(Alignment.BottomCenter),
            onClick = onReRollShop
        ) { Text("Re-roll shop") }
    }
}