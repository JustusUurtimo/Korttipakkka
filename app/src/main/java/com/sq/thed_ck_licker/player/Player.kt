package com.sq.thed_ck_licker.player

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun HealthBar(playerHealth: Float, modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "Player Health",
            modifier = modifier
        )
        LinearProgressIndicator(
            progress = { (playerHealth % 100) / 100f },
            modifier = Modifier
                .fillMaxWidth()
                .height(15.dp),
            color = Color.LightGray,
            trackColor = Color.Red, //remaining health
        )
    }
}