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
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    private val worldCreationSystem: WorldCreationSystem,
    private val pitSystem: PitSystem,
    private val gameNavigator: GameNavigator
) : ViewModel() {

    init {
        viewModelScope.launch {
            GameEvents.eventStream.collect { event ->
                when (event) {
                    is GameEvent.PlayerDied -> gameNavigator.navigateTo(Screen.DeathScreen.route)
                    is GameEvent.ShovelUsed -> gameNavigator.navigateTo(Screen.Pit.route)
                }
            }
        }
    }

    fun restartGame() {
        worldCreationSystem.destroyWorld()
        gameNavigator.restartGame()
    }

    fun dropCardInPit(latestCard: Int) {
        pitSystem.dropCardInPit(latestCard)
    }

    fun leaveGame() {
        worldCreationSystem.destroyWorld()
        gameNavigator.leaveGame()  
    }
}