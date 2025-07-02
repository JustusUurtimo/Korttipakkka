package com.sq.thed_ck_licker.ecs.systems.viewSystems.navigationViews.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.sq.thed_ck_licker.helpers.Settings

@Composable
fun SettingsScreen(modifier: Modifier) {
    Column (modifier = modifier) {
        Text("Settings")
        Text("Not implemented yet")
        SettingCheckbox(
            Settings.isRealTimePlayerDamageEnabled,
            "Real time things",
            "Now you must hurry mode",
            "Relaxed mode"
        )
        SettingCheckbox(
            Settings.addBaseTestPackage,
            "Testing Cards",
            "Now you have all kind a test cards",
            "No test cards for you?"
        )
        SettingCheckbox(
            Settings.addForestPackage,
            "Forest package",
            "Now you have forest cards",
            "No forest for you :("
        )
    }

}


@Composable
fun SettingCheckbox(
    areRealTimeThingsEnabled: MutableState<Boolean>, name: String, active: String, inactive: String
) {
    var checked2 by remember { areRealTimeThingsEnabled }

    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(name)
        Checkbox(
            checked = checked2,
            onCheckedChange = { checked2 = it }
        )
    }
    Text(if (checked2) active else inactive)
}

