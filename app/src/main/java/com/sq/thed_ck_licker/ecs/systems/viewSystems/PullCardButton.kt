package com.sq.thed_ck_licker.ecs.systems.viewSystems

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PullCardButton(
    navigationBarPadding: PaddingValues,
    modifier: Modifier,
    pullNewCard: () -> Unit
) {
    Column(
        modifier = modifier.padding(
            start = 16.dp,
            bottom = navigationBarPadding.calculateBottomPadding() // Add bottom padding for the navigation bar
        ),
    ) {
        Button(
            onClick = pullNewCard
        ) { Text("draw a card") }
    }
}

