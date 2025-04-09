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
fun HealthBar(health: Float, modifier: Modifier = Modifier) {
    println(health)
    println (health % 100)
    println ((health % 100) / 100f)
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            //TODO This kind a should be generic some how
            text = "Player Health",
            modifier = modifier
        )
        //we might want to look into some other way of doing the hp bar in the future :D
        //this way the bar starts off red and goes from left to right
        LinearProgressIndicator(
            progress = { if(health == 100f) 0f else (1 -(health % 100) / 100f)},
            modifier = modifier
                .fillMaxWidth()
                .height(15.dp),
            color =  Color.LightGray,
            trackColor =  Color.Red, //remaining health
        )

        // TODO: Remove the 1- when merged to the main branch
        LinearProgressIndicator(
            progress = { 1 - (health / 100f) },
            modifier = modifier
                .fillMaxWidth()
                .height(15.dp),
            color = Color.Red,
            trackColor = Color.LightGray, //remaining health
        )
    }
}

@Composable
fun ScoreDisplayer(score: Int, modifier: Modifier = Modifier) {
    Text("Score: $score steps taken")
}