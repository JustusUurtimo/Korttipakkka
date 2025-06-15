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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp

@Composable
fun MerchantHandView(
    modifier: Modifier,
    merchantHand: List<Int>,
    chooseMerchantCard: (Int) -> Unit = {},
    onReRollShop: () -> Unit,
    onOpenShop: () -> Unit
) {

    var isZoomed by remember { mutableStateOf(false) }

    if (merchantHand.isEmpty()) {
        onOpenShop()
    }

    Box(
        modifier = modifier
            .height(250.dp)
            .background(Color.Magenta)
    ) {
        Column(
            modifier = modifier
                .padding(16.dp)
                .wrapContentSize(Alignment.Center)
        )
        {
            CardRow(cardSize = DpSize(50.dp, 100.dp), isZoomed, merchantHand, { chooseMerchantCard(it) }, { isZoomed = it }, modifier)
            Button(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                onClick = onReRollShop
            ) { Text("Re-roll shop") }
        }
    }
}