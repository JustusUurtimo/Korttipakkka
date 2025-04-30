package com.sq.thed_ck_licker.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sq.thed_ck_licker.ecs.managers.GameEvents
import com.sq.thed_ck_licker.ecs.systems.WorldCreationSystem
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class GameViewModel @Inject constructor(
    private val worldCreationSystem: WorldCreationSystem
) : ViewModel() {

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
        worldCreationSystem.destroyWorld()
        // Reset player, entities, map, etc.
    }

    fun exitToMenu() {
        // Trigger navigation or update game state to menu
    }
}