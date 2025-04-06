package com.sq.thed_ck_licker.ui.components.buttons

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
        // atm ottaa aina dmg 2% ja sit kortin arvon verran.
        //static 2% dmg idea, että tulevat kortit ei välttämättä ole dmg kortteja
        Button(
            onClick = pullNewCard
        ) { Text("draw a card") }
    }
}

