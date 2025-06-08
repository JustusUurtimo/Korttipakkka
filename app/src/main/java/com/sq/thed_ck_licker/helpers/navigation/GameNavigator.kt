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
        navController.navigate(route)
    }
}