package com.sq.thed_ck_licker.ecs.systems.viewSystems

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
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
    val zoomedCardId = rememberSaveable { mutableIntStateOf(-1) }
    val isZoomed = zoomedCardId.intValue != -1

    if (merchantHand.isEmpty()) {
        onOpenShop()
    }
    Box(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(isZoomed) {
                if (isZoomed) {
                    detectTapGestures { zoomedCardId.intValue = -1 }
                }
            }
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(16.dp)
        ) {
            Text(
                text = "Merchant Shop",
                modifier = modifier
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Use points to buy a card",
                softWrap = true,
                modifier = modifier
                    .fillMaxWidth()
            )
        }

        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(24.dp))

            CardRow(
                cardSize = DpSize(100.dp, 150.dp),
                zoomedCardId,
                merchantHand,
                { chooseMerchantCard(it) },
                onZoomChange = { zoom ->
                    zoomedCardId.intValue = zoom
                },
                modifier
            )
            Spacer(modifier = Modifier.height(48.dp))
            Button(
                modifier = modifier.align(Alignment.CenterHorizontally),
                onClick = onReRollShop
            ) { Text("Re-roll shop") }
        }

    }
}
