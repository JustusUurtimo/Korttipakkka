package com.sq.thed_ck_licker.helpers.navigation

import android.util.Log
import android.annotation.SuppressLint
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
        navController.navigateUp()
    }

    //for navigation debugging
    @SuppressLint("RestrictedApi")
    private fun printBackstack(navController: NavController) {
        Log.i("BackStackDebug","==== BACKSTACK ====")
        navController.currentBackStack.value.forEach { entry ->
            Log.i("BackStackDebug","Route: ${entry.destination.route}")
            Log.i("BackStackDebug","Arguments: ${entry.arguments}")
            Log.i("BackStackDebug","‐-------------------------")
        }
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
