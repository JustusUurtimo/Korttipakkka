package com.sq.thed_ck_licker.viewModels

import androidx.lifecycle.ViewModel
import com.sq.thed_ck_licker.ecs.systems.RewardSystem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class RewardViewModel @Inject constructor(private val rewardSystem: RewardSystem) : ViewModel() {

    private val _rewardSelection = MutableStateFlow<List<Int>>(emptyList())
    val rewardSelection: StateFlow<List<Int>> get() = _rewardSelection

    fun getRewardCards(): List<Int> {
        return rewardSystem.getRewardCards().also {
            _rewardSelection.value = it
        }
    }

    fun selectReward(chosenCard: Int) {
        rewardSystem.selectReward(chosenCard)
        _rewardSelection.value = emptyList()
    }
}
