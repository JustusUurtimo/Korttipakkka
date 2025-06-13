package com.sq.thed_ck_licker.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sq.thed_ck_licker.ecs.components.misc.LatestCardComponent
import com.sq.thed_ck_licker.ecs.managers.EntityId
import com.sq.thed_ck_licker.ecs.managers.EntityManager.getPlayerID
import com.sq.thed_ck_licker.ecs.managers.get
import com.sq.thed_ck_licker.ecs.states.PlayerState
import com.sq.thed_ck_licker.ecs.systems.CardPullingSystem
import com.sq.thed_ck_licker.ecs.systems.cardSystems.CardsSystem
import com.sq.thed_ck_licker.ecs.systems.characterSystems.PlayerSystem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

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
            latestCard = (getPlayerID() get LatestCardComponent::class).getLatestCard()
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

    fun onPullNewCard(ownerId: EntityId) {
        cardPullingSystem.pullNewCard(ownerId)
    }

    fun onActivateCard(ownerId: EntityId) {
        cardsSystem.cardActivation(ownerId)
    }
}