package com.sq.thed_ck_licker.helpers.navigation

sealed class Screen(val route: String) {
    object MainMenu : Screen("main_menu")
    object Settings : Screen("settings")
    object Game : Screen("game")
    object HighScores : Screen("high_scores")
    object DeathScreen : Screen("death_screen")
    object MerchantShop : Screen("merchant_shop")

}