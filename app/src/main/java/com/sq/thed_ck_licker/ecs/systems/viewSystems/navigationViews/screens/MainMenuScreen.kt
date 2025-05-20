package com.sq.thed_ck_licker.ecs.systems.viewSystems.navigationViews.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun MainMenuScreen(
    modifier: Modifier = Modifier,
    onSettingsClick: () -> Unit,
    onGameClick: () -> Unit,
    onHighScoresClick: () -> Unit
) {

    Box(
        modifier = modifier
            .fillMaxSize() // Ensures the menu takes the whole screen
            .background(MaterialTheme.colorScheme.background), // Optional background
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Title
            Text("Korttipakka", style = MaterialTheme.typography.displayLarge)

            Spacer(Modifier.height(64.dp))

            // Buttons
            MenuButton("Start Game", onClick = onGameClick)
            MenuButton("Settings", onClick = onSettingsClick)
            MenuButton("High Scores", onClick = onHighScoresClick)
        }
    }

}

@Composable
fun MenuButton(text: String, onClick: () -> Unit) {
    //this prevents many quick presses from launching many game instances
    var isClicked by remember { mutableStateOf(false) }
    Button(
        onClick = {
            if (!isClicked) {
                isClicked = true
                onClick.invoke()
            }
        },
        enabled = !isClicked,
        modifier = Modifier
            .width(200.dp)
            .padding(8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.LightGray
        )
    ) {
        Text(text)
    }
}