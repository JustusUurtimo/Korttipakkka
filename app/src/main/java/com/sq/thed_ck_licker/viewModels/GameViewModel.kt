package com.sq.thed_ck_licker.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sq.thed_ck_licker.ecs.managers.GameEvent
import com.sq.thed_ck_licker.ecs.managers.GameEvents
import com.sq.thed_ck_licker.ecs.systems.PitSystem
import com.sq.thed_ck_licker.ecs.systems.WorldCreationSystem
import com.sq.thed_ck_licker.helpers.navigation.GameNavigator
import com.sq.thed_ck_licker.helpers.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    private val worldCreationSystem: WorldCreationSystem,
    private val pitSystem: PitSystem,
    private val gameNavigator: GameNavigator
) : ViewModel() {

    private val _isShovelUsed = MutableStateFlow(false)
    val isShovelUsed: StateFlow<Boolean> = _isShovelUsed


    init {
        viewModelScope.launch {
            GameEvents.eventStream.collect { event ->
                println("Game event: $event")
                when (event) {
                    is GameEvent.PlayerDied -> gameNavigator.navigateTo(Screen.DeathScreen.route)
                    is GameEvent.ShovelUsed -> _isShovelUsed.value = (!isShovelUsed.value)
                }
            }
        }
    }

    fun restartGame() {
        _isShovelUsed.value = false
        worldCreationSystem.destroyWorld()
        gameNavigator.restartGame()
    }

    fun dropCardInHole(latestCard: Int) {
        pitSystem.dropCardInHole(latestCard)
    }

    fun leaveGame() {
        _isShovelUsed.value = false
        worldCreationSystem.destroyWorld()
        gameNavigator.leaveGame()  
    }
}