package com.sq.thed_ck_licker.viewModels

import androidx.compose.runtime.MutableIntState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sq.thed_ck_licker.ecs.states.PlayerState
import com.sq.thed_ck_licker.ecs.systems.CardPullingSystem
import com.sq.thed_ck_licker.ecs.systems.cardSystems.CardsSystem
import com.sq.thed_ck_licker.ecs.systems.characterSystems.PlayerSystem
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val playerSystem: PlayerSystem,
    private val cardPullingSystem: CardPullingSystem,
    private val cardsSystem: CardsSystem
    ) : ViewModel() {

    private val _playerState = MutableStateFlow(
        PlayerState(
            health = playerSystem.getPlayerHealth(),
            maxHealth = playerSystem.getPlayerMaxHealth(),
            score = playerSystem.getPlayerScore(),
            latestCard = playerSystem.getLatestCard()
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

    fun onPullNewCard(latestCard: Int) {
        cardPullingSystem.pullNewCard(latestCard)
    }

    fun onActivateCard(cardCount: MutableIntState) {
        cardsSystem.cardActivation(cardCount)
    }
}