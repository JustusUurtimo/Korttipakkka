package com.sq.thed_ck_licker.viewModels

import androidx.compose.runtime.MutableIntState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sq.thed_ck_licker.ecs.states.MerchantState
import com.sq.thed_ck_licker.ecs.states.PlayerState
import com.sq.thed_ck_licker.ecs.systems.characterSystems.MerchantSystem
import com.sq.thed_ck_licker.ecs.systems.characterSystems.PlayerSystem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MerchantViewModel @Inject constructor(
    private val merchantSystem: MerchantSystem,
    private val playerSystem: PlayerSystem
) : ViewModel() {

    private val _merchantState = MutableStateFlow(
        MerchantState(
            affinity = merchantSystem.getMerchantAffinity(),
        )
    )
    val merchantState: StateFlow<MerchantState> get() = _merchantState

    init {
        viewModelScope.launch {
            merchantSystem.merchantUpdates().collect { merchantData ->
                _merchantState.value = merchantData
            }
        }
    }

    private val _merchantHand = MutableStateFlow<List<Int>>(emptyList())
    val merchantHand: StateFlow<List<Int>> get() = _merchantHand

    fun onChooseMerchantCard(latestCard: MutableIntState, newcard: Int) {
        merchantSystem.chooseMerchantCard(latestCard, newcard)
        _merchantHand.value = emptyList()
    }

    fun onOpenShop() {
        merchantSystem.reRollMerchantHand().also {
            _merchantHand.value = it
        }
    }

    fun onReRollShop() {
        println("Re-rolling shop")
        val activeMerchantSummonCard = playerSystem.getPlayerActiveMerchantCard()

        if (merchantSystem.getReRollCount(activeMerchantSummonCard) > 1) {
            playerSystem.updateScore(-500)
        }
        merchantSystem.addReRollCount(activeMerchantSummonCard)
        merchantSystem.reRollMerchantHand().also {
            _merchantHand.value = it
        }
    }

}