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
import com.sq.thed_ck_licker.ecs.systems.WorldCreationSystem
import com.sq.thed_ck_licker.ecs.systems.viewSystems.navigationViews.GameNavigation
import com.sq.thed_ck_licker.ecs.systems.viewSystems.navigationViews.screens.Game
import com.sq.thed_ck_licker.helpers.MyRandom
import com.sq.thed_ck_licker.helpers.navigation.GameNavigator
import com.sq.thed_ck_licker.ui.theme.TheD_ck_LickerTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.random.Random

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject lateinit var worldCreationSystem: WorldCreationSystem
    @Inject lateinit var gameNavigator: GameNavigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        worldCreationSystem.destroyWorld()
        enableEdgeToEdge()
        setContent {
            TheD_ck_LickerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    GameNavigation(innerPadding, gameNavigator)
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
            Game(modifier = Modifier, innerPadding)
        }
    }
}