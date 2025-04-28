package com.sq.thed_ck_licker.ui.theme.viewModels

import androidx.lifecycle.ViewModel
import com.sq.thed_ck_licker.ecs.systems.characterSystems.MerchantSystem
import com.sq.thed_ck_licker.ecs.systems.characterSystems.PlayerSystem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MerchantViewModel(private val merchantSystem: MerchantSystem, private val playerSystem: PlayerSystem) : ViewModel() {
    private val _merchantHand = MutableStateFlow<List<Int>>(emptyList())
    val merchantHand: StateFlow<List<Int>> get() = _merchantHand



    fun onReRollShop(): () -> Unit = {
        val activeMerchantSummonCard = playerSystem.getPlayerActiveMerchantCard()

        if (merchantSystem.getReRollCount(activeMerchantSummonCard).intValue > 1) {
            playerSystem.updateScore(-500)
        }
        merchantSystem.getReRollCount(activeMerchantSummonCard).intValue++
        merchantSystem.reRollMerchantHand().also {
            _merchantHand.value = it
        }
    }

}