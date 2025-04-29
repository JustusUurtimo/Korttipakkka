package com.sq.thed_ck_licker.ui.theme.viewModels

import androidx.compose.runtime.MutableIntState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sq.thed_ck_licker.ecs.states.PlayerState
import com.sq.thed_ck_licker.ecs.systems.characterSystems.PlayerSystem
import com.sq.thed_ck_licker.ecs.systems.pullNewCardSystem
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val playerSystem: PlayerSystem
) : ViewModel() {

    private val _playerState = MutableStateFlow(
        PlayerState(
            health = playerSystem.getPlayerHealth(),
            maxHealth = playerSystem.getPlayerMaxHealth(),
            score = playerSystem.getPlayerScore(),
            merchantId = playerSystem.getPlayerMerchantId()
        )
    )
    val playerState: StateFlow<PlayerState> get() = _playerState

    init {
        viewModelScope.launch {
            playerSystem.playerUpdates().collect { playerData ->
                _playerState.value = playerData
            }
        }
    }

    fun onPullNewCard(latestCard: MutableIntState) {
        pullNewCardSystem(latestCard)
        playerSystem.updateMerchantId(-1)
    }
}