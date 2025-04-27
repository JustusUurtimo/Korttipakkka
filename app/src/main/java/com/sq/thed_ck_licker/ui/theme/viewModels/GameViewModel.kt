package com.sq.thed_ck_licker.ui.theme.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sq.thed_ck_licker.ecs.managers.GameEvents
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.sq.thed_ck_licker.ecs.systems.DescriptionSystem.Companion.instance as descriptionSystem
import com.sq.thed_ck_licker.ecs.systems.characterSystems.MerchantSystem.Companion.instance as merchantSystem
import com.sq.thed_ck_licker.ecs.systems.characterSystems.PlayerSystem.Companion.instance as playerSystem

class GameViewModel : ViewModel() {
    private val _isPlayerDead = MutableStateFlow(false)
    val isPlayerDead: StateFlow<Boolean> = _isPlayerDead

    init {
        viewModelScope.launch {
            GameEvents.onPlayerDied.collect {
                _isPlayerDead.value = true
            }
        }
    }

    fun restartGame() {
        _isPlayerDead.value = false
        playerSystem.initPlayer()
        merchantSystem.initRegularMerchant()
        descriptionSystem.updateAllDescriptions()
        // Reset player, entities, map, etc.
    }

    fun exitToMenu() {
        // Trigger navigation or update game state to menu
    }
}