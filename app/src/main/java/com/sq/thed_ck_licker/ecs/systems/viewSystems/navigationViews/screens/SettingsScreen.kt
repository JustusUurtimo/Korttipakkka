package com.sq.thed_ck_licker.ecs.systems.viewSystems.navigationViews.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun SettingsScreen(modifier: Modifier) {
    Column (modifier = modifier) {
        Text("Settings")
        Text("Not implemented yet")
        HurryCheckbox()
    }

}

val isHurryModeEnabled = mutableStateOf(true)

@Composable
fun HurryCheckbox() {
    var checked2 by remember { isHurryModeEnabled }

    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            "Hurryness"
        )
        Checkbox(
            checked = checked2,
            onCheckedChange = { checked2 = it }
        )
    }

    Text(
        if (checked2) "Now you must hurry mode" else "Relaxed mode"
    )
}

