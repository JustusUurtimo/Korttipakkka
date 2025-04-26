package com.sq.thed_ck_licker.ecs.systems.viewSystems

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf

class GameViewSystem {
    private val _isPlayerDead = mutableStateOf(false)
    val isPlayerDead: State<Boolean> = _isPlayerDead

    fun playerDied() {
        _isPlayerDead.value = true
    }

    fun restartGame() {
        _isPlayerDead.value = false
    }

    fun exitToMenu() {
        // Handle navigation to menu. Will be used in #58 issue
    }
}