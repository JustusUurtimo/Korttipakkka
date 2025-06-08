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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.sq.thed_ck_licker.ecs.systems.viewSystems.DeathScreen
import com.sq.thed_ck_licker.ecs.systems.viewSystems.navigationViews.screens.Game
import com.sq.thed_ck_licker.ecs.systems.viewSystems.navigationViews.screens.HighScoresScreen
import com.sq.thed_ck_licker.ecs.systems.viewSystems.navigationViews.screens.MainMenuScreen
import com.sq.thed_ck_licker.ecs.systems.viewSystems.navigationViews.screens.MerchantScreen
import com.sq.thed_ck_licker.ecs.systems.viewSystems.navigationViews.screens.SettingsScreen
import com.sq.thed_ck_licker.helpers.navigation.GameNavigator
import com.sq.thed_ck_licker.helpers.navigation.Screen
import com.sq.thed_ck_licker.viewModels.GameViewModel
import com.sq.thed_ck_licker.viewModels.MerchantViewModel

@Composable
fun GameNavigation(
    innerPadding: PaddingValues,
    gameNavigator: GameNavigator,
    gameViewModel: GameViewModel = hiltViewModel(),
) {
    val navController = rememberNavController()
    val modifier = Modifier

    LaunchedEffect(Unit) {
        gameNavigator.setNavController(navController)
    }

    NavHost(navController = navController, startDestination = Screen.MainMenu.route) {

        composable(route = Screen.MainMenu.route) {
            MainMenuScreen(
                onSettingsClick = { gameNavigator.navigateTo(Screen.Settings.route) },
                onGameClick = { gameNavigator.navigateTo(Screen.Game.route) },
                onHighScoresClick = { gameNavigator.navigateTo(Screen.HighScores.route) }
            )
        }

        composable(route = Screen.Settings.route,
            enterTransition = {
                slideInHorizontally { width -> width }
            },
            exitTransition = {
                slideOutHorizontally { width -> -width }
            })
        { SettingsScreen(modifier.padding(innerPadding)) }

        composable(route = Screen.HighScores.route,
            enterTransition = {
                slideInHorizontally { width -> width }
            },
            exitTransition = {
                slideOutHorizontally { width -> -width }
            })
        { HighScoresScreen(modifier.padding(innerPadding)) }

        composable(route = Screen.Game.route,
            enterTransition = {
                scaleIn(initialScale = 0.8f) + fadeIn(initialAlpha = 0.3f)
            },
            exitTransition = {
                scaleOut(targetScale = 1.2f) + fadeOut()
            })
        { Game(modifier = modifier, innerPadding) }

        composable(route = Screen.DeathScreen.route,
            enterTransition = {
                slideInHorizontally { width -> width }
            },
            exitTransition = {
                slideOutHorizontally { width -> -width }
            })
        { DeathScreen(onRetry = { gameViewModel.restartGame() }) }

        composable(route = Screen.MerchantShop.route)
        {
            val viewModel: MerchantViewModel = hiltViewModel()
            MerchantScreen(modifier = modifier, viewModel, gameNavigator)
        }


    }
}




