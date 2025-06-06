package com.sq.thed_ck_licker.helpers.navigation

import androidx.navigation.NavController
import javax.inject.Inject

class GameNavigator @Inject constructor() {
    private var navController: NavController? = null

    fun setupWithNavController(navController: NavController) {
        this.navController = navController
    }

    fun navigateTo(route: String) {
        println("NAVIGATING TO $route")
        println("NavController: $navController")
        navController?.navigate(route)
    }
}