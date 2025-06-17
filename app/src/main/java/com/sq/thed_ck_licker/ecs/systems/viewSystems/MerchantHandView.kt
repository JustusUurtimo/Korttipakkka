package com.sq.thed_ck_licker.ecs.systems.viewSystems

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
        Box(
            modifier = modifier
                .align(Alignment.Center)
        ) {
            Column(
                modifier = modifier
                    .padding(16.dp)
                    .wrapContentSize(Alignment.Center)
            )
            {
                CardRow(
                    cardSize = DpSize(120.dp, 170.dp),
                    zoomedCardId,
                    merchantHand,
                    { chooseMerchantCard(it) },
                    onZoomChange = { zoom ->
                        zoomedCardId.intValue = zoom
                    },
                    modifier
                )

            }

        }
        Button(
            modifier = modifier.align(Alignment.BottomCenter),
            onClick = onReRollShop
        ) { Text("Re-roll shop") }
    }
}