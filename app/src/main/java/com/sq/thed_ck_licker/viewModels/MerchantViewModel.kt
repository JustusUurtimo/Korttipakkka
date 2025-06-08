package com.sq.thed_ck_licker.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sq.thed_ck_licker.ecs.managers.MerchantEvent
import com.sq.thed_ck_licker.ecs.managers.MerchantEvents
import com.sq.thed_ck_licker.ecs.states.MerchantState
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
    private val playerSystem: PlayerSystem,
) : ViewModel() {

    private val _activeMerchantId = MutableStateFlow<Int>(-1)
    val activeMerchantId: StateFlow<Int> get() = _activeMerchantId

    private val _merchantSummonCard = MutableStateFlow<Int>(-1)
    val merchantSummonCard: StateFlow<Int> get() = _merchantSummonCard

    private val _merchantHand = MutableStateFlow<List<Int>>(emptyList())
    val merchantHand: StateFlow<List<Int>> get() = _merchantHand

    private val _merchantState = MutableStateFlow(
        MerchantState(
            affinity = merchantSystem.getMerchantAffinity(activeMerchantId)
        )
    )
    val merchantState: StateFlow<MerchantState> get() = _merchantState

    init {
        viewModelScope.launch {
            merchantSystem.merchantUpdates(activeMerchantId).collect { merchantData ->
                _merchantState.value = merchantData
            }
        }
        viewModelScope.launch {
            MerchantEvents.eventStream.collect { event ->
                when (event) {
                    is MerchantEvent.MerchantShopOpened -> {
                        // Handle merchant shop opened
                        _activeMerchantId.value = event.merchantId
                        _merchantSummonCard.value = event.cardEntity
                    }
                    is MerchantEvent.MerchantShopClosed -> {
                        // Handle merchant shop closed
                        _activeMerchantId.value = -1
                    }
                }
            }
        }
    }

    fun onChooseMerchantCard(newCard: Int, activeMerchant: Int) {
        merchantSystem.chooseMerchantCard(newCard, activeMerchant)
        _merchantHand.value = emptyList()

    }

    fun onOpenShop(merchantId: Int) {
        merchantSystem.rollMerchantHand(merchantId).also {
            _merchantHand.value = it
        }
    }

    fun onReRollShop(activeMerchantSummonCard: Int, merchantId: Int) {
        if (merchantSystem.getReRollCount(activeMerchantSummonCard) > 1) {
            playerSystem.updateScore(-500)
        }
        merchantSystem.addReRollCount(activeMerchantSummonCard)
        merchantSystem.rollMerchantHand(merchantId).also {
            _merchantHand.value = it
        }
    }

}