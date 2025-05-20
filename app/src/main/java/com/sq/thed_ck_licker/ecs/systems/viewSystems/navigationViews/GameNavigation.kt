package com.sq.thed_ck_licker.ecs.systems.viewSystems.navigationViews

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.sq.thed_ck_licker.Game
import com.sq.thed_ck_licker.ecs.systems.viewSystems.DeathScreen
import com.sq.thed_ck_licker.ecs.systems.viewSystems.navigationViews.screens.HighScoresScreen
import com.sq.thed_ck_licker.ecs.systems.viewSystems.navigationViews.screens.MainMenuScreen
import com.sq.thed_ck_licker.ecs.systems.viewSystems.navigationViews.screens.SettingsScreen
import com.sq.thed_ck_licker.helpers.navigation.Screen
import com.sq.thed_ck_licker.viewModels.GameViewModel

@Composable
fun GameNavigation(innerPadding: PaddingValues, gameViewModel: GameViewModel = hiltViewModel()) {
    val navController = rememberNavController()
    val modifier = Modifier
    NavHost(navController = navController, startDestination = Screen.MainMenu.route) {
        composable(Screen.MainMenu.route) {
            MainMenuScreen(
                onSettingsClick = { navController.navigate(Screen.Settings.route) },
                onGameClick = { navController.navigate(Screen.Game.route) },
                onHighScoresClick = { navController.navigate(Screen.HighScores.route) }
            )
        }
        composable(route = Screen.Settings.route,
            enterTransition = {
                slideInHorizontally { width -> width }
            },
            exitTransition = {
                slideOutHorizontally { width -> -width }
            }) { SettingsScreen(modifier.padding(innerPadding)) }
        composable(route = Screen.HighScores.route,
            enterTransition = {
                slideInHorizontally { width -> width }
            },
            exitTransition = {
                slideOutHorizontally { width -> -width }
            }) { HighScoresScreen(modifier.padding(innerPadding)) }
        composable(route = Screen.Game.route,
            enterTransition = {
                scaleIn(initialScale = 0.8f) + fadeIn(initialAlpha = 0.3f)
            },
            exitTransition = {
                scaleOut(targetScale = 1.2f) + fadeOut()
            }) { Game(modifier = modifier, innerPadding) }

        composable(route = Screen.DeathScreen.route,
            enterTransition = {
                slideInHorizontally { width -> width }
            },
            exitTransition = {
                slideOutHorizontally { width -> -width }
            }) {DeathScreen(onRetry = {gameViewModel.restartGame()})}
    }
}




