package com.sq.thed_ck_licker.ecs.systems.viewSystems.navigationViews

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.sq.thed_ck_licker.Game
import com.sq.thed_ck_licker.ecs.systems.viewSystems.navigationViews.screens.HighScoresScreen
import com.sq.thed_ck_licker.ecs.systems.viewSystems.navigationViews.screens.MainMenuScreen
import com.sq.thed_ck_licker.ecs.systems.viewSystems.navigationViews.screens.SettingsScreen
import com.sq.thed_ck_licker.helpers.navigation.Screen

@Composable
fun GameNavigation(innerPadding: PaddingValues) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.MainMenu.route) {
        composable(Screen.MainMenu.route) {
            MainMenuScreen(
                onSettingsClick = { navController.navigate(Screen.Settings.route) },
                onGameClick = { navController.navigate(Screen.Game.route) },
                onHighScoresClick = { navController.navigate(Screen.HighScores.route) }
            )
        }
        composable(route = Screen.Settings.route, enterTransition = {
            slideInHorizontally { width -> width }
        },
            exitTransition = {
                slideOutHorizontally { width -> -width }
            }) { SettingsScreen() }
        composable(route = Screen.HighScores.route) { HighScoresScreen() }
        composable(route = Screen.Game.route) { Game(innerPadding) }
    }
}




