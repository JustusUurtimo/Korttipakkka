package com.sq.thed_ck_licker.ecs.systems.viewSystems.navigationViews.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sq.thed_ck_licker.helpers.Settings
import com.sq.thed_ck_licker.viewModels.SettingsViewModel

@Composable
fun SettingsScreen(modifier: Modifier, viewModel: SettingsViewModel = hiltViewModel()) {

    val realTimePlayerDamageEnabled by viewModel.realTimePlayerDamageEnabled.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Settings", style = MaterialTheme.typography.headlineSmall)

        Spacer(modifier = modifier.height(24.dp))

        SettingCheckbox(
            realTimePlayerDamageEnabled,
            onChanged = { viewModel.toggleRealTimePlayerDamageEnabled() },
            "Real time things",
            "Now you must hurry mode",
            "Relaxed mode"
        )



        SettingCheckbox(
            Settings.addBaseTestPackage.value,
            onChanged = { Settings.addBaseTestPackage.value = it },
            "Testing Cards",
            "Now you have all kind a test cards",
            "No test cards for you?"
        )

        Spacer(modifier = modifier.height(24.dp))

        SettingCheckbox(
            Settings.addForestPackage.value,
            onChanged = { Settings.addForestPackage.value = it },
            "Forest package",
            "Now you have forest cards",
            "No forest for you :("
        )
    }

}


@Composable
fun SettingCheckbox(
    value: Boolean, onChanged: (Boolean) -> Unit, name: String, active: String, inactive: String
) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(name)
        Checkbox(
            checked = value,
            onCheckedChange = onChanged
        )
    }
    Text(if (value) active else inactive)
}

