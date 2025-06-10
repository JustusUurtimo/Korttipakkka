package com.sq.thed_ck_licker.helpers.navigation

import androidx.navigation.NavController
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GameNavigator @Inject constructor() {
    private var _navController: NavController? = null

    private val navController: NavController
        get() = _navController ?: throw IllegalStateException("NavController not set")

    fun setNavController(controller: NavController) {
        if (_navController == null) {
            _navController = controller
        }
    }

    fun navigateTo(route: String) {
        navController.navigate(route) {
            launchSingleTop = true
        }
    }

    fun navigateBack() {
        println("backstack: ${navController.currentBackStackEntry}")
        println("Navigating back ${navController.currentBackStackEntry?.destination?.route}")
        navController.navigateUp()
    }

    fun leaveGame() {

        navController.navigate(Screen.MainMenu.route) {
            popUpTo(0) { inclusive = true }
        }
    }

    fun restartGame() {
        navController.navigate(Screen.Game.route) {
            popUpTo(0) { inclusive = true }
        }
    }
}