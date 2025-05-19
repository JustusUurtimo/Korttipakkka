package com.sq.thed_ck_licker.viewModels

import androidx.compose.runtime.MutableIntState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sq.thed_ck_licker.ecs.managers.GameEvent
import com.sq.thed_ck_licker.ecs.managers.GameEvents
import com.sq.thed_ck_licker.ecs.systems.PitSystem
import com.sq.thed_ck_licker.ecs.systems.WorldCreationSystem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    private val worldCreationSystem: WorldCreationSystem,
    private val pitSystem: PitSystem,
) : ViewModel() {

    private val _isPlayerDead = MutableStateFlow(false)
    val isPlayerDead: StateFlow<Boolean> = _isPlayerDead

    private val _isShovelUsed = MutableStateFlow(false)
    val isShovelUsed: StateFlow<Boolean> = _isShovelUsed

    init {
        viewModelScope.launch {
            GameEvents.eventStream.collect { event ->
                when (event) {
                    is GameEvent.PlayerDied -> _isPlayerDead.value = true
                    is GameEvent.ShovelUsed -> _isShovelUsed.value = (!isShovelUsed.value)
                }
            }
        }
    }

    fun restartGame() {
        _isPlayerDead.value = false
        _isShovelUsed.value = false
        worldCreationSystem.destroyWorld()
    }

    fun dropCardInHole(latestCard: MutableIntState) {
        pitSystem.dropCardInHole(latestCard)
    }
}