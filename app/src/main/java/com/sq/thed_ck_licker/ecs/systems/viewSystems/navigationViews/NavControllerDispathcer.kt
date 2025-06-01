package com.sq.thed_ck_licker.ecs.systems.viewSystems.navigationViews

import androidx.navigation.NavController
import javax.inject.Inject

class NavControllerDispatcher @Inject constructor(
    private val navController: NavController
) : NavigationDispatcher {
    override fun navigateTo(route: String) {
        navController.navigate(route)
    }
}