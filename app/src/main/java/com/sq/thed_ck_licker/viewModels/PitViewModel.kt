package com.sq.thed_ck_licker.viewModels

import androidx.lifecycle.ViewModel
import com.sq.thed_ck_licker.ecs.systems.PitSystem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class PitViewModel @Inject constructor(
    private val pitSystem: PitSystem,
) : ViewModel() {

    private val _pitCardSelection = MutableStateFlow<List<Int>>(emptyList())
    val pitCardSelection: StateFlow<List<Int>> get() = _pitCardSelection

    fun buyShovel() {
        pitSystem.buyShovel()
        _pitCardSelection.value = emptyList()
    }

    fun getPitCards(): List<Int> {
        return pitSystem.getPitCards().also {
            _pitCardSelection.value = it
        }
    }

    fun dropCardInPit(chosenCard: Int) {
        pitSystem.dropCardInPit(chosenCard)
        _pitCardSelection.value = emptyList()
    }

}