package com.sq.thed_ck_licker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.sq.thed_ck_licker.ui.theme.TheD_ck_LickerTheme
import com.sq.thed_ck_licker.ecs.systems.DescriptionSystem.Companion.instance as descriptionSystem
import com.sq.thed_ck_licker.ecs.systems.characterSystems.MerchantSystem.Companion.instance as merchantSystem
import com.sq.thed_ck_licker.ecs.systems.characterSystems.PlayerSystem.Companion.instance as playerSystem

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        playerSystem.initPlayer()
        merchantSystem.initRegularMerchant()
        descriptionSystem.updateAllDescriptions()

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TheD_ck_LickerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Game(innerPadding)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TheD_ck_LickerTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            Game(innerPadding)
        }
    }
}